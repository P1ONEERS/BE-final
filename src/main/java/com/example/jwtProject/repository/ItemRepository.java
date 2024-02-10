package com.example.jwtProject.repository;


import com.example.jwtProject.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>{
//    @Query("SELECT i.id_item FROM Item i ORDER BY i.id_item DESC")
//    List<Long> getItemByIds();
}