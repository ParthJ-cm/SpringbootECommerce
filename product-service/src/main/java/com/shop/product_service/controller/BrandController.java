package com.shop.product_service.controller;

import com.shop.product_service.dto.BrandDTO;
import com.shop.product_service.dto.SaveBrandDTO;
import com.shop.product_service.service.interfaces.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping
    public ResponseEntity<BrandDTO> saveBrand(@RequestBody SaveBrandDTO saveBrandDTO){
        BrandDTO brandDTO = brandService.saveBrand(saveBrandDTO);
        return ResponseEntity.ok(brandDTO);
    }
}