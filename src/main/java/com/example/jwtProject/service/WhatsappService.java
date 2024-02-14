package com.example.jwtProject.service;

import com.example.jwtProject.model.*;
import com.example.jwtProject.repository.BillRepository;
import com.example.jwtProject.repository.SplitBillRepository;
import com.example.jwtProject.repository.TotalRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.example.jwtProject.repository.ItemRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WhatsappService {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private SplitBillRepository splitBillRepository;

    @Autowired
    private TotalRepository totalRepository;

    @Value("${whatsapp.api.url}")
    private String whatsappApiUrl;

    @Value("${whatsapp.api.token}")
    private String whatsappApiToken;

    private final TemanService temanService;


//    @Transactional
//    public void sendWhatsAppMessage(Long userId) {
//        // Use UserService to retrieve user information
//        Users user = userService.getUsersByIdWithItems(userId);
//
//        if (user != null) {
//            HttpHeaders headers = new HttpHeaders();
//            System.out.println("User Object: " + user.toString());
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.setBearerAuth(whatsappApiToken);
//
//            // Calculate the total amount
//            // Mengonversi Set<Item> menjadi List<Item>
//            List<Item> itemList = new ArrayList<>(user.getItems());
//
//
//
//            // Add user's items, prices, and quantities to the JSON payload
//            // Menggunakan List<Item> itemList yang sudah diambil sebelumnya
//            List<Item> itemsToSend = itemList.stream().filter(item -> item.getFlag() == 0).collect(Collectors.toList());
//            String totalAmount = calculateTotalAmount(itemsToSend);
//            String jsonPayload = "{\n" +
//                    "    \"messaging_product\": \"whatsapp\",\n" +
//                    "    \"recipient_type\": \"individual\",\n" +
//                    "    \"to\": \"whatsapp: " + user.getNoTelp() + "\",\n" +
//                    "    \"type\": \"text\",\n" +
//                    "    \"text\": {\n" +
//                    "        \"preview_url\": true,\n" +
//                    "        \"body\": \"Halo " + user.getName() + "!\\n" + "\\n" +
//                    "Ada bill yang belum kamu bayar nih !" + "\\n" + "Kamu mendapat tagihan sebesar" + "\\n" + " -- Rp. " + totalAmount + " -- " + "\\n"
//                    + "\\n" + "Ayo bayar disini" + "\\n" + "bit.ly/gamenakal" + "\\n" + "\\n" + "Rincian:\\n";
//
//            for (Item item : itemsToSend) {
//                // Format harga dan total tanpa angka desimal .0
//                String formattedPrice = String.format("%.0f", item.getPrice());
//                String formattedTotal = String.format("%.0f", item.getQuantity() * item.getPrice());
//
//                jsonPayload += "Nama Barang: " + item.getName() + "\\n" +
//                        "Jumlah: " + item.getQuantity() + "\\n" +
//                        "Harga: Rp. " + formattedPrice + "\\n" +
//                        "Total: Rp. " + formattedTotal + "\\n" +
//                        "\\n";
//            }
//
//            // Add the total amount to the JSON payload
//            jsonPayload += "Jangan lupa bayar ya :3\\n" +
//                    "        \"\n" +
//                    "    }\n" +
//                    "}";
//
//            HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);
//
//            RestTemplate restTemplate = new RestTemplate();
//            try {
//                ResponseEntity<String> response = restTemplate.postForEntity(whatsappApiUrl, entity, String.class);
//                if (response.getStatusCode() == HttpStatus.OK) {
//                    System.out.println("WhatsApp Message Sent: " + response.getBody());
//
//                    // Update flags of items that have been sent
//                    itemsToSend.forEach(item -> {
//                        item.setFlag(1);
//                        itemRepository.save(item);
//                    });
//                } else {
//                    System.err.println("Error sending WhatsApp message. Status Code: " + response.getStatusCodeValue());
//                }
//            } catch (HttpClientErrorException.Unauthorized ex) {
//                System.err.println("Unauthorized access. Check your authorization token.");
//                ex.printStackTrace();
//            } catch (Exception e) {
//                System.err.println("Error sending WhatsApp message. " + e.getMessage());
//                e.printStackTrace();
//            }
//        } else {
//            System.err.println("User with ID " + userId + " not found.");
//        }
//    }

    @Transactional
    public void sendWhatsAppMessage(Long userId) {
        // Use UserService to retrieve user information
        Teman teman = temanService.getUsersByIdWithItems(userId);

        if (teman != null) {
            HttpHeaders headers = new HttpHeaders();
            System.out.println("User Object: " + teman.toString());
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(whatsappApiToken);

            // Calculate the total amount
            // Mengonversi Set<Item> menjadi List<Item>
            List<Item> itemList = new ArrayList<>(teman.getItems());

            // Add user's items, prices, and quantities to the JSON payload
            // Menggunakan List<Item> itemList yang sudah diambil sebelumnya
            List<Item> itemsToSend = itemList.stream().filter(item -> item.getFlag() == 0).collect(Collectors.toList());

            // Create and save Bill object
            SplitBill splitBill = new SplitBill();
            splitBill.setId_trans(generateTransId());

            // Save Bill object (initially with grand_total set to 0.0)
            splitBill = splitBillRepository.save(splitBill);

            // Save items directly without using Total object
            double grand_total = 0.0;
            for (Item item : itemsToSend) {
                double totalPrice = item.getPrice() * item.getQuantity();

                // Save Item object
                item.setFlag(1); // Update flag before saving
                itemRepository.save(item);

                // Save Total object
                Total total = new Total();
                total.setItem(item);
                total.setTotal(totalPrice);
                total.setBill(splitBill);
                grand_total += totalPrice;
                totalRepository.save(total);
            }

            // Update grand_total in Bill
            splitBill.setGrand_total(grand_total);

            // Save the updated Bill object
            splitBill = splitBillRepository.save(splitBill);

            // Build JSON payload
            String jsonPayload = buildJsonPayload(teman, itemsToSend, String.valueOf(grand_total), splitBill);

            HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);

            RestTemplate restTemplate = new RestTemplate();
            try {
                ResponseEntity<String> response = restTemplate.postForEntity(whatsappApiUrl, entity, String.class);
                if (response.getStatusCode() == HttpStatus.OK) {
                    System.out.println("WhatsApp Message Sent: " + response.getBody());
                } else {
                    System.err.println("Error sending WhatsApp message. Status Code: " + response.getStatusCodeValue());
                }
            } catch (HttpClientErrorException.Unauthorized ex) {
                System.err.println("Unauthorized access. Check your authorization token.");
                ex.printStackTrace();
            } catch (Exception e) {
                System.err.println("Error sending WhatsApp message. " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("User with ID " + userId + " not found.");
        }
    }




    private String buildJsonPayload(Teman teman, List<Item> itemsToSend, String totalAmount, SplitBill splitBill) {
        StringBuilder jsonPayload = new StringBuilder("{\n" +
                "    \"messaging_product\": \"whatsapp\",\n" +
                "    \"recipient_type\": \"individual\",\n" +
                "    \"to\": \"whatsapp: " + teman.getNoTelp() + "\",\n" +
                "    \"type\": \"text\",\n" +
                "    \"text\": {\n" +
                "        \"preview_url\": true,\n" +
                "        \"body\": \"Halo " + teman.getName() + "!\\n" + "\\n" +
                "Ada bill yang belum kamu bayar nih !" + "\\n" + "Kamu mendapat tagihan sebesar" + "\\n" + " -- Rp. " + totalAmount + " -- " + "\\n"
                + "\\n" + "Ayo bayar disini" + "\\n" + "bit.ly/pembayaran_sharing_bill" + "\\n" + "\\n" + "Rincian:\\n");

        for (Item item : itemsToSend) {
            // Format harga dan total tanpa angka desimal .0
            String formattedPrice = String.format("%.0f", item.getPrice());
            String formattedTotal = String.format("%.0f", item.getQuantity() * item.getPrice());

            jsonPayload.append("Nama Barang: ").append(item.getName()).append("\\n")
                    .append("Jumlah: ").append(item.getQuantity()).append("\\n")
                    .append("Harga: Rp. ").append(formattedPrice).append("\\n")
                    .append("Total: Rp. ").append(formattedTotal).append("\\n")
                    .append("\\n");
        }

        // Add the total amount to the JSON payload
        jsonPayload.append("Jangan lupa bayar ya :3\\n" +
                "        \"\n" +
                "    }\n" +
                "}");

        return jsonPayload.toString();
    }

    private Long generateTransId() {
        // Contoh: Mendapatkan idTrans secara otomatis (gunakan logika bisnis Anda)
        // Misalnya, bisa menggunakan timestamp atau generator unik lainnya
        return System.currentTimeMillis();
    }





    private String calculateTotalAmount(List<Item> items) {
        double totalAmount = 0;
        for (Item item : items) {
            totalAmount += item.getPrice() * item.getQuantity();
        }
        // Menggunakan format string untuk menghilangkan desimal .0
        return String.format("%.0f", totalAmount);
    }
}