package com.shop.product_service.service.interfaces;

import com.shop.product_service.dto.ReviewDTO;
import com.shop.product_service.dto.SaveReviewDTO;

public interface ReviewService {
    ReviewDTO save(SaveReviewDTO saveReviewDTO);
}