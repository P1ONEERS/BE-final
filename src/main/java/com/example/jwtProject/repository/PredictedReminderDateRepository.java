package com.example.jwtProject.repository;

import com.example.jwtProject.model.PredictedReminderDate;
import com.example.jwtProject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PredictedReminderDateRepository extends JpaRepository<PredictedReminderDate, Long> {



    Optional<PredictedReminderDate> findByUserAndBillType(User user, String billType);

    List<PredictedReminderDate> findAllByUser(User user);

    Optional<PredictedReminderDate> findFirstByUser_IdAndBillTypeOrderByDueDateDesc(Long userId, String billType);

    List<PredictedReminderDate> findAllByUserAndDueDateBetween(User user, LocalDateTime startDate, LocalDateTime endDate);

    Optional<PredictedReminderDate> findFirstByUserOrderByDueDateDesc(User user);

    void deleteByUser_Id(Long userId);

    // Method untuk mencari billType berdasarkan user dan dueDate
    @Query("SELECT DISTINCT prd.billType FROM PredictedReminderDate prd WHERE prd.user = :user AND prd.dueDate = :dueDate")
    Optional<String> findBillTypeByUserAndDueDate(@Param("user") User user, @Param("dueDate") LocalDateTime dueDate);
}
