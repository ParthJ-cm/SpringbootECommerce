package com.shop.product_service.service.implementations;

import com.shop.product_service.dto.ProductVariantDTO;
import com.shop.product_service.dto.SaveVariantDTO;
import com.shop.product_service.dto.SaveVariantAttributeDTO;
import com.shop.product_service.entity.Product;
import com.shop.product_service.entity.ProductVariant;
import com.shop.product_service.entity.VariantAttribute;
import com.shop.product_service.repository.AttributeRepository;
import com.shop.product_service.repository.ProductRepository;
import com.shop.product_service.repository.ProductVariantRepository;
import com.shop.product_service.service.interfaces.ProductVariantService;
import com.shop.product_service.service.interfaces.ValidateProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {

    private final AttributeRepository attributeRepository;
    private final ModelMapper modelMapper;
    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository;
    private final ValidateProductService validateProductService;

    @Transactional
    public List<ProductVariantDTO> create(Long productId, List<SaveVariantDTO> variantDTOs) {
        validateProductService.validatePrimaryVariant(variantDTOs);
        validateProductService.validateProduct(productId);
        validateProductService.validateProductVariants(variantDTOs);

        if(productVariantRepository.existsPrimaryVariant(productId)){
            throw new IllegalArgumentException("Primary Variant is already present");
        }

        Product productRef = productRepository.getReferenceById(productId);

        List<ProductVariant> variants = new ArrayList<>();

        for(SaveVariantDTO vdto : variantDTOs){
            ProductVariant variant = new ProductVariant();
            variant.setName(vdto.getName());
            variant.setSku(vdto.getSku());
            variant.setPrice(vdto.getPrice());
            variant.setStockQuantity(vdto.getStockQuantity());
            variant.setIsPrimary(vdto.getIsPrimary());
            variant.setImageUrl(vdto.getImageUrl());
            variant.setProduct(productRef);

            if(vdto.getVariantAttributes() != null){
                for(SaveVariantAttributeDTO adto : vdto.getVariantAttributes()){
                    VariantAttribute attr = new VariantAttribute();
                    attr.setAttribute(attributeRepository.getReferenceById(adto.getAttributeId()));
                    attr.setAttributeValue(adto.getAttributeValue());
                    attr.setProductVariant(variant);

                    variant.getVariantAttributes().add(attr);
                }
            }

            variants.add(variant);
        }

        List<ProductVariant> savedVariants = productVariantRepository.saveAll(variants);

        return savedVariants.stream()
                .map(v -> modelMapper.map(v, ProductVariantDTO.class))
                .toList();
    }
}
