package com.sudarshan.orderservice;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sudarshan.orderservice.dto.OrderLineItemsDto;
import com.sudarshan.orderservice.dto.OrderRequest;
import com.sudarshan.orderservice.helper.WebClientHelper;
import com.sudarshan.orderservice.model.OrderLineItems;
import com.sudarshan.orderservice.repository.OrderRepository;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
@AutoConfigureMockMvc
class OrderServiceApplicationTests {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private OrderRepository orderRepository;
  @Mock
  WebClientHelper webClientHelper;

  @Test
  void shouldPlaceOrderSuccessfully() throws Exception {
    OrderRequest orderRequest = getOrderRequest();
    String orderRequestString = objectMapper.writeValueAsString(orderRequest);
    Mockito.when(webClientHelper.checkIsInStock(Mockito.anyList())).thenReturn(true);
//    MockWebServer server = new MockWebServer();
//    server.start();
//
//    server.url("http://inventory-service:8686/api/v1/inventory/isinstock");
//    server.enqueue(new MockResponse().setBody(String.valueOf(true)).addHeader("Content-Type", "application/json"));
  //  HttpUrl baseUrl = server.url("http://inventory-service:8686/api/v1/inventory/isinstock");
    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/order")
        .contentType(MediaType.APPLICATION_JSON)
        .content(orderRequestString))
        .andExpect(status().isCreated());
    //Assertions.assertEquals(1, orderRepository.findAll().size());
    //server.shutdown();
  }
  @Test
  @Sql("create-order.sql")
  void shouldGetAllOrders() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/order"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(1)));
  }

  private OrderRequest getOrderRequest() {
    OrderLineItemsDto orderLineItemsDto = OrderLineItemsDto.builder()
        .id(1L)
        .skuCode("pixel7")
        .price(BigDecimal.valueOf(2000.0))
        .quantity(1)
        .build();
    List<OrderLineItemsDto> orderLineItemsDtoList = new ArrayList<>();
    orderLineItemsDtoList.add(orderLineItemsDto);

    return OrderRequest.builder()
        .orderLineItemsDtoList(orderLineItemsDtoList)
        .build();
  }
}
