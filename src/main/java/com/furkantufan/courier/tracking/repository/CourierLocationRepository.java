package com.furkantufan.courier.tracking.repository;

import com.furkantufan.courier.tracking.entity.CourierLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourierLocationRepository extends JpaRepository<CourierLocation, Long> {

}
