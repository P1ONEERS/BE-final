package com.example.jwtProject.repository;

import com.example.jwtProject.model.SplitBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SplitBillRepository extends JpaRepository<SplitBill, Long> {


//    @Query("SELECT t.id_total, t.id_item, t.total, b.id_trans FROM Bill b JOIN Total t ON t.id_trans = b.id_trans")
//    List<Object[]> findTotalDetailsInBill();

    // Metode lainnya
}
