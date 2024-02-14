package com.example.jwtProject.repository;

import com.example.jwtProject.model.RiwayatBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiwayatBillRepository extends JpaRepository<RiwayatBill, Long> {



    // Tambahan query khusus jika diperlukan

}
