package org.example.service.handler;

import org.example.model.Account;
import org.example.model.User;
import org.example.service.AccountService;
import org.example.service.Command;
import org.example.service.ConsoleInput;
import org.example.service.TransferService;
import org.example.service.UserService;

import java.math.BigDecimal;

public class DepositHandler implements Command {

    @Override
    public void execute(UserService userService, AccountService accountService, TransferService transferService) {
        User user = ConsoleInput.readExistingUser(
                "Введите имя пользователя: ",
                userService.getUserRepository().getUsers()
        );
        int idx = ConsoleInput.readAccountIndex("Выберите индекс счёта: ", user);
        BigDecimal amount = ConsoleInput.readBigDecimal("Введите сумму пополнения: ");

        Account account = user.getAccounts().get(idx);
        account.setBalance(account.getBalance().add(amount));

        System.out.printf("Баланс успешно пополнен. Новый баланс: %.2f%n", account.getBalance());
    }

    @Override
    public String getName() { return "Пополнить счёт"; }
}
