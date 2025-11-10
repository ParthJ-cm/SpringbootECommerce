package com.shop.product_service.controller;

import com.shop.product_service.dto.*;
import com.shop.product_service.service.interfaces.ProductService;
import com.shop.product_service.service.interfaces.ProductVariantService;
import com.shop.product_service.service.interfaces.ReviewService;
import com.shop.product_service.service.interfaces.VariantAttributeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductVariantService productVariantService;
    private final ReviewService reviewService;
    private final VariantAttributeService variantAttributeService;

    @PostMapping("/review")
    public ResponseEntity<ReviewDTO> saveReview(@Valid @RequestBody SaveReviewDTO saveReviewDTO){
        ReviewDTO reviewDTO = reviewService.save(saveReviewDTO);
        return ResponseEntity.ok(reviewDTO);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody SaveProductDTO saveProductDTO){
        ProductDTO productDTO = productService.createProduct(saveProductDTO);
        return ResponseEntity.ok(productDTO);
    }

    @PostMapping("/{id}/variants")
    public ResponseEntity<List<ProductVariantDTO>> createVariant(@PathVariable Long id, @Valid @RequestBody List<SaveVariantDTO> saveVariantDTOs){
        List<ProductVariantDTO> variants = productVariantService.create(id, saveVariantDTOs);
        return ResponseEntity.ok(variants);
    }

    @PostMapping("/variants/{variantId}/attributes")
    public ResponseEntity<List<VariantAttributeDTO>> createAttributes(@PathVariable Long variantId, @Valid @RequestBody List<SaveVariantAttributeDTO> saveVariantAttributeDTOS){
        List<VariantAttributeDTO> variantAttributes = variantAttributeService.create(variantId, saveVariantAttributeDTOS);
        return ResponseEntity.ok(variantAttributes);
    }
}