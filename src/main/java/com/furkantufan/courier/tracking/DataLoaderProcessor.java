package com.furkantufan.courier.tracking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.furkantufan.courier.tracking.entity.Courier;
import com.furkantufan.courier.tracking.entity.Store;
import com.furkantufan.courier.tracking.repository.CourierRepository;
import com.furkantufan.courier.tracking.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataLoaderProcessor implements CommandLineRunner {

    private static final String STORE_PATH = "stores.json";

    private static final String COURIER_PATH = "couriers.json";

    private final StoreRepository storeRepository;

    private final CourierRepository courierRepository;

    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        loadData(STORE_PATH, storeRepository, Store.class);
        loadData(COURIER_PATH, courierRepository, Courier.class);
    }

    private <T> void loadData(String filePath, JpaRepository<T, Long> repository, Class<T> clazz) {
        if (repository.count() == 0) {
            try (var inputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
                if (inputStream == null) {
                    log.error("{} not found in the resources directory!", filePath);
                    return;
                }

                var type = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
                List<T> data = objectMapper.readValue(inputStream, type);
                repository.saveAll(data);
                log.info("Successfully loaded {} {} into the database.", data.size(), clazz.getSimpleName());
            } catch (Exception e) {
                log.error("Error occurred while loading initial data from {}: {}", filePath, e.getMessage(), e);
            }
        } else {
            log.info("Initial data for {} already exists in the database. Skipping load.", clazz.getSimpleName());
        }
    }
}