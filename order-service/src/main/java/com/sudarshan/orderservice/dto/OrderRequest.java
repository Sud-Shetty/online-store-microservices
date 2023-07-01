package com.sudarshan.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.sudarshan.orderservice.model.OrderLineItems;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
  private List<OrderLineItemsDto> orderLineItemsDtoList;
}
