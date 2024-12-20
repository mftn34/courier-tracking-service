package com.furkantufan.courier.tracking.repository;

import com.furkantufan.courier.tracking.entity.StoreEntrance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface StoreEntranceRepository extends JpaRepository<StoreEntrance, Long> {

    @Query("""
            select se.entranceTime
            from StoreEntrance se
            where se.courier.id = :courierId
            order by se.entranceTime desc
            """)
    List<Instant> getLastEntranceTime(@Param("courierId") Long courierId);

    @Query("""
                select sum(se.totalDistance)
                from StoreEntrance se
                where se.courier.id = :courierId
            """)
    Optional<Double> getTotalTravelDistanceByCourierId(@Param("courierId") Long courierId);
}