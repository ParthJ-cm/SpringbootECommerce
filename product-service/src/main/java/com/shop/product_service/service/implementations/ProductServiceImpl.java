package com.shop.product_service.service.implementations;

import com.shop.product_service.constants.EntityName;
import com.shop.product_service.constants.MessageConstants;
import com.shop.product_service.dto.*;
import com.shop.product_service.entity.Product;
import com.shop.product_service.entity.ProductVariant;
import com.shop.product_service.entity.VariantAttribute;
import com.shop.product_service.exceptions.EntityNotFoundException;
import com.shop.product_service.repository.*;
import com.shop.product_service.service.interfaces.ProductService;
import com.shop.product_service.service.interfaces.ValidateProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final AttributeRepository attributeRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ValidateProductService validateProductService;

    @Override
    @Transactional
    public void deleteProductsByBrandId(Long brandId) {
        List<Product> products = productRepository.findByBrand_Id(brandId);
        products.forEach(product -> product.setIsDeleted(true));
        productRepository.saveAll(products);
    }

    private void validateBrand(Long brandId) {
        if (brandId == null) {
            throw new IllegalArgumentException(MessageConstants.isRequired(EntityName.BRAND,"id"));
        }

        boolean exists = brandRepository.existsById(brandId);
        if (!exists) {
            throw new EntityNotFoundException(MessageConstants.notFound(EntityName.BRAND, brandId));
        }
    }

    private void validateCategories(List<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) return;

        List<Long> existingIds = categoryRepository.findExistingIds(categoryIds);

        if (existingIds.size() != categoryIds.size()) {
            List<Long> missing = categoryIds.stream().filter(id -> !existingIds.contains(id)).toList();

            throw new EntityNotFoundException("One or more categories not found: " + missing);
        }
    }

    private void validatePrimaryVariant(List<SaveVariantDTO> variants) {

        long primaryCount = variants.stream()
                .map(SaveVariantDTO::getIsPrimary)
                .filter(Boolean.TRUE::equals)
                .count();

        if (primaryCount != 1) {
            throw new IllegalArgumentException("Exactly one variant must be primary.");
        }
    }

    @Override
    @Transactional
    public ProductDTO createProduct(SaveProductDTO dto) {

        validatePrimaryVariant(dto.getVariants());
        validateBrand(dto.getBrandId());
        validateCategories(dto.getCategoryIds());
        validateProductService.validateProductVariants(dto.getVariants());

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setImageUrl(dto.getImageUrl());
        product.setStockQuantity(dto.getStockQuantity());

        product.setBrand(brandRepository.getReferenceById(dto.getBrandId()));

        if (dto.getCategoryIds() != null) {
            product.setCategories(categoryRepository.findAllById(dto.getCategoryIds()));
        }

        for (SaveVariantDTO vdto : dto.getVariants()) {
            ProductVariant variant = new ProductVariant();

            variant.setName(vdto.getName());
            variant.setSku(vdto.getSku());
            variant.setPrice(vdto.getPrice());
            variant.setStockQuantity(vdto.getStockQuantity());
            variant.setIsPrimary(vdto.getIsPrimary());
            variant.setImageUrl(vdto.getImageUrl());
            variant.setProduct(product);

            for (SaveVariantAttributeDTO adto : vdto.getVariantAttributes()) {
                VariantAttribute attr = new VariantAttribute();
                attr.setAttribute(attributeRepository.getReferenceById(adto.getAttributeId()));
                attr.setAttributeValue(adto.getAttributeValue());
                attr.setProductVariant(variant);

                variant.getVariantAttributes().add(attr);
            }

            product.getVariants().add(variant);
        }

        Product savedProduct = productRepository.save(product);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

}