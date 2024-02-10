package com.example.jwtProject.controller;


import com.example.jwtProject.model.Bill;
import com.example.jwtProject.service.BillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final BillService billService;

    private static final Logger logger = LoggerFactory.getLogger(BillController.class);

    @Autowired
    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping
    public ResponseEntity<List<Bill>> getAllBills() {
        List<Bill> bills = billService.getAllBills();
        return new ResponseEntity<>(bills, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Bill>> getAllBillsForUser(@PathVariable Long userId) {
        List<Bill> bills = billService.getAllBillsForUser(userId);
        return new ResponseEntity<>(bills, HttpStatus.OK);
    }

    @GetMapping("/paid-bills/{userId}")
    public ResponseEntity<List<Bill>> getAllPaidBillsForUser(@PathVariable Long userId) {
        List<Bill> paidBills = billService.getAllPaidBillsForUser(userId);
        return new ResponseEntity<>(paidBills, HttpStatus.OK);
    }

    @GetMapping("/unpaid-bills/{userId}")
    public ResponseEntity<List<Bill>> getAllUnpaidBillsForUser(@PathVariable Long userId) {
        List<Bill> unpaidBills = billService.getAllUnpaidBillsForUser(userId);
        return new ResponseEntity<>(unpaidBills, HttpStatus.OK);
    }

    @GetMapping("/latest-unpaid-bills/{userId}")
    public ResponseEntity<List<Bill>> getLatestUnpaidBillsForUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Bill> latestUnpaidBills = billService.getLatestUnpaidBillsForUser(userId, page, size);
        return new ResponseEntity<>(latestUnpaidBills, HttpStatus.OK);
    }

    @GetMapping("/latest-unpaid-bills/{userId}/single")
    public ResponseEntity<Bill> getLatestUnpaidSingleBillForUser(@PathVariable Long userId) {
        Optional<Bill> latestUnpaidBill = billService.getLatestUnpaidBillForUser(userId);
        return latestUnpaidBill.map(bill -> new ResponseEntity<>(bill, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/latest-paid-bills/{userId}")
    public ResponseEntity<List<Bill>> getLatestPaidBillsForUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Bill> latestPaidBills = billService.getLatestPaidBillsForUser(userId, page, size);
        return new ResponseEntity<>(latestPaidBills, HttpStatus.OK);
    }

    @GetMapping("/latest-paid-bills/{userId}/single")
    public ResponseEntity<Bill> getLatestPaidSingleBillForUser(@PathVariable Long userId) {
        Optional<Bill> latestPaidBill = billService.getLatestPaidBillForUser(userId);
        return latestPaidBill.map(bill -> new ResponseEntity<>(bill, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Bill> addBill(
            @PathVariable Long userId,
            @RequestBody String idpel) {
        try {
            logger.info("Received request to add bill for user with ID {}", userId);
            logger.info("IDPEL: {}", idpel);

            // Tambahkan tagihan baru dengan validasi idpel
            Bill addedBill = billService.addBillWithIdpelValidation(userId, idpel);
            logger.info("Bill added successfully with ID {}", addedBill.getId());
            return new ResponseEntity<>(addedBill, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to add bill: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("An error occurred while adding bill: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Endpoint untuk memperbarui status pembayaran tagihan
    @PutMapping("/{billId}/update-payment-status")
    public ResponseEntity<String> updatePaymentStatus(@PathVariable Long billId,
                                                      @RequestBody String transactionCode) {
        try {
            // Temukan userId berdasarkan billId
            Long userId = billService.getUserIdByBillId(billId);

            // Perbarui status pembayaran tagihan dengan validasi transaksi
            billService.updatePaymentStatusWithTransactionValidation(userId, billId, transactionCode);

            return ResponseEntity.ok("Payment status updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

//
//    @Autowired
//    private BillService billService;
//    @Autowired
//    private TotalService totalService;

//   @GetMapping("/{id_trans}")
//    public ResponseEntity<List<Total>> getBillDetails(@PathVariable Long id_trans) {
//        List<Total> billDetails = totalService.getTotalsByBillId(id_trans);
//        if (billDetails.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(billDetails);
//    }

//    @GetMapping("/{id_trans}/totals")
//    public ResponseEntity<List<Total>> getTotalsByBillId(@PathVariable Long id_trans) {
//        List<Total> totals = billService.getTotalsByBillId(id_trans);
//
//        if (!totals.isEmpty()) {
//            return new ResponseEntity<>(totals, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
}