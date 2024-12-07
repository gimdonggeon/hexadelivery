package com.kdg.hexa_delivery.domain.point.repository;

import com.kdg.hexa_delivery.domain.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {

    @Query("SELECT SUM(p.pointPresentAmount) FROM Point p WHERE p.user.id = :userId AND p.expirationTime > :time AND p.status = 'NORMAL'")
    Integer findByUserIdToPointAmount(@Param("userId") Long userId, @Param("time")LocalDateTime time);

    @Query("SELECT p FROM Point p WHERE p.expirationTime > :time AND p.status = 'NORMAL'")
    List<Point> findAllOrderByExpirationTime(@Param("time")LocalDateTime time);
}
