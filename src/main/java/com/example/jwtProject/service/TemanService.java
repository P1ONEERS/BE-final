package com.example.jwtProject.service;

import com.example.jwtProject.model.Teman;
import com.example.jwtProject.repository.TemanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TemanService {

    @Autowired
    private TemanRepository temanRepository;

    public List<Teman> getAllUsers() {
        return temanRepository.findAll();
    }

//    public Users getUsersById(Long id_user) {
//        return userRepository.findById(id_user).orElse(null);
//    }
    public Teman getUsersById(Long id_user) {
    return temanRepository.findById(id_user).orElse(null);
}
    public List<Teman> getUsersByIds(List<Long> id_user) {
        return temanRepository.findAllById(id_user);
    }

    public Teman createUsers(Teman user) {
        return temanRepository.save(user);
    }

    public Teman updateUsers(Long id_user, Teman newTeman) {
        Optional<Teman> existingUsersOptional = temanRepository.findById(id_user);
        if (existingUsersOptional.isPresent()) {
            Teman existingTeman = existingUsersOptional.get();
            existingTeman.setId_user(newTeman.getId_user());
            existingTeman.setName(newTeman.getName());
            existingTeman.setNoTelp(newTeman.getNoTelp());
            return temanRepository.save(existingTeman);
        } else {
            return null;
        }
    }
    public Teman getUsersByIdWithItems(Long userId) {
        return temanRepository.findById(userId)
                .map(user -> {
                    // Eagerly fetch the items associated with the user
                    user.getItems().size(); // This triggers the fetch
                    return user;
                })
                .orElse(null);
    }

    public void deleteUsers(Long userId) {
        temanRepository.deleteById(userId);
    }
}
