package com.example.jwtProject.service;

import com.example.jwtProject.model.Bill;
import com.example.jwtProject.model.PredictedReminderDate;
import com.example.jwtProject.model.User;
import com.example.jwtProject.repository.BillRepository;
import com.example.jwtProject.repository.PredictedReminderDateRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class PredictedReminderDateService {

    private final PredictedReminderDateRepository predictedReminderDateRepository;
    private final BillRepository billRepository;

    @Autowired
    public PredictedReminderDateService(PredictedReminderDateRepository predictedReminderDateRepository, BillRepository billRepository) {
        this.predictedReminderDateRepository = predictedReminderDateRepository;
        this.billRepository = billRepository;
    }

    public List<PredictedReminderDate> findAllByUser(User user) {
        return predictedReminderDateRepository.findAllByUser(user);
    }


    public void updateOrSavePredictedReminderDate(User user, String billType, LocalDateTime dueDate) {
        try {
            // Get the latest paid bill for the user
            Optional<Bill> latestPaidBillOptional = billRepository.findLatestPaidBillForUser(user.getId());

            if (latestPaidBillOptional.isPresent()) {
                Bill latestPaidBill = latestPaidBillOptional.get();

                // Check if there is an existing entry for the user and bill_type
                Optional<PredictedReminderDate> existingReminderDateOptional =
                        predictedReminderDateRepository.findByUserAndBillType(user, billType);

                if (existingReminderDateOptional.isPresent()) {
                    PredictedReminderDate existingReminderDate = existingReminderDateOptional.get();
                    LocalDateTime existingDate = existingReminderDate.getDueDate();

                    // Update only if the due date or bill type is different
                    if (!existingDate.equals(dueDate) || !existingReminderDate.getBillType().equals(billType)) {
                        existingReminderDate.setDueDate(dueDate);
                        existingReminderDate.setBillType(billType); // Set the latest bill type
                        updateBillingPeriod(existingReminderDate);
                        predictedReminderDateRepository.save(existingReminderDate);
                    }
                } else {
                    // Save a new entry if no existing entry found
                    PredictedReminderDate newPredictedReminderDate = new PredictedReminderDate(
                            user,
                            billType,
                            dueDate,
                            generateBillingPeriod(dueDate),
                            LocalDateTime.now()
                    );
                    predictedReminderDateRepository.save(newPredictedReminderDate);
                }
            } else {
                throw new RuntimeException("No latest paid bill found for user: " + user.getUsername());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error updating/saving PredictedReminderDate", e);
        }
    }

    @Transactional
    public void deleteDataByUserId(Long userId) {
        predictedReminderDateRepository.deleteByUser_Id(userId);
    }

    private void updateBillingPeriod(PredictedReminderDate predictedReminderDate) {
        // Format dueDate to MMM yy and set it to billingPeriod
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
        String billingPeriod = predictedReminderDate.getDueDate().format(formatter);
        predictedReminderDate.setBillingPeriod(billingPeriod);
    }

    private String generateBillingPeriod(LocalDateTime dueDate) {
        // Generate billingPeriod from dueDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
        return dueDate.format(formatter);
    }
}
