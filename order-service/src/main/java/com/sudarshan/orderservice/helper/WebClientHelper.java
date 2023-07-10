package com.sudarshan.orderservice.helper;

import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.sudarshan.orderservice.model.OrderLineItems;
@AllArgsConstructor
@Component
public class WebClientHelper {
  private final WebClient.Builder webClientBuilder;
  public boolean checkIsInStock(List<OrderLineItems> orderLineItemsList) {
    return Boolean.TRUE.equals(webClientBuilder.build().post()
        .uri("http://inventory-service:8686/api/v1/inventory/isinstock")
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(orderLineItemsList))
        .retrieve()
        .bodyToMono(Boolean.class)
        .block());
  }

}
