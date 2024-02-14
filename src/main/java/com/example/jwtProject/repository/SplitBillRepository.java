package com.example.jwtProject.repository;

import com.example.jwtProject.model.SplitBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SplitBillRepository extends JpaRepository<SplitBill, Long> {


    @Query(value = "SELECT b.id_trans, b.grand_total FROM bill_split b WHERE b.id_trans IN :idTransList", nativeQuery = true)
    List<Object[]> findBillDetailsByIdTransList(@Param("idTransList") List<Long> idTransList);


    @Query(value = "SELECT DISTINCT b.id_trans, u.id_teman as id_user, u.name_teman as user_name, i.name as item_name, b.grand_total " +
            "FROM bill_split b " +
            "JOIN total t ON t.id_trans = b.id_trans " +
            "JOIN item i ON i.id_item = t.id_item " +
            "JOIN teman u ON u.id_teman = i.id_teman", nativeQuery = true)
    List<Object[]> findDetailItemWithUserAndItem();

    @Query(value = "SELECT DISTINCT b.id_trans, u.name_teman, b.grand_total " +
            "FROM bill_split b " +
            "JOIN total t ON t.id_trans = b.id_trans " +
            "JOIN item i ON i.id_item = t.id_item " +
            "JOIN teman u ON u.id_teman = i.id_teman", nativeQuery = true)
    List<Object[]> findBillDetailsWithUserAndItem();

    @Query(value = "SELECT DISTINCT r.id_riwayatbill, b.id_trans, u.id_teman, u.name_teman, b.grand_total " +
            "FROM riwayatbill r " +
            "JOIN bill_split b ON b.id_riwayatbill = r.id_riwayatbill " +
            "JOIN total t ON t.id_trans = b.id_trans " +
            "JOIN item i ON i.id_item = t.id_item " +
            "JOIN teman u ON u.id_teman = i.id_teman", nativeQuery = true)
    List<Object[]> findIdRiwayatBillDetails();
}
