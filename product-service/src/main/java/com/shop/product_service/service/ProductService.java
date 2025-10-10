package com.shop.product_service.service;

import com.shop.product_service.dto.AttributeDTO;
import com.shop.product_service.dto.ProductDTO;
import com.shop.product_service.entity.Category;
import com.shop.product_service.entity.Product;
import com.shop.product_service.entity.ProductAttribute;
import com.shop.product_service.repository.CategoryRepository;
import com.shop.product_service.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = new Product();
        modelMapper.map(productDTO, product);
        product.setAttributes(new ArrayList<>()); // prevent modelMapper from mapping transient ones


        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        product.setStockQuantity(productDTO.getStockQuantity() != null ? productDTO.getStockQuantity() : 0);
        product.setIsActive(productDTO.getIsActive() != null ? productDTO.getIsActive() : true);

        if (productDTO.getAttributes() == null || productDTO.getAttributes().isEmpty()) {
            if (productDTO.getPrice() == null) {
                throw new RuntimeException("Price is required when attributes are null or empty");
            }
        }

        if (productDTO.getCategoryIds() != null) {
            for (Long categoryId : productDTO.getCategoryIds()) {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
                product.getCategories().add(category);
            }
        }

        Product savedProduct = productRepository.save(product);

        if (productDTO.getAttributes() != null && !productDTO.getAttributes().isEmpty()) {
            for (AttributeDTO attributeDTO : productDTO.getAttributes()) {
                ProductAttribute attribute = new ProductAttribute();
                attribute.setAttributeName(attributeDTO.getAttributeName());
                attribute.setAttributePrice(attributeDTO.getAttributePrice());
                attribute.setProduct(savedProduct); // now it's persistent!
                savedProduct.getAttributes().add(attribute);
            }
            savedProduct = productRepository.save(savedProduct); // save again to persist attributes
        }

        return mapToDTO(savedProduct);
    }


    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStockQuantity(productDTO.getStockQuantity() != null ? productDTO.getStockQuantity() : product.getStockQuantity());
        product.setSku(productDTO.getSku());
        product.setImageUrl(productDTO.getImageUrl());
        product.setVendorId(productDTO.getVendorId());
        product.setIsActive(productDTO.getIsActive() != null ? productDTO.getIsActive() : product.getIsActive());
        product.setUpdatedBy(productDTO.getUpdatedBy());
        product.setUpdatedAt(LocalDateTime.now());

        product.getCategories().clear();
        if (productDTO.getCategoryIds() != null) {
            for (Long categoryId : productDTO.getCategoryIds()) {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
                product.getCategories().add(category);
            }
        }

        Product savedProduct = productRepository.save(product);

        savedProduct.getAttributes().clear(); // remove existing attributes

        if (productDTO.getAttributes() != null && !productDTO.getAttributes().isEmpty()) {
            for (AttributeDTO attributeDTO : productDTO.getAttributes()) {
                ProductAttribute attribute = new ProductAttribute();
                attribute.setAttributeName(attributeDTO.getAttributeName());
                attribute.setAttributePrice(attributeDTO.getAttributePrice());
                attribute.setProduct(savedProduct); // attach managed product
                savedProduct.getAttributes().add(attribute);
            }
            savedProduct = productRepository.save(savedProduct); // persist new attributes
        } else if (productDTO.getPrice() == null) {
            throw new RuntimeException("Price is required when attributes are null or empty");
        }

        return mapToDTO(savedProduct);
    }


    public ProductDTO getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        return mapToDTO(product);
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        productRepository.delete(product);
    }

    private ProductDTO mapToDTO(Product product) {
        ProductDTO dto = modelMapper.map(product, ProductDTO.class);
        dto.setCategoryIds(product.getCategories().stream()
                .map(Category::getCategoryId)
                .collect(Collectors.toList()));
        dto.setAttributes(product.getAttributes().stream()
                .map(attribute -> new AttributeDTO(attribute.getAttributeName(), attribute.getAttributePrice()))
                .collect(Collectors.toList()));
        return dto;
    }
}