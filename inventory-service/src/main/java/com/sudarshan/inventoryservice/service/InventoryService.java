package com.sudarshan.inventoryservice.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sudarshan.inventoryservice.dto.InventoryResponse;
import com.sudarshan.inventoryservice.repository.InventoryRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
  private final InventoryRepository inventoryRepository;

  //TODO: Check the quantity as well
  @Transactional(readOnly = true)
  //@SneakyThrows
  public List<InventoryResponse> isInStock(List<String> skuCodes){
//    log.info("Wait started");
//    Thread.sleep(10000);
//    log.info("Wait ended");
    return inventoryRepository.findBySkuCodeIn(skuCodes)
        .stream()
        .map(inventory -> InventoryResponse.builder()
            .skuCode(inventory.getSkuCode())
            .isInStock(inventory.getQuantity() > 0)
            .build())
        .toList();
  }

}
