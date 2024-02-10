package com.example.jwtProject.controller;



import com.example.jwtProject.model.Teman;
import com.example.jwtProject.service.TemanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/teman")
public class TemanController {

    @Autowired
    private TemanService temanService;
    @GetMapping
    public List<Teman> getAllUsers() {
        return temanService.getAllUsers();
    }

    @GetMapping("/multiple")
    public ResponseEntity<List<Teman>> getUsersByIds(@RequestParam List<Long> id_user) {
        List<Teman> users = temanService.getUsersByIds(id_user);

        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
    }

    @PostMapping
    public Teman createUsers(@RequestBody Teman user) {
        return temanService.createUsers(user);
    }

    @PutMapping("/{userId}")
    public Teman updateUsers(@PathVariable Long id_user, @RequestBody Teman user) {
        return temanService.updateUsers(id_user, user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUsers(@PathVariable Long id_user) {
        temanService.deleteUsers(id_user);
    }
}

