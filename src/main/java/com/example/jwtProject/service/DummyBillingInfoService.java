package com.example.jwtProject.service;

import com.example.jwtProject.model.DummyBillingInfo;
import com.example.jwtProject.repository.DummyBillingInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DummyBillingInfoService {

    private final DummyBillingInfoRepository dummyBillingInfoRepository;

    @Autowired
    public DummyBillingInfoService(DummyBillingInfoRepository dummyBillingInfoRepository) {
        this.dummyBillingInfoRepository = dummyBillingInfoRepository;
    }

    public List<DummyBillingInfo> getAllDummyBillingInfo() {
        return dummyBillingInfoRepository.findAll();
    }

    public Optional<DummyBillingInfo> findDummyBillingInfoByIdpel(String idpel) {
        return dummyBillingInfoRepository.findByIdpel(idpel);
    }
}
