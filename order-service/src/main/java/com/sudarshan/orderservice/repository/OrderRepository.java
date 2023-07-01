package com.sudarshan.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sudarshan.orderservice.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
