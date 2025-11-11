package com.shop.order_service.kafka;

import com.shop.order_service.dto.OrderDTO;
import com.shop.order_service.dto.SaveOrderDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC_ORDER_CREATED = "order-created";
    private static final String TOPIC_ORDER_STATUS_UPDATED = "order-status-updated";

    public void orderCreatedEvent(OrderDTO orderDTO) {
        String key = String.valueOf(orderDTO.getId());
        kafkaTemplate.send(TOPIC_ORDER_CREATED, key, orderDTO);
//        ListenableFuture<SendResult<String, Object>> future =
//
//        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
//            @Override
//            public void onSuccess(SendResult<String, Object> result) {
//                log.info("Published order-created for orderId={} to partition={} offset={}",
//                        orderDTO.getId(),
//                        result.getRecordMetadata().partition(),
//                        result.getRecordMetadata().offset());
//            }
//
//            @Override
//            public void onFailure(Throwable ex) {
//                log.error("Failed to publish order-created for orderId={}", orderDTO.getId(), ex);
//                // optionally: retry or persist to outbox
//            }
//        });
        //kafkaTemplate.send(TOPIC_ORDER_CREATED, orderDTO);
    }

    public void orderStatusUpdatedEvent(OrderDTO orderDTO) {
        String key = String.valueOf(orderDTO.getId());
        kafkaTemplate.send(TOPIC_ORDER_STATUS_UPDATED, key, orderDTO);
    }

}
