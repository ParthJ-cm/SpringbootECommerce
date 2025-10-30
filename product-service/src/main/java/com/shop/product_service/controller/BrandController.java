package com.shop.product_service.controller;

import com.shop.product_service.dto.BrandDTO;
import com.shop.product_service.dto.SaveBrandDTO;
import com.shop.product_service.service.interfaces.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @GetMapping("/{id}")
    public ResponseEntity<BrandDTO> getBrand(@PathVariable Long id){
        BrandDTO brandDTO = brandService.getBrand(id);
        return ResponseEntity.ok(brandDTO);
    }

    @PostMapping
    public ResponseEntity<BrandDTO> saveBrand(@Valid @RequestBody SaveBrandDTO saveBrandDTO){
        BrandDTO brandDTO = brandService.saveBrand(saveBrandDTO);
        return ResponseEntity.ok(brandDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id){
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<Page<BrandDTO>> getAllBrands(@RequestParam(required = false) String searchByBrandName, @PageableDefault(size = 10, sort="id") Pageable page){
        Page<BrandDTO> records = brandService.paginatedBrands(searchByBrandName, page);
        return ResponseEntity.ok(records);
    }

}