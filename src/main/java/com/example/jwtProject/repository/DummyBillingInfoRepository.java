package com.example.jwtProject.repository;

import com.example.jwtProject.model.DummyBillingInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DummyBillingInfoRepository extends JpaRepository<DummyBillingInfo, Long> {
    Optional<DummyBillingInfo> findByIdpel(String idpel);
    Optional<DummyBillingInfo> findByIdpelAndUser_Id(String idpel, Long userId);
}
