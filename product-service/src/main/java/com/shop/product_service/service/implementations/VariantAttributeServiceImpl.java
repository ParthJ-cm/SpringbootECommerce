package com.shop.product_service.service.implementations;

import com.shop.product_service.dto.SaveVariantAttributeDTO;
import com.shop.product_service.dto.VariantAttributeDTO;
import com.shop.product_service.entity.Attribute;
import com.shop.product_service.entity.ProductVariant;
import com.shop.product_service.entity.VariantAttribute;
import com.shop.product_service.exceptions.EntityNotFoundException;
import com.shop.product_service.repository.AttributeRepository;
import com.shop.product_service.repository.ProductVariantRepository;
import com.shop.product_service.repository.VariantAttributeRepository;
import com.shop.product_service.service.interfaces.ValidateProductService;
import com.shop.product_service.service.interfaces.VariantAttributeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VariantAttributeServiceImpl implements VariantAttributeService {

    private final AttributeRepository attributeRepository;
    private final ModelMapper modelMapper;
    private final ProductVariantRepository productVariantRepository;
    private final VariantAttributeRepository variantAttributeRepository;
    private final ValidateProductService validateProductService;

    @Override
    @Transactional
    public List<VariantAttributeDTO> create(Long productVariantId, List<SaveVariantAttributeDTO> variantAttributeDTOs) {
        validateProductService.validateProductVariant(productVariantId);

        List<Long> attributeIds = variantAttributeDTOs.stream()
                .map(SaveVariantAttributeDTO::getAttributeId)
                .distinct()
                .toList();

        validateProductService.validateAttributes(attributeIds);

        ProductVariant productVariantRef = productVariantRepository.getReferenceById(productVariantId);

        List<Long> ids = variantAttributeDTOs.stream()
                .map(SaveVariantAttributeDTO::getAttributeId)
                .toList();

        List<Long> existingIds = attributeRepository.findExistingIds(ids);
        if (existingIds.size() != ids.size()) {
            throw new EntityNotFoundException("One or more attributes not found");
        }

        List<VariantAttribute> variantAttributes = variantAttributeDTOs.stream()
                .map(dto ->{
                    VariantAttribute variantAttribute = modelMapper.map(dto, VariantAttribute.class);
                    Attribute attributeRef = attributeRepository.getReferenceById(dto.getAttributeId());
                    variantAttribute.setAttribute(attributeRef);
                    variantAttribute.setProductVariant(productVariantRef);
                    return variantAttribute;})
                .toList();

        List<VariantAttribute> savedVariantAttributes = variantAttributeRepository.saveAll(variantAttributes);

        return savedVariantAttributes.stream()
                .map(variantAttribute -> modelMapper.map(variantAttribute, VariantAttributeDTO.class))
                .toList();
    }

}
