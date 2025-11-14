package com.example.demo.repository;

import com.example.demo.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    
    Optional<Card> findByXrefCardNum(String xrefCardNum);
    
    List<Card> findByXrefAcctId(String xrefAcctId);
    
    boolean existsByXrefCardNum(String xrefCardNum);
    
    Optional<Card> findByXrefCardNumAndXrefAcctId(String xrefCardNum, String xrefAcctId);
    
    @Query("SELECT c FROM Card c WHERE c.xrefCardNum = :cardNum AND c.xrefAcctId = :acctId")
    Optional<Card> findCardByCardNumberAndAccountId(@Param("cardNum") String cardNum, @Param("acctId") String acctId);
    
    @Query("SELECT COUNT(c) FROM Card c WHERE c.xrefAcctId = :acctId")
    long countCardsByAccountId(@Param("acctId") String acctId);
    
    @Query("SELECT c FROM Card c WHERE c.xrefAcctId IN :acctIds")
    List<Card> findAllByAccountIds(@Param("acctIds") List<String> acctIds);
    
    List<Card> findAllByXrefCardNumIn(List<String> cardNumbers);
}
