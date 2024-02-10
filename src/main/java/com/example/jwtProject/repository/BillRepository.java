package com.example.jwtProject.repository;


import com.example.jwtProject.model.Bill;
import com.example.jwtProject.model.SplitBill;
import com.example.jwtProject.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    boolean existsByUser_Id(Long userId);

    Optional<Bill> findFirstByUserAndIsPaidOrderByDueDateDesc(User user, boolean isPaid);


    List<Bill> findAllByUser_Id(Long userId);

    @Query("SELECT b.user.id FROM Bill b WHERE b.id = :billId")
    Long findUserIdByBillId(@Param("billId") Long billId);

    Optional<Bill> findByIdAndUser_IdAndIsPaid(Long billId, Long userId, boolean isPaid);

    @Query("SELECT b FROM Bill b WHERE b.user.id = :userId AND b.isPaid = true")
    List<Bill> findAllPaidBillsForUser(@Param("userId") Long userId);

    @Query("SELECT b FROM Bill b WHERE b.user.id = :userId AND b.isPaid = false")
    List<Bill> findAllUnpaidBillsForUser(@Param("userId") Long userId);

    @Query("SELECT b FROM Bill b WHERE b.user.id = :userId AND b.isPaid = false ORDER BY b.transactionCompletionTime DESC")
    Page<Bill> findLatestUnpaidBillsForUser(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT b FROM Bill b WHERE b.user.id = :userId AND b.isPaid = true ORDER BY b.transactionCompletionTime DESC")
    Page<Bill> findLatestPaidBillsForUser(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT b FROM Bill b " +
            "WHERE b.user.id = :userId AND b.isPaid = true " +
            "AND b.dueDate = (SELECT MAX(b2.dueDate) FROM Bill b2 WHERE b2.user.id = :userId AND b2.isPaid = true) " +
            "AND b.transactionCompletionTime = (SELECT MAX(b3.transactionCompletionTime) FROM Bill b3 WHERE b3.user.id = :userId AND b3.isPaid = true)")
    Optional<Bill> findLatestPaidBillForUser(@Param("userId") Long userId);

    @Query("SELECT b FROM Bill b " +
            "WHERE b.user.id = :userId AND b.isPaid = false " +
            "AND b.dueDate = (SELECT MAX(b2.dueDate) FROM Bill b2 WHERE b2.user.id = :userId AND b2.isPaid = false) " +
            "AND b.transactionCompletionTime = (SELECT MAX(b3.transactionCompletionTime) FROM Bill b3 WHERE b3.user.id = :userId AND b3.isPaid = false)")
    Optional<Bill> findLatestUnpaidBillForUser(@Param("userId") Long userId);


    Optional<Bill> findByReferenceNumber(String referenceNumber);

    List<Bill> findByIsPaid(boolean isPaid);

    List<Bill> findByUserIdAndIsPaid(Long userId, boolean isPaid);

    @Modifying
    @Query("UPDATE Bill b SET b.transactionCompletionTime = :completionTime WHERE b.id = :billId")
    void updateTransactionCompletionTime(@Param("billId") Long billId, @Param("completionTime") LocalDateTime completionTime);


//    @Query("SELECT t.id_total, t.id_item, t.total, b.id_trans FROM Bill b JOIN Total t ON t.id_trans = b.id_trans")
//    List<Object[]> findTotalDetailsInBill();

    // Metode lainnya
}


