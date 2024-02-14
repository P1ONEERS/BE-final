package com.example.jwtProject.service;

import com.example.jwtProject.model.RiwayatBill;
import com.example.jwtProject.model.SplitBill;
import com.example.jwtProject.repository.SplitBillRepository;
import com.example.jwtProject.repository.RiwayatBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RiwayatBillService {

    private final RiwayatBillRepository riwayatBillRepository;

    @Autowired
    public RiwayatBillService(RiwayatBillRepository riwayatBillRepository) {
        this.riwayatBillRepository = riwayatBillRepository;

    }

    @Autowired
    private SplitBillRepository splitBillRepository;
    public List<RiwayatBill> getAllRiwayatBills() {
        return riwayatBillRepository.findAll();
    }

    public Optional<RiwayatBill> getRiwayatBillById(Long id) {
        return riwayatBillRepository.findById(id);
    }

    public RiwayatBill saveRiwayatBill(RiwayatBill riwayatBill) {
        return riwayatBillRepository.save(riwayatBill);
    }

    public void deleteRiwayatBill(Long id) {
        riwayatBillRepository.deleteById(id);
    }

    public RiwayatBill createRiwayatBill(List<SplitBill> bills) {
        double totalGrandTotal = 0;

        for (SplitBill splitBill : bills) {
            totalGrandTotal += splitBill.getGrand_total();
        }

        RiwayatBill riwayatBill = new RiwayatBill();
        riwayatBill.setTotalSeluruhTeman(totalGrandTotal);

        // Save RiwayatBill to generate id_riwayatbill
        riwayatBill = riwayatBillRepository.save(riwayatBill);

        // Set RiwayatBill in each Bill and save them
        Long riwayatBillId = riwayatBill.getIdRiwayatBill();
        for (SplitBill splitBill : bills) {
            splitBill.setRiwayatBill(riwayatBill);
            // Save each bill separately
            splitBillRepository.save(splitBill);
        }

        return riwayatBill;
    }

    public List<Map<String, Object>> transformRiwayatBills(List<RiwayatBill> riwayatBills) {
        List<Map<String, Object>> transformedResponse = new ArrayList<>();

        for (RiwayatBill riwayatBill : riwayatBills) {
            Map<String, Object> transformedRow = new HashMap<>();
            transformedRow.put("id_riwayatbill", riwayatBill.getIdRiwayatBill());
            transformedRow.put("total_seluruh_teman", riwayatBill.getTotalSeluruhTeman());
            transformedResponse.add(transformedRow);
        }

        return transformedResponse;
    }

}
