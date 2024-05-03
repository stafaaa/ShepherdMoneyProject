package com.shepherdmoney.interviewproject.repository;

import com.shepherdmoney.interviewproject.model.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository to store credit cards.
 */
@Repository("CreditCardRepo")
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {
    // Method to find all credit cards by the owner's ID
    List<CreditCard> findByOwnerId(int ownerId);

    // Method to find a credit card by its number
    CreditCard findByNumber(String number);
}
