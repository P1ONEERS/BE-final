package com.example.jwtProject.repository;


import com.example.jwtProject.model.Total;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TotalRepository extends JpaRepository<Total, Long> {

    @Query(value = "SELECT b.id_trans, u.name as nama_teman, i.name as nama_item, t.total, b.grand_total " +
            "FROM total t " +
            "JOIN bill b ON b.id_trans = t.id_trans " +
            "JOIN item i ON i.id_item = t.id_item " +
            "JOIN users u ON u.id_user = i.id_user " +
            "WHERE b.id_trans = ?1 " +
            "ORDER BY b.id_trans ASC", nativeQuery = true)
    List<Object[]> findDetailsByTransId(Long idTrans);

    @Query(value = "SELECT " +
            "    u.name AS nama_user, " +
            "    i.name AS nama_item, " +
            "    t.id_trans, " +
            "    t.total, " +
            "    (" +
            "        SELECT SUM(t2.total) " +
            "        FROM total t2 " +
            "        JOIN item i2 ON t2.id_item = i2.id_item " +
            "        WHERE i2.id_user = u.id_user AND t2.id_trans = t.id_trans" +
            "    ) AS total_keseluruhan " +
            "FROM " +
            "    item i " +
            "JOIN " +
            "    users u ON u.id_user = i.id_user " +
            "JOIN " +
            "    total t ON t.id_item = i.id_item " +
            "WHERE " +
            "    t.id_trans = :transId", nativeQuery = true)
    List<Object[]> findTotalDetailsWithSum(@Param("transId") Long transId);
}
