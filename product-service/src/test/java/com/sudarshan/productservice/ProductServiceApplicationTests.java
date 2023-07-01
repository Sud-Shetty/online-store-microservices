package com.sudarshan.productservice;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sudarshan.productservice.dto.ProductRequest;
import com.sudarshan.productservice.repository.ProductRepository;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private ProductRepository productRepository;

  @Test
  void shouldCreateProduct() throws Exception {
    ProductRequest productRequest = getProductRequest();
    String productRequestString = objectMapper.writeValueAsString(productRequest);
    mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product")
        .contentType(MediaType.APPLICATION_JSON)
        .content(productRequestString))
        .andExpect(status().isCreated());
    Assertions.assertEquals(1, productRepository.findAll().size());
  }

  @Test
  void shouldGetAllProducts() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/product"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(1)));
  }

  private ProductRequest getProductRequest() {
    return ProductRequest.builder()
        .name("Pixel 7")
        .description("Google Pixel 7")
        .price(BigDecimal.valueOf(80000.00))
        .build();
  }

}
