package com.example.jwtProject.service;

import com.example.jwtProject.model.Item;
import com.example.jwtProject.model.Teman;
import com.example.jwtProject.repository.ItemRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
//kepake tambahin function baru naufal
@Service
public class ItemService {

    @PersistenceContext
    private EntityManager entityManager;


    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private TemanService temanService; // Anda perlu menyediakan service untuk pengelolaan User

    @Transactional
    public void truncateTable() {
        String tableName = "item";
        entityManager.createNativeQuery("SET foreign_key_checks = 0").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
        entityManager.createNativeQuery("SET foreign_key_checks = 1").executeUpdate();
    }

    @Transactional
    public void refreshAllItems() {
        Query query = entityManager.createQuery("SELECT i FROM Item i");
        List<Item> items = query.getResultList();

        for (Item item : items) {
            entityManager.refresh(item);
        }
    }

    public List<Item> getAllItem() {
        return itemRepository.findAll();
    }

    public List<Item> createItems(List<Item> items, Long id_user) {
        if (items == null || items.isEmpty()) {
            System.out.println("Data items tidak boleh kosong");
            return null;
        }

        // Tambahkan log id_user
        System.out.println("Received request to create items for id_user: " + id_user);

        Teman user = temanService.getUsersById(id_user); // Mendapatkan objek Users dari ID

        if (user == null) {
            System.out.println("User dengan ID " + id_user + " tidak ditemukan.");
            return null;
        }

        // Set user untuk setiap item, set flag 0, dan simpan ke database
        items.forEach(item -> {
            item.setUsers(user);
            item.setId_item(null); // Reset ID untuk memastikan ID yang diberikan oleh pengguna digunakan
            item.setFlag(0); // Set flag menjadi 0
            itemRepository.save(item);
        });

        return items;
    }
//    public List<Item> getItemsByUserIdAndFlag(Long id_user, int flag) {
//        return itemRepository.findByUsersIdAndFlag(id_user, flag);
//    }

    //ini kepake functionnya
    public List<Item> getItemByIds(List<Long> id_user) {
        return itemRepository.findAllById(id_user);
    }

    //ini di ubah
    public Item getItemById(Long id_item) {
        return itemRepository.findById(id_item).orElse(null);
    }

    public Item createItem(Item item) {
        if (item.getId_item() != null) {
            // ID diberikan oleh pengguna, pastikan untuk meresetnya
            item.setId_item(null);
        }

        // Simpan item ke database
        return itemRepository.save(item);
    }

    public Item updateItem(Long id_item, Item newItem) {
        // Sisipkan log atau logika lain sesuai kebutuhan Anda
        // ...

        // Contoh update item
        Item existingItem = itemRepository.findById(id_item).orElse(null);
        if (existingItem != null) {
            existingItem.setName(newItem.getName());
            existingItem.setPrice(newItem.getPrice());
            existingItem.setQuantity(newItem.getQuantity());
            itemRepository.save(existingItem);
        }

        return existingItem;
    }

    public void deleteItem(Long id_item) {
        itemRepository.deleteById(id_item);
    }
}