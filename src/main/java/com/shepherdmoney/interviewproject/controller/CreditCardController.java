package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.CreditCardRepository;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import com.shepherdmoney.interviewproject.vo.request.AddCreditCardToUserPayload;
import com.shepherdmoney.interviewproject.vo.request.UpdateBalancePayload;
import com.shepherdmoney.interviewproject.vo.response.CreditCardView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CreditCardController {

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/credit-card")
    public ResponseEntity<Integer> addCreditCardToUser(@RequestBody AddCreditCardToUserPayload payload) {
        User user = userRepository.findById(payload.getUserId()).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(null);
        }
        CreditCard newCard = new CreditCard();
        newCard.setOwner(user);
        newCard.setIssuanceBank(payload.getCardIssuanceBank());
        newCard.setNumber(payload.getCardNumber());
        CreditCard savedCard = creditCardRepository.save(newCard);
        return ResponseEntity.ok(savedCard.getId());
    }

    @GetMapping("/credit-cards/user/{userId}")
    public ResponseEntity<List<CreditCardView>> getAllCardOfUser(@PathVariable int userId) {
        List<CreditCard> cards = creditCardRepository.findByOwnerId(userId);
        List<CreditCardView> cardViews = cards.stream()
            .map(card -> new CreditCardView(card.getIssuanceBank(), card.getNumber()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(cardViews);
    }

    @GetMapping("/credit-card/{cardNumber}/user-id")
    public ResponseEntity<Integer> getUserIdForCreditCard(@PathVariable String cardNumber) {
        CreditCard card = creditCardRepository.findByNumber(cardNumber);
        if (card != null && card.getOwner() != null) {
            return ResponseEntity.ok(card.getOwner().getId());
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/credit-card/update-balance")
    public ResponseEntity<String> updateCreditCardBalance(@RequestBody List<UpdateBalancePayload> payloads) {
        for (UpdateBalancePayload payload : payloads) {
            CreditCard card = creditCardRepository.findByNumber(payload.getCreditCardNumber());
            if (card == null) {
                return ResponseEntity.badRequest().body("Credit card not found for number: " + payload.getCreditCardNumber());
            }
            card.updateBalance(payload.getBalanceDate(), payload.getBalanceAmount());
        }
        return ResponseEntity.ok("Balances updated successfully.");
    }
}
