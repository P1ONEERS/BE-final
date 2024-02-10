package com.example.jwtProject.service;


import com.example.jwtProject.model.Bill;
import com.example.jwtProject.model.DummyBillingInfo;
import com.example.jwtProject.model.SplitBill;
import com.example.jwtProject.model.User;
import com.example.jwtProject.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BillService {


    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final DummyBillingInfoRepository dummyBillingInfoRepository;
    private static final Logger logger = LoggerFactory.getLogger(BillService.class);
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public BillService(
            BillRepository billRepository,
            UserRepository userRepository,
            DummyBillingInfoRepository dummyBillingInfoRepository,
            PasswordEncoder passwordEncoder) {
        this.billRepository = billRepository;
        this.userRepository = userRepository;
        this.dummyBillingInfoRepository = dummyBillingInfoRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public List<Bill> getAllBillsForUser(Long userId) {
        return billRepository.findAllByUser_Id(userId);
    }

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    public Long getUserIdByBillId(Long billId) {
        return billRepository.findUserIdByBillId(billId);
    }

    // Metode untuk mendapatkan semua tagihan yang sudah dibayar untuk pengguna tertentu
    public List<Bill> getAllPaidBillsForUser(Long userId) {
        return billRepository.findAllPaidBillsForUser(userId);
    }

    // Metode untuk mendapatkan semua tagihan yang sudah belum untuk pengguna tertentu
    public List<Bill> getAllUnpaidBillsForUser(Long userId) {
        return billRepository.findAllUnpaidBillsForUser(userId);
    }

    public List<Bill> getLatestPaidBillsForUser(Long userId, int page, int size) {
        return billRepository.findLatestPaidBillsForUser(userId, PageRequest.of(page, size)).getContent();
    }

    public Optional<Bill> getLatestPaidBillForUser(Long userId) {
        return billRepository.findLatestPaidBillForUser(userId);
    }

    public List<Bill> getLatestUnpaidBillsForUser(Long userId, int page, int size) {
        return billRepository.findLatestUnpaidBillsForUser(userId, PageRequest.of(page, size)).getContent();
    }

    public Optional<Bill> getLatestUnpaidBillForUser(Long userId) {
        return billRepository.findLatestUnpaidBillForUser(userId);
    }

    public String determineBillingPeriod(LocalDate dueDate) {
        if (dueDate == null) {
            throw new IllegalArgumentException("dueDate cannot be null");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
        return dueDate.format(formatter);
    }

    private void validateIdpel(String idpel) {
        dummyBillingInfoRepository.findByIdpel(idpel)
                .orElseThrow(() -> new IllegalArgumentException("Dummy Billing Info not found for IDPEL: " + idpel));
    }

    // Fungsi untuk menambahkan bill dengan validasi idpel
    public Bill addBillWithIdpelValidation(Long userId, String idpel) {
        try {
            logger.info("Adding bill for user with ID {}", userId);
            logger.info("Validating idpel: {}", idpel);

            // Validasi idpel
            validateIdpel(idpel);

            // Dapatkan pengguna berdasarkan userId
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found"));

            // Dapatkan informasi tagihan palsu berdasarkan idpel dan userId
            DummyBillingInfo dummyBillingInfo = dummyBillingInfoRepository.findByIdpelAndUser_Id(idpel, userId)
                    .orElseThrow(() -> new IllegalArgumentException("Dummy Billing Info not found for IDPEL: " + idpel));

            // Buat objek Bill dan inisialisasi dengan data dari informasi tagihan palsu
            Bill bill = new Bill();
            bill.setUser(user);
            bill.setDummyBillingInfo(dummyBillingInfo);

            // Set billType dari DummyBillingInfo
            bill.setBillType(dummyBillingInfo.getBillType());

            BigDecimal nominalPayment = dummyBillingInfo.getNominalPayment();
            BigDecimal adminFee = dummyBillingInfo.getAdminFee();
            BigDecimal totalAmount = nominalPayment.add(adminFee);

            bill.setNominalPayment(nominalPayment);
            bill.setAdminFee(adminFee);
            bill.setAmount(totalAmount);

            // Tentukan tanggal jatuh tempo dan periode penagihan
            bill.setDueDate(LocalDate.now()); // Sesuaikan dengan kebutuhan bisnis Anda
            String billingPeriod = determineBillingPeriod(bill.getDueDate());
            bill.setBillingPeriod(billingPeriod);

            // Format Transaction Completion Time
            String formattedTransactionCompletionTime = formatTransactionCompletionTime(LocalDateTime.now());

            // Set formatted transaction completion time to the bill
            bill.setTransactionCompletionTime(formattedTransactionCompletionTime);

            // Simpan objek Bill ke dalam basis data
            return billRepository.save(bill);
        } catch (Exception e) {
            logger.error("Error adding bill: {}", e.getMessage());
            throw e; // Re-throw exception agar dapat ditangani di controller atau bagian lainnya
        }
    }

    public void validateTransactionPassword(Long userId, String transactionCode) {
        try {
            if (userId == null || transactionCode == null || transactionCode.isEmpty()) {
                throw new IllegalArgumentException("User ID or Transaction Code is empty");
            }

            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (!passwordEncoder.matches(transactionCode, user.getTransactionCode())) {
                    throw new IllegalArgumentException("Invalid transaction code");
                }
            } else {
                throw new IllegalArgumentException("User not found with ID: " + userId);
            }
        } catch (Exception e) {
            logger.error("Error validating transaction password: {}", e.getMessage());
            throw e;
        }
    }

    public void updatePaymentStatusWithTransactionValidation(Long userId, Long billId, String transactionCode) {
        try {
            // Validasi Transaction Code
            validateTransactionPassword(userId, transactionCode);

            // Dapatkan tagihan terbaru yang belum dibayar berdasarkan billId dan userId
            Optional<Bill> billOptional = billRepository.findByIdAndUser_IdAndIsPaid(billId, userId, false);
            Bill bill = billOptional.orElseThrow(() -> new IllegalArgumentException("No unpaid bill found for user with ID " + userId + " and bill ID " + billId));

            // Update Status Pembayaran menjadi "dibayar"
            bill.setPaid(true);

            // Generate Reference Code
            String referenceCode = generateReferenceCode();
            bill.setReferenceNumber(referenceCode);

            // Format Transaction Completion Time
            String formattedTransactionCompletionTime = formatTransactionCompletionTime(LocalDateTime.now());

            // Set formatted transaction completion time to the bill
            bill.setTransactionCompletionTime(formattedTransactionCompletionTime);

            // Simpan perubahan ke dalam basis data
            billRepository.save(bill);
        } catch (Exception e) {
            logger.error("Error updating payment status: {}", e.getMessage());
            throw e; // Re-throw exception agar dapat ditangani di controller atau bagian lainnya
        }
    }

    // Fungsi untuk memformat transactionCompletionTime
    public String formatTransactionCompletionTime(LocalDateTime transactionCompletionTime) {
        if (transactionCompletionTime == null) {
            throw new IllegalArgumentException("Transaction completion time cannot be null");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss");
        return transactionCompletionTime.format(formatter);
    }

    private String generateReferenceCode() {
        // Menghasilkan UUID secara acak
        UUID uuid = UUID.randomUUID();
        // Mengonversi UUID menjadi string dan mengambil 10 karakter pertama sebagai kode referensi
        return uuid.toString().substring(0, 10);
    }

    private Long getUserIdByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
    }

}
