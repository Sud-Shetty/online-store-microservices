package com.sudarshan.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.sudarshan.orderservice.dto.InventoryResponse;
import com.sudarshan.orderservice.dto.OrderLineItemsDto;
import com.sudarshan.orderservice.dto.OrderRequest;
import com.sudarshan.orderservice.dto.OrderResponse;
import com.sudarshan.orderservice.event.OrderPlacedEvent;
import com.sudarshan.orderservice.model.Order;
import com.sudarshan.orderservice.model.OrderLineItems;
import com.sudarshan.orderservice.repository.OrderRepository;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {
  private final OrderRepository orderRepository;
  private final WebClient.Builder webClientBuilder;
  //private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
  private final ApplicationEventPublisher applicationEventPublisher;
  public String placeOrder(OrderRequest orderRequest){
    Order order = new Order();
    order.setOrderNumber(UUID.randomUUID().toString());
    List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList()
        .stream()
        .map(this::mapFromDto)
        .toList();
    order.setOrderLineItemsList(orderLineItemsList);
    log.info("order request received for order id {}",order.getOrderNumber());
    try {
      //Call inventory service and place order if all the products are in stock.
      boolean allProductsInStock = Boolean.TRUE.equals(webClientBuilder.build().post()
          .uri("http://inventory-service:8686/api/v1/inventory/isinstock")
          .contentType(MediaType.APPLICATION_JSON)
          .body(BodyInserters.fromValue(orderLineItemsList))
          .retrieve()
          .bodyToMono(Boolean.class)
          .block());

      log.info("received isInStock as {}", allProductsInStock);

      if (allProductsInStock) {
        orderRepository.save(order);
        applicationEventPublisher.publishEvent(new OrderPlacedEvent(this, order.getOrderNumber()));
        log.info("order placed event published");
        return "Order placed successfully!";
      } else {
        throw new IllegalArgumentException(
            "One or more items are not in stock, please check and try again!");
      }
    }catch (IllegalArgumentException iae){
      log.error("Exception is:", iae);
      return iae.getMessage();
    }catch (Exception e){
      log.error("Exception is:", e);
      return "Oops! Something went wrong.";
    }

  }

  public List<OrderResponse> getAllOrders() {
    List<Order> orders = orderRepository.findAll();
    return orders.stream().map(this::mapToOrderResponse).toList();
  }

  private OrderLineItems mapFromDto(OrderLineItemsDto orderLineItemsDto) {
    OrderLineItems orderLineItems = new OrderLineItems();
    orderLineItems.setPrice(orderLineItemsDto.getPrice());
    orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
    orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
    return orderLineItems;
  }

  private OrderResponse mapToOrderResponse(Order order) {
    return OrderResponse.builder()
        .id(order.getId())
        .orderNumber(order.getOrderNumber())
        .orderLineItemsDtoList(
            order.getOrderLineItemsList()
                .stream()
                .map(this::mapToDto)
                .toList())
        .build();
  }

  private OrderLineItemsDto mapToDto(OrderLineItems orderLineItems) {
    return OrderLineItemsDto.builder()
        .price(orderLineItems.getPrice())
        .quantity(orderLineItems.getQuantity())
        .skuCode(orderLineItems.getSkuCode())
        .id(orderLineItems.getId())
        .build();
  }
}
