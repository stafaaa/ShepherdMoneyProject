package com.shepherdmoney.interviewproject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDate;
import java.util.TreeMap;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String issuanceBank;

    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;  // Link to the credit card's owner

    @Transient  // Not stored in the database directly
    private TreeMap<LocalDate, Double> balanceHistory = new TreeMap<>();  // Efficient storage and retrieval

    // Helper methods to manage balance history
    public void updateBalance(LocalDate date, double balance) {
        balanceHistory.put(date, balance);
        fillGapToDate(LocalDate.now());
    }

    private void fillGapToDate(LocalDate endDate) {
        LocalDate lastEntryDate = balanceHistory.lastKey();
        while (!lastEntryDate.isBefore(endDate)) {
            lastEntryDate = lastEntryDate.plusDays(1);
            balanceHistory.putIfAbsent(lastEntryDate, balanceHistory.lowerEntry(lastEntryDate).getValue());
        }
    }

    public Double getBalanceOn(LocalDate date) {
        return balanceHistory.floorEntry(date).getValue();
    }
}
