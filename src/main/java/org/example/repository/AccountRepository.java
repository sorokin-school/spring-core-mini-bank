package org.example.repository;

import org.example.model.Account;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class AccountRepository {


    private final Map<String, List<Account>> accounts = new HashMap<>();

    public Map<String, List<Account>> getAccounts() {
        return accounts;
    }

    public List<Account> getAccountsByOwner(String ownerUserId) {
        return accounts.getOrDefault(ownerUserId, List.of());
    }
    public Account getAccount(String accountUuid) {
        return accounts.values().stream()
                .flatMap(List::stream)
                .filter(a -> a.getUuid().equals(accountUuid))
                .findFirst()
                .orElse(null);
    }
    public void addAccount(Account account) {
        accounts
            .computeIfAbsent(account.getOwnerUUID(), k -> new ArrayList<>())
            .add(account);
    }
    public boolean deleteAccount(String accountUuid) {
        for (List<Account> list : accounts.values()) {
            boolean removed = list.removeIf(a -> a.getUuid().equals(accountUuid));
            if (removed) return true;
        }
        return false;
    }
}
