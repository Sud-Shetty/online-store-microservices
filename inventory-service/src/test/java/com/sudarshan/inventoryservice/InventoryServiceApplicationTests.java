package com.sudarshan.inventoryservice;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
@AutoConfigureMockMvc
class InventoryServiceApplicationTests {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
//  @Test
//  @Sql("create-inventory.sql")
//  void shouldReturnIsInStockTrue() throws Exception {
//    List<OrderLineItems> orderLineItemsList = getOrderLineItemsListForInStock();
//    String inventoryRequestString = objectMapper.writeValueAsString(orderLineItemsList);
//    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/inventory/isinstock")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(inventoryRequestString))
//        .andExpect(status().isOk())
//        .andExpect(content().string("true"));
//  }

  private List<OrderLineItems> getOrderLineItemsListForInStock() {
    return Collections.singletonList(OrderLineItems.builder()
        .id(1L)
        .skuCode("pixel7")
        .price(BigDecimal.valueOf(1200.00))
        .quantity(2)
        .build());
  }
}
