package com.shop.product_service.service.implementations;

import com.shop.product_service.constants.EntityName;
import com.shop.product_service.constants.MessageConstants;
import com.shop.product_service.dto.SaveVariantAttributeDTO;
import com.shop.product_service.dto.SaveVariantDTO;
import com.shop.product_service.exceptions.AlreadyExistsException;
import com.shop.product_service.exceptions.EntityNotFoundException;
import com.shop.product_service.repository.AttributeRepository;
import com.shop.product_service.repository.ProductRepository;
import com.shop.product_service.repository.ProductVariantRepository;
import com.shop.product_service.service.interfaces.ValidateProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ValidateProductServiceImpl implements ValidateProductService {

    private final AttributeRepository attributeRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;

    @Override
    public void validateAttribute(Long attributeId) {
        if (attributeId == null) {
            throw new IllegalArgumentException("Attribute Id is required");
        }

        boolean exists = productVariantRepository.existsByIdAndIsDeletedFalse(attributeId);
        if (!exists) {
            throw new EntityNotFoundException(
                    MessageConstants.notFound(EntityName.ATTRIBUTE, attributeId)
            );
        }
    }

    @Override
    public void validateAttributes(List<Long> attributeIds) {

        if (attributeIds.isEmpty()) {
            return;
        }

        List<Long> existing = attributeRepository.findExistingIds(attributeIds);

        if (existing.size() != attributeIds.size()) {
            List<Long> missing = attributeIds.stream().filter(id -> !existing.contains(id)).toList();

            throw new EntityNotFoundException("One or more attributes not found: " + missing);
        }
    }

    @Override
    public void validateProduct(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product Id is required");
        }

        boolean exists = productRepository.existsByIdAndIsDeletedFalse(productId);
        if (!exists) {
            throw new EntityNotFoundException(
                    MessageConstants.notFound(EntityName.PRODUCT, productId)
            );
        }
    }

    @Override
    public void validateProductVariant(Long productVariantId) {
        if (productVariantId == null) {
            throw new IllegalArgumentException("Product Variant Id is required");
        }

        boolean exists = productVariantRepository.existsByIdAndIsDeletedFalse(productVariantId);
        if (!exists) {
            throw new EntityNotFoundException(
                    MessageConstants.notFound(EntityName.PRODUCT_VARIANT, productVariantId)
            );
        }
    }

    @Override
    public void validateProductVariants(List<SaveVariantDTO> variants) {
        validateDuplicateSkus(variants);
        validateExistingSkus(variants);
        validateAttributeIds(variants);
    }

    @Override
    public void validatePrimaryVariant(List<SaveVariantDTO> variants) {

        long primaryCount = variants.stream()
                .map(SaveVariantDTO::getIsPrimary)
                .filter(Boolean.TRUE::equals)
                .count();

        if (primaryCount != 1) {
            throw new IllegalArgumentException("Exactly one variant must be primary.");
        }
    }

    private void validateAttributeIds(List<SaveVariantDTO> variants) {

        List<Long> attributeIds = variants.stream()
                .flatMap(v -> Optional.ofNullable(v.getVariantAttributes())
                        .orElse(List.of())
                        .stream())
                .map(SaveVariantAttributeDTO::getAttributeId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        validateAttributes(attributeIds);
    }

    private void validateDuplicateSkus(List<SaveVariantDTO> variants) {

        Set<String> duplicates = variants.stream()
                .map(SaveVariantDTO::getSku)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()))
                .entrySet()
                .stream()
                .filter(e -> e.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        if (!duplicates.isEmpty()) {
            throw new IllegalArgumentException("Duplicate SKUs found: " + duplicates);
        }
    }

    private void validateExistingSkus(List<SaveVariantDTO> variants) {

        List<String> skuList = variants.stream()
                .map(SaveVariantDTO::getSku)
                .filter(Objects::nonNull)
                .toList();

        List<String> existingSkus = productVariantRepository.findExistingSkus(skuList);

        if (!existingSkus.isEmpty()) {
            throw new AlreadyExistsException("One or more SKUs already exist: " + existingSkus);
        }
    }

}
