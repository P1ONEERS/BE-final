package com.example.jwtProject.service;

import com.example.jwtProject.model.SplitBill;
import com.example.jwtProject.repository.SplitBillRepository;
import com.example.jwtProject.repository.TotalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
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



    public List<Map<String, Object>> getBillDetailsByIdTransList(List<Long> idTransList) {
        List<Object[]> originalResponse = splitBillRepository.findBillDetailsByIdTransList(idTransList);
        return transformResponse(originalResponse);
    }

    private List<Map<String, Object>> transformResponse(List<Object[]> originalResponse) {
        List<Map<String, Object>> transformedResponse = new ArrayList<>();

        for (Object[] row : originalResponse) {
            Map<String, Object> transformedRow = new HashMap<>();
            transformedRow.put("id_trans", row[0].toString());
            transformedRow.put("grandTotal", row[1]);
            transformedResponse.add(transformedRow);
        }

        return transformedResponse;
    }

    public List<Map<String, Object>> getBillDetailsWithUserAndItem() {
        List<Object[]> originalResponse = splitBillRepository.findBillDetailsWithUserAndItem();
        return transformResponseUser(originalResponse);
    }
    private List<Map<String, Object>> transformResponseUser(List<Object[]> originalResponse) {
        List<Map<String, Object>> transformedResponseUser = new ArrayList<>();

        for (Object[] row : originalResponse) {
            Map<String, Object> transformedRow = new HashMap<>();
            transformedRow.put("id_trans", row[0].toString());
            transformedRow.put("name", row[1]);
            transformedRow.put("grandTotal", row[2]);
            transformedResponseUser.add(transformedRow);
        }

        return transformedResponseUser;
    }
    public List<Map<String, Object>> getDetailItemWithUserAndItem() {
        List<Object[]> originalResponse = splitBillRepository.findDetailItemWithUserAndItem();
        return transformResponseitem(originalResponse);
    }

    private List<Map<String, Object>> transformResponseitem(List<Object[]> originalResponse) {
        List<Map<String, Object>> transformedResponseitem = new ArrayList<>();

        for (Object[] row : originalResponse) {
            Map<String, Object> transformedRow = new HashMap<>();
            transformedRow.put("id_trans",(Long) row[0]);
            transformedRow.put("id_user", row[1]);
            transformedRow.put("user_name", row[2]);
            transformedRow.put("item_name", row[3]);
            transformedRow.put("grandTotal", row[4]);
            transformedResponseitem.add(transformedRow);
        }

        return transformedResponseitem;
    }

    public List<Object[]> getAllBillInfoWithNullRiwayatBill() {
        List<SplitBill> bills = splitBillRepository.findAll();
        return bills.stream()
                .filter(splitBill -> splitBill.getRiwayatBill() == null)
                .map(splitBill -> new Object[]{splitBill.getId_trans(), splitBill.getGrand_total()})
                .collect(Collectors.toList());
    }
    public List<Map<String, Object>> getIdRiwayatBillDetails() {
        List<Object[]> originalResponse = splitBillRepository.findIdRiwayatBillDetails();
        return transformResponseIdRiwayatBill(originalResponse);
    }

    private List<Map<String, Object>> transformResponseIdRiwayatBill(List<Object[]> originalResponse) {
        List<Map<String, Object>> transformedResponseIdRiwayatBill = new ArrayList<>();

        for (Object[] row : originalResponse) {
            Map<String, Object> transformedRow = new HashMap<>();
            transformedRow.put("id_riwayatbill", row[0]);
            transformedRow.put("id_trans", row[1]);
            transformedRow.put("id_user", row[2]);
            transformedRow.put("name", row[3]);
            transformedRow.put("grandTotal", row[4]);
            transformedResponseIdRiwayatBill.add(transformedRow);
        }

        return transformedResponseIdRiwayatBill;
    }
}
