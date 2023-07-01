package com.sudarshan.inventoryservice.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sudarshan.inventoryservice.dto.InventoryResponse;
import com.sudarshan.inventoryservice.service.InventoryService;

@RestController
@RequestMapping("api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {
  private final InventoryService inventoryService;
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode){
    return inventoryService.isInStock(skuCode);
  }
}
