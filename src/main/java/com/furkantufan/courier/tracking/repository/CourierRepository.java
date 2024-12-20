package com.furkantufan.courier.tracking.repository;

import com.furkantufan.courier.tracking.data.dto.CourierLocationDto;
import com.furkantufan.courier.tracking.entity.Courier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Long> {

    Optional<Courier> findByPhoneNumber(String phoneNumber);

    @Query("""
                select new com.furkantufan.courier.tracking.data.dto.CourierLocationDto(cl.latitude,cl.longitude, cl.time)
                from CourierLocation cl
                where cl.courier.id = :courierId and cl.time > :lastEntranceTime
                order by cl.time
            """)
    Page<CourierLocationDto> getCourierLocationsGreaterThanByTime(@Param("courierId") Long courierId,
                                                                  @Param("lastEntranceTime") Instant lastEntranceTime,
                                                                  Pageable pageable);
}
