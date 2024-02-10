package com.example.jwtProject.repository;

import com.example.jwtProject.model.PredictedReminderDate;
import com.example.jwtProject.model.Whatsapp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WhatsappRepository extends JpaRepository<Whatsapp, Long> {

    Logger logger = LoggerFactory.getLogger(WhatsappRepository.class);

    @Query(value = "SELECT prd FROM PredictedReminderDate prd " +
            "WHERE (prd.dueDate <= :currentDate OR prd.dueDate IS NULL)")
    List<PredictedReminderDate> findPredictedReminderDatesToProcess(@Param("currentDate") LocalDateTime currentDate);


    List<Whatsapp> findByBillTypeAndSentIsFalse(String billType);

    void deleteByPredictedReminderDate_User_Id(Long userId);
}
