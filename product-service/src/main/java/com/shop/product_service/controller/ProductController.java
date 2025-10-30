package com.shop.product_service.controller;

import com.shop.product_service.dto.ReviewDTO;
import com.shop.product_service.dto.SaveReviewDTO;
import com.shop.product_service.service.interfaces.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ReviewService reviewService;

    @PostMapping("/review")
    public ResponseEntity<ReviewDTO> saveReview(@Valid @RequestBody SaveReviewDTO saveReviewDTO){
        ReviewDTO reviewDTO = reviewService.save(saveReviewDTO);
        return ResponseEntity.ok(reviewDTO);
    }
}