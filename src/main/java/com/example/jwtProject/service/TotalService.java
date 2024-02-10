package com.example.jwtProject.service;



import com.example.jwtProject.model.SplitBill;
import com.example.jwtProject.model.Item;
import com.example.jwtProject.model.Total;
import com.example.jwtProject.repository.BillRepository;
import com.example.jwtProject.repository.ItemRepository;
import com.example.jwtProject.repository.SplitBillRepository;
import com.example.jwtProject.repository.TotalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TotalService {
    //ini kepake total
    @Autowired
    private TotalRepository totalRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private SplitBillRepository splitBillRepository;
    public List<Total> getAllTotal() {
        return totalRepository.findAll();
    }

    public Total getTotalById(Long id_total) {
        return totalRepository.findById(id_total).orElse(null);
    }
    public List<Object[]> findDetailsByTransId(Long idTrans) {
        return totalRepository.findDetailsByTransId(idTrans);
    }
    public List<Object[]> findTotalDetailsWithSum(Long transId) {
        return totalRepository.findTotalDetailsWithSum(transId);
    }
    //ini kepake post total ke tabel total dan tabel bill
    public List<Total> calculateAndSaveTotals(List<Long> idItems, Long id_trans) {
        // Contoh: Mendapatkan idTrans secara otomatis (bisa disesuaikan dengan logika bisnis Anda)

        List<Total> totals = new ArrayList<>();
        double grand_total= 0.0;

        for (Long idItem : idItems) {
            Item item = itemRepository.findById(idItem).orElse(null);

            if (item != null) {
                double totalPrice = item.getPrice() * item.getQuantity();

                Total total = new Total();
                total.setItem(item);
                total.setTotal(totalPrice);

                grand_total += totalPrice;

                // Simpan total ke repository
                totalRepository.save(total);

                // Tambahkan total ke daftar
                totals.add(total);
            }
        }


        SplitBill splitBill = new SplitBill();
        splitBill.setId_trans(id_trans);
        splitBill.setGrand_total(grand_total);

        // Save Bill object
        splitBillRepository.save(splitBill);

        // Set bill in Total objects and save
        totals.forEach(total -> total.setBill(splitBill));
        totalRepository.saveAll(totals);

        return totals;
    }

    private Long generateTransId() {
        // Contoh: Mendapatkan idTrans secara otomatis (gunakan logika bisnis Anda)
        // Misalnya, bisa menggunakan timestamp atau generator unik lainnya
        return System.currentTimeMillis();
    }
}




