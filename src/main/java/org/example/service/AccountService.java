package org.example.service;

import org.example.config.SystemProperties;
import org.example.model.Account;
import org.example.repository.AccountRepository;
import org.example.service.factory.AccountFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final SystemProperties systemProperties;

    @Autowired
    public AccountService(AccountRepository accountRepository, SystemProperties systemProperties) {
        this.accountRepository = accountRepository;
        this.systemProperties = systemProperties;
    }
    public Account addAccount(int walletNumber, String ownerUserId) {
        return AccountFactory.createAccount(walletNumber, ownerUserId, systemProperties.getDefaultBalance());
    }

    public AccountRepository getAccountRepository() {
        return accountRepository;
    }
}
