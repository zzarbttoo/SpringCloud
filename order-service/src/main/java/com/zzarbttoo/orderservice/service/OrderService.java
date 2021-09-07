package com.zzarbttoo.orderservice.service;


import com.zzarbttoo.orderservice.dto.OrderDto;
import com.zzarbttoo.orderservice.jpa.OrderEntity;

public interface OrderService {

    OrderDto createOrder(OrderDto orderDto);
    OrderDto getOrderByOrderId(String orderId);
    Iterable<OrderEntity> getOrdersByUserId(String userId);



}
