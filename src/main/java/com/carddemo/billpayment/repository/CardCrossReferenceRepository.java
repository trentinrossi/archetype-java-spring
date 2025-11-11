package com.carddemo.billpayment.repository;

import com.carddemo.billpayment.entity.CardCrossReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardCrossReferenceRepository extends JpaRepository<CardCrossReference, Long> {
    
    Optional<CardCrossReference> findByXrefAcctId(String xrefAcctId);
    
    Optional<CardCrossReference> findByXrefCardNum(String xrefCardNum);
    
    Optional<CardCrossReference> findByXrefAcctIdAndXrefCardNum(String xrefAcctId, String xrefCardNum);
    
    boolean existsByXrefAcctId(String xrefAcctId);
    
    boolean existsByXrefCardNum(String xrefCardNum);
    
    boolean existsByXrefAcctIdAndXrefCardNum(String xrefAcctId, String xrefCardNum);
    
    List<CardCrossReference> findAllByXrefAcctId(String xrefAcctId);
    
    List<CardCrossReference> findAllByXrefCardNum(String xrefCardNum);
    
    @Query("SELECT c FROM CardCrossReference c WHERE c.xrefAcctId = :xrefAcctId")
    List<CardCrossReference> findCrossReferencesByAccountId(@Param("xrefAcctId") String xrefAcctId);
    
    @Query("SELECT c FROM CardCrossReference c WHERE c.xrefCardNum = :xrefCardNum")
    List<CardCrossReference> findCrossReferencesByCardNumber(@Param("xrefCardNum") String xrefCardNum);
    
    long countByXrefAcctId(String xrefAcctId);
    
    long countByXrefCardNum(String xrefCardNum);
}
