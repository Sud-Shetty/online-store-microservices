package com.sudarshan.inventoryservice.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sudarshan.inventoryservice.OrderLineItems;
import com.sudarshan.inventoryservice.dto.InventoryResponse;
import com.sudarshan.inventoryservice.model.Inventory;
import com.sudarshan.inventoryservice.repository.InventoryRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
  private final InventoryRepository inventoryRepository;

  @Transactional(readOnly = true)
  //@SneakyThrows
  public boolean isInStock(List<OrderLineItems> orderLineItemsList){
//    log.info("Wait started");
//    Thread.sleep(10000);
//    log.info("Wait ended");
    boolean inStock = true;

    for(OrderLineItems orderLineItems : orderLineItemsList){
      Inventory inventory = inventoryRepository.findBySkuCode(orderLineItems.getSkuCode());
      if(inventory == null || inventory.getQuantity() < orderLineItems.getQuantity()){
        inStock = false;
        break;
      }
    }
    log.info("Received request for isInStock check and responded {}", inStock);
    return inStock;
  }

  public List<InventoryResponse> getAllInventoryItems() {
    List<Inventory> inventoryList = inventoryRepository.findAll();
    return inventoryList.stream().map(this::mapToDto).toList();
  }

  private InventoryResponse mapToDto(Inventory inventory) {
    return InventoryResponse.builder()
        .skuCode(inventory.getSkuCode())
        .quantity(inventory.getQuantity())
        .build();
  }
}
