package com.sudarshan.orderservice.service;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

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
public class OrderService {
  private final OrderRepository orderRepository;
  private final WebClient.Builder webClientBuilder;
  private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
  public String placeOrder(OrderRequest orderRequest){
    Order order = new Order();
    order.setOrderNumber(UUID.randomUUID().toString());
    List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList()
        .stream()
        .map(this::mapFromDto)
        .toList();
    order.setOrderLineItemsList(orderLineItemsList);

    List<String> skuCodes = order.getOrderLineItemsList().stream()
        .map(OrderLineItems::getSkuCode)
        .toList();

    //Call inventory service and place order if all the products are in stock.
    InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
        .uri("http://inventory-service/api/v1/inventory",
                uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
        .retrieve()
        .bodyToMono(InventoryResponse[].class)
        .block();

    boolean allProductsInStock = Arrays.stream(inventoryResponses)
        .allMatch(InventoryResponse::isInStock);

    if(allProductsInStock){
      orderRepository.save(order);
      kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
      return "Order placed successfully!";
    }else{
      throw new IllegalArgumentException("Product is not in stock, please try again later");
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
