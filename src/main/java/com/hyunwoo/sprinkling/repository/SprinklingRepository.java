package com.hyunwoo.sprinkling.repository;

import com.hyunwoo.sprinkling.entity.Sprinkling;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SprinklingRepository extends JpaRepository<Sprinkling, Long> {
    Optional<Sprinkling> findByTokenAndRoomIdAndCreatedTimeAfter(String token, String roomId, LocalDateTime limitTime);
    Optional<Sprinkling> findByTokenAndRoomIdAndOwnerIdAndCreatedTimeAfter(String token, String roomId, Long ownerId, LocalDateTime limitTime);
    int countByTokenAndOwnerIdAndRoomIdAndCreatedTimeAfter(String token, Long ownerId, String roomId, LocalDateTime limitTime);
}
