package com.sudarshan.orderservice.dto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.sudarshan.orderservice.model.OrderLineItems;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
  private Long id;
  private String orderNumber;
  private List<OrderLineItemsDto> orderLineItemsDtoList;

}
