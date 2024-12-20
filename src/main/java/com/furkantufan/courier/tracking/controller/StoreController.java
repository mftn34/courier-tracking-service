package com.furkantufan.courier.tracking.controller;

import com.furkantufan.courier.tracking.data.dto.StoreDto;
import com.furkantufan.courier.tracking.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1/stores", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping("/{store-id}")
    @Operation(summary = "Get store by id")
    public StoreDto getStoreById(@PathVariable("store-id") Long storeId) {
        return storeService.getStoreDtoById(storeId);
    }

    @GetMapping(value = "/all")
    @Operation(summary = "Get all store with pageable")
    public Page<StoreDto> getAllStores(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "50") int size) {
        return storeService.getAllStore(page, size);
    }
}