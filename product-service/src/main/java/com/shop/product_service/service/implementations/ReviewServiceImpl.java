package com.shop.product_service.service.implementations;

import com.shop.product_service.constants.EntityName;
import com.shop.product_service.constants.MessageConstants;
import com.shop.product_service.dto.ReviewDTO;
import com.shop.product_service.dto.SaveReviewDTO;
import com.shop.product_service.entity.Product;
import com.shop.product_service.entity.Review;
import com.shop.product_service.exceptions.EntityNotFoundException;
import com.shop.product_service.repository.ProductRepository;
import com.shop.product_service.repository.ReviewRepository;
import com.shop.product_service.service.interfaces.ReviewService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public ReviewDTO save(SaveReviewDTO saveReviewDTO) {
        Long userId = saveReviewDTO.getUserId();
        Long productId = saveReviewDTO.getProductId();
        Review review = reviewRepository.findByProduct_IdAndUserId(productId, userId).orElseGet(Review::new);

        if(review.getId() == null){
            if (!productRepository.existsById(productId)) {
                throw new EntityNotFoundException(
                        MessageConstants.notFound(EntityName.PRODUCT, productId)
                );
            }
            Product productRef = productRepository.getReferenceById(productId);
            review.setProduct(productRef);
        }

        modelMapper.map(saveReviewDTO, review);
        Review savedReview = reviewRepository.save(review);
        return modelMapper.map(savedReview, ReviewDTO.class);
    }
}