package com.example.demo.repository;

import com.example.demo.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, String> {
    
    List<Card> findByAccountId(String accountId);
    
    List<Card> findByActiveStatus(String activeStatus);
}
