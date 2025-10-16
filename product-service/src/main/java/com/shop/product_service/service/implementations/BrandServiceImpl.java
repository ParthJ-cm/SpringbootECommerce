package com.shop.product_service.service.implementations;

import com.shop.product_service.constants.MessageConstants;
import com.shop.product_service.dto.BrandDTO;
import com.shop.product_service.dto.SaveBrandDTO;
import com.shop.product_service.entity.Brand;
import com.shop.product_service.exceptions.AlreadyExistsException;
import com.shop.product_service.repository.BrandRepository;
import com.shop.product_service.service.interfaces.BrandService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final ModelMapper modelMapper;

    @Override
    public BrandDTO saveBrand(SaveBrandDTO saveBrandDTO) {

        Optional<Brand> existingBrand = brandRepository.findByName(saveBrandDTO.getName());

        if(existingBrand.isPresent() && !Objects.equals(existingBrand.get().getBrandId(), saveBrandDTO.getId())){
            throw new AlreadyExistsException(
                    String.format(MessageConstants.Brand.EXISTS,saveBrandDTO.getName())
            );
        }

        Brand brand = modelMapper.map(saveBrandDTO, Brand.class);
        brand = brandRepository.save(brand);
        return modelMapper.map(brand, BrandDTO.class);
    }

}