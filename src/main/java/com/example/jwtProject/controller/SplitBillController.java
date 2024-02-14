package com.example.jwtProject.controller;


import com.example.jwtProject.service.BillService;
import com.example.jwtProject.service.SplitBillService;
import com.example.jwtProject.service.TotalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bill")
public class SplitBillController {

    @Autowired
    private SplitBillService splitBillService;
    @Autowired
    private TotalService totalService;

    @GetMapping("/getByIdTrans")
    public ResponseEntity<List<Map<String, Object>>> getBillDetailsByIdTransList(@RequestParam List<Long> idTransList) {
        List<Map<String, Object>> billDetails = splitBillService.getBillDetailsByIdTransList(idTransList);
        return new ResponseEntity<>(billDetails, HttpStatus.OK);
    }

    //untuk pas cekstatus
    @GetMapping("/getDetailsWithUserAndItem")
    public ResponseEntity<List<Map<String, Object>>> getBillDetailsWithUserAndItem() {
        List<Map<String, Object>> billDetails = splitBillService.getBillDetailsWithUserAndItem();
        return new ResponseEntity<>(billDetails, HttpStatus.OK);
    }
    //untuk pas liat detail ada nama itemnya
    @GetMapping("/getDetailItemWithUserAndItem")
    public ResponseEntity<List<Map<String, Object>>> getDetailItemWithUserAndItem() {
        List<Map<String, Object>> detailItems = splitBillService.getDetailItemWithUserAndItem();
        return new ResponseEntity<>(detailItems, HttpStatus.OK);
    }

    @GetMapping("/getAllBillInfoWithNullRiwayatBill")
    public List<Map<String, Object>> getAllBillInfoWithNullRiwayatBill() {
        List<Object[]> billInfoList = splitBillService.getAllBillInfoWithNullRiwayatBill();

        return billInfoList.stream()
                .map(billInfo -> {
                    Map<String, Object> map = Map.of(
                            "id_trans", billInfo[0],
                            "grand_total", billInfo[1]
                    );
                    return map;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/getIdRiwayatBillDetails")
    public ResponseEntity<List<Map<String, Object>>> getIdRiwayatBillDetails() {
        List<Map<String, Object>> idRiwayatBillDetails = splitBillService.getIdRiwayatBillDetails();
        return new ResponseEntity<>(idRiwayatBillDetails, HttpStatus.OK);
    }
}
