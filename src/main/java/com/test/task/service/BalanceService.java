package com.test.task.service;

import com.test.task.entity.Balance;
import com.test.task.exceptions.BalanceNotFoundException;
import com.test.task.exceptions.InsufficientBalanceException;
import com.test.task.repository.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BalanceService {

    @Autowired
    private BalanceRepository balanceRepository;

    public Balance getBalanceByClientId(Integer clientId) {
        Balance balance = balanceRepository.findByClientId(clientId);
        return balance;
    }

    public Balance addBalance(Integer clientId, Double amount) {
        Balance balance = balanceRepository.findByClientId(clientId);

        if (balance == null) {
            balance = new Balance();
            balance.setBalance(amount);
            balance.setClientId(clientId);
        } else {
            balance.addBalance(amount);
        }

        balanceRepository.save(balance);
        return balance;
    }

    public Balance chargeBalance(Integer clientId, Double amount) {
        Balance balance = balanceRepository.findByClientId(clientId);
        if (balance == null) {
            throw new BalanceNotFoundException("{balance.notfound.error}");
        }

        if (balance.getBalance() < amount) {
            throw new InsufficientBalanceException("{balance.insufficient.error}");
        }

        balance.addBalance(-amount);
        balanceRepository.save(balance);

        return balance;
    }
}
