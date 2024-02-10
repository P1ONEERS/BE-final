package com.example.jwtProject.repository;


import com.example.jwtProject.model.Teman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemanRepository extends JpaRepository<Teman, Long> {

}