package org.example.service.factory;

import org.example.model.Account;

import java.math.BigDecimal;

public class AccountFactory {
    public static Account createAccount(int walletNumber, String ownerUUID, BigDecimal balance){
        return new Account(walletNumber, ownerUUID, balance);
    }
}
