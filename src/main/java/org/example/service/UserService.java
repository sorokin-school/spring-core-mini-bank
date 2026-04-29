package org.example.service;

import org.example.model.User;
import org.example.repository.AccountRepository;
import org.example.repository.UserRepository;
import org.example.service.factory.AccountFactory;
import org.example.service.factory.UserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public UserService(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    public User createUser(String name, int age) {
        User user = UserFactory.createUser(name, age);
        var firstAccount = AccountFactory.createAccount(1, user.getId(), BigDecimal.valueOf(500));
        user.addAccount(firstAccount);
        userRepository.getUsers().put(user.getId(), user);
        accountRepository.addAccount(firstAccount);
        return user;
    }
    public User findUserByName(String name) {
        return userRepository.getUsers().values().stream()
                .filter(u -> u.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public void showAllUsers() {
        if (userRepository.getUsers().isEmpty()) {
            System.out.println("  (нет пользователей)");
            return;
        }
        userRepository.getUsers().values().forEach(System.out::println);
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
}
