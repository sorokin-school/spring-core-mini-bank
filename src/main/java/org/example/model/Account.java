package org.example.model;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Account {
    private String uuid;
    private String ownerUUID;
    private BigDecimal balance;
    public Account (int walletNumber, String ownerUUID, BigDecimal balance) {
        this.uuid = ownerUUID + "-" + walletNumber;
        this.ownerUUID = ownerUUID;
        this.balance = balance;
    }
    public Account(){

    }
    public String getUuid() {
        return uuid;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(String ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "Account{" +
                "uuid='" + uuid + '\'' +
                ", ownerUUID='" + ownerUUID + '\'' +
                ", balance=" + balance +
                '}';
    }
}

