package com.example.jwtProject.controller;

import com.example.jwtProject.model.Item;
import com.example.jwtProject.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping
    public List<Item> getAllItem() {
        return itemService.getAllItem();
    }

    @GetMapping("/{id_item}")
    public Item getItemById(@PathVariable Long id_item) {
        return itemService.getItemById(id_item);
    }

    @PostMapping
    public Item createItem(@RequestBody Item item) {
        System.out.println("Received request to create item: " + item.toString());
        Item createdItem = itemService.createItem(item);
        System.out.println("Created item: " + createdItem.toString());
        return createdItem;
    }

    @PostMapping("/multiple")
    public List<Item> createItems(@RequestBody List<Item> items,
                                  @RequestParam("id_user") Long id_user) {
        try {
            if (items == null || items.isEmpty()) {
                System.out.println("Data items tidak boleh kosong");
                return null;
            }

            // Tambahkan log id_user
            System.out.println("Received request to create items for id_user: " + id_user);

            List<Item> createdItems = itemService.createItems(items, id_user);
            System.out.println("Created items: " + createdItems.toString());
            return createdItems;
        } catch (Exception e) {
            System.out.println("Gagal memproses data: " + e.getMessage());
            return null;
        }
    }

    @PostMapping("/truncate-table")
    public void truncateTable() {
        itemService.truncateTable();
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshAllItems() {
        itemService.refreshAllItems();
        return new ResponseEntity<>("All items refreshed successfully", HttpStatus.OK);
    }
    //ini kepake functionnya
    @GetMapping("/multiple")
    public ResponseEntity<List<Item>> getItemByIds(@RequestParam List<Long> id_item) {
        List<Item> items = itemService.getItemByIds(id_item);

        if (items.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(items, HttpStatus.OK);
        }
    }

}