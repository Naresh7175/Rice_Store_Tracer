package com.ricestore.controller;

import com.ricestore.dto.SaleRequest;
import com.ricestore.model.Sale;
import com.ricestore.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin(
    origins = "*",
    allowedHeaders = "*",
    methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS }
)
public class SalesController {

    @Autowired
    private SalesService salesService;

    @PostMapping
    public Sale createSale(@RequestBody SaleRequest request) {
        return salesService.createSale(request);
    }
}
