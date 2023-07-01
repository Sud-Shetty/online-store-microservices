package com.sudarshan.inventoryservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.sudarshan.inventoryservice.model.Inventory;
import com.sudarshan.inventoryservice.repository.InventoryRepository;

@SpringBootApplication
public class InventoryServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(InventoryServiceApplication.class, args);
  }
  @Bean
  public CommandLineRunner loadData(InventoryRepository inventoryRepository){
    return args -> {
      Inventory inventory = new Inventory();
      inventory.setSkuCode("pixel7");
      inventory.setQuantity(0);

      Inventory inventory1 = new Inventory();
      inventory1.setSkuCode("iphone14");
      inventory1.setQuantity(2);

      inventoryRepository.save(inventory);
      inventoryRepository.save(inventory1);
    };
  }
}
