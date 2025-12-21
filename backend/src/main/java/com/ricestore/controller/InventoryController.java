package com.ricestore.controller;

import com.ricestore.model.Product;
import com.ricestore.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(
    origins = "*",
    allowedHeaders = "*",
    methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS }
)
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return inventoryService.addProduct(product);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return inventoryService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return inventoryService.getProduct(id);
    }

    // Upload endpoint removed as image is now part of Product model

}
