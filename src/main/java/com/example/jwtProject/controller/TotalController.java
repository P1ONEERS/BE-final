package com.example.jwtProject.controller;

import com.example.jwtProject.model.Total;
import com.example.jwtProject.service.TotalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/total")
public class TotalController {

    @Autowired
    private TotalService totalService;

    @GetMapping
    public ResponseEntity<List<Total>> getAllTotal() {
        List<Total> totals = totalService.getAllTotal();
        if (!totals.isEmpty()) {
            return new ResponseEntity<>(totals, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id_total}")
    public ResponseEntity<Total> getTotalById(@PathVariable Long id_total) {
        Total total = totalService.getTotalById(id_total);
        if (total != null) {
            return new ResponseEntity<>(total, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/calculateAndSaveTotals")
    public ResponseEntity<String> calculateAndSaveTotals(@RequestBody List<Long> id_item) {
        try {
            Long idTrans = generateTransId();
            totalService.calculateAndSaveTotals(id_item, idTrans);  // Menyimpan total dan id_trans
            return ResponseEntity.ok("Totals and Bills calculated and saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to calculate and save totals and bills. Error: " + e.getMessage());
        }
    }

    private Long generateTransId() {
        // Contoh: Mendapatkan idTrans secara otomatis di controller
        // Misalnya, bisa menggunakan timestamp atau generator unik lainnya
        return System.currentTimeMillis();
    }

    @GetMapping("/detailsByTransId/{idTrans}")
    public ResponseEntity<List<Object[]>> getDetailsByTransId(@PathVariable Long idTrans) {
        List<Object[]> details = totalService.findDetailsByTransId(idTrans);
        if (!details.isEmpty()) {
            return new ResponseEntity<>(details, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/detailsWithSum/{transId}")
    public ResponseEntity<List<Object[]>> findTotalDetailsWithSum(@PathVariable Long transId) {
        List<Object[]> detailsWithSum = totalService.findTotalDetailsWithSum(transId);
        if (!detailsWithSum.isEmpty()) {
            return new ResponseEntity<>(detailsWithSum, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

