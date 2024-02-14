package com.example.jwtProject.controller;

import com.example.jwtProject.model.Bill;
import com.example.jwtProject.model.RiwayatBill;
import com.example.jwtProject.model.SplitBill;
import com.example.jwtProject.service.RiwayatBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/riwayatbill")
public class RiwayatBillController {

    private final RiwayatBillService riwayatBillService;

    @Autowired
    public RiwayatBillController(RiwayatBillService riwayatBillService) {
        this.riwayatBillService = riwayatBillService;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllRiwayatBills() {
        List<RiwayatBill> riwayatBills = riwayatBillService.getAllRiwayatBills();
        List<Map<String, Object>> transformedResponse = riwayatBillService.transformRiwayatBills(riwayatBills);
        return new ResponseEntity<>(transformedResponse, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<RiwayatBill> getRiwayatBillById(@PathVariable Long id) {
        RiwayatBill riwayatBill = riwayatBillService.getRiwayatBillById(id)
                .orElse(null);

        if (riwayatBill != null) {
            return new ResponseEntity<>(riwayatBill, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRiwayatBill(@PathVariable Long id) {
        riwayatBillService.deleteRiwayatBill(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/create")
    public ResponseEntity<RiwayatBill> createRiwayatBill(@RequestBody List<SplitBill> bills) {
        RiwayatBill riwayatBill = riwayatBillService.createRiwayatBill(bills);
        return new ResponseEntity<>(riwayatBill, HttpStatus.CREATED);
    }

}
