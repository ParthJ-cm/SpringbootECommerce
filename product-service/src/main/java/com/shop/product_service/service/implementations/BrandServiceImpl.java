package com.shop.product_service.service.implementations;

import com.shop.product_service.constants.CommonMessageTemplate;
import com.shop.product_service.constants.EntityName;
import com.shop.product_service.constants.MessageConstants;
import com.shop.product_service.dto.BrandDTO;
import com.shop.product_service.dto.SaveBrandDTO;
import com.shop.product_service.entity.Brand;
import com.shop.product_service.exceptions.AlreadyExistsException;
import com.shop.product_service.exceptions.EntityNotFoundException;
import com.shop.product_service.repository.BrandRepository;
import com.shop.product_service.service.interfaces.BrandService;
import com.shop.product_service.service.interfaces.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public BrandDTO saveBrand(SaveBrandDTO saveBrandDTO) {
        //prevent same brand name
        brandRepository.findByNameAndIsDeletedFalse(saveBrandDTO.getName())
                .filter(brand -> !Objects.equals(brand.getBrandId(), saveBrandDTO.getId()))
                .ifPresent(brand -> {
                    throw new AlreadyExistsException(
                            MessageConstants.exists(EntityName.BRAND,saveBrandDTO.getName()));
                });

        Long brandId = saveBrandDTO.getId();
        Brand brand = (brandId == null)
                ? new Brand()
                : brandRepository.findByBrandIdAndIsDeletedFalse(brandId)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageConstants.notFound(EntityName.BRAND,brandId)));

        modelMapper.map(saveBrandDTO, brand);
        Brand savedBrand = brandRepository.save(brand);
        return modelMapper.map(savedBrand, BrandDTO.class);
    }

    @Override
    public BrandDTO getBrand(Long id) {

        Brand brand = brandRepository.findByBrandIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageConstants.notFound(EntityName.BRAND,id)));

        return modelMapper.map(brand, BrandDTO.class);
    }

    @Override
    @Transactional
    public void deleteBrand(Long id) {
        Brand brand = brandRepository.findByBrandIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageConstants.notFound(EntityName.BRAND,id)));

        //Delete all products of that brand
        productService.deleteProductsByBrandId(id);

        brand.setIsDeleted(true);
        brandRepository.save(brand);
    }

    public Page<BrandDTO> paginatedBrands(String search, Pageable pageable) {
        if (search == null || search.isBlank()) {
            search = "";
        }
        Page<Brand> brandPages = brandRepository.findByNameContainingIgnoreCaseAndIsDeletedFalse(search, pageable);

        return brandPages.map(brand -> modelMapper.map(brand, BrandDTO.class));
    }

}