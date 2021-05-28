package com.test.task.controllers;

import com.test.task.entity.Balance;
import com.test.task.exceptions.BalanceNotFoundException;
import com.test.task.exceptions.InsufficientBalanceException;
import com.test.task.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/v1/balance")
@Validated
public class BalanceController {

    @Autowired
    private BalanceService balanceService;

    @GetMapping("/get-balance/{clientId}")
    public ResponseEntity<Balance> getBalance(@PathVariable("clientId") @Positive(message = "{clientid.validation.error}") Integer clientId) {
        Balance balance = balanceService.getBalanceByClientId(clientId);
        if (balance == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(balance);
    }

    @PutMapping("/add-balance/{clientId}/{amount}")
    public ResponseEntity<Balance> addBalance(@PathVariable("clientId") @Positive(message = "{clientid.validation.error}") Integer clientId,
                                              @PathVariable("amount") @Positive(message = "{balance.validation.error}") Double amount) {
        try {
            Balance balance = balanceService.addBalance(clientId, amount);
            return ResponseEntity.ok(balance);
        } catch(BalanceNotFoundException bnfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/charge-balance/{clientId}/{amount}")
    public ResponseEntity<Balance> chargeBalance(@PathVariable("clientId") @Positive(message = "{clientid.validation.error}") Integer clientId,
                                                 @PathVariable("amount") @Positive(message = "{balance.validation.error}") Double amount) {
        try {
            Balance balance = balanceService.chargeBalance(clientId, amount);
            return ResponseEntity.ok(balance);
        } catch(BalanceNotFoundException bnfe) {
            return ResponseEntity.notFound().build();
        } catch (InsufficientBalanceException ibe) {
            return ResponseEntity.status(444).build();
        }
    }



}
