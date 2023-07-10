package com.sudarshan.inventoryservice;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class OrderLineItems {
  private Long id;
  private String skuCode;
  private BigDecimal price;
  private Integer quantity;
}
