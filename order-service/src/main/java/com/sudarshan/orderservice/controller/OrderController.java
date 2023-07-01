package com.sudarshan.orderservice.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sudarshan.orderservice.dto.OrderRequest;
import com.sudarshan.orderservice.dto.OrderResponse;
import com.sudarshan.orderservice.service.OrderService;

@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
public class OrderController {
  private final OrderService orderService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @CircuitBreaker(name = "inventory", fallbackMethod = "fallBackMethod")
  @TimeLimiter(name = "inventory")
  @Retry(name = "inventory")
  public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest){
    return CompletableFuture.supplyAsync(() -> orderService.placeOrder(orderRequest));
  }

  public CompletableFuture<String> fallBackMethod(OrderRequest orderRequest, RuntimeException runtimeException){
    return CompletableFuture.supplyAsync(() -> "Oops! Something went wrong, please try again after sometime!");
  }
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<OrderResponse> getAllOrders(){
    return orderService.getAllOrders();
  }

}
