package com.example.jwtProject.service;

import com.example.jwtProject.model.SplitBill;
import com.example.jwtProject.repository.SplitBillRepository;
import com.example.jwtProject.repository.TotalRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class SplitBillService {
    @Autowired
    private SplitBillRepository splitBillRepository;
    @Autowired
    private TotalRepository totalRepository;
    @Autowired
    private TotalService totalService;

    public SplitBill getBillById(Long id_trans) {
        return splitBillRepository.findById(id_trans).orElse(null);
    }
}
