package org.example.service.handler;

import org.example.model.Account;
import org.example.model.User;
import org.example.service.AccountService;
import org.example.service.Command;
import org.example.service.ConsoleInput;
import org.example.service.TransferService;
import org.example.service.UserService;

import java.math.BigDecimal;

public class TransferHandler implements Command {

    @Override
    public void execute(UserService userService, AccountService accountService, TransferService transferService) {
        User fromUser = ConsoleInput.readExistingUser(
                "Пользователь-отправитель (имя): ",
                userService.getUserRepository().getUsers()
        );
        Account fromAccount = ConsoleInput.readAccountOfUser("UUID счёта-источника: ", fromUser);

        User toUser = ConsoleInput.readExistingUser(
                "Пользователь-получатель (имя): ",
                userService.getUserRepository().getUsers()
        );
        Account toAccount = ConsoleInput.readAccountOfUser("UUID счёта-получателя: ", toUser);

        BigDecimal amount = ConsoleInput.readBigDecimal("Сумма перевода: ");

        transferService.transfer(fromAccount.getUuid(), toAccount.getUuid(), amount);
    }

    @Override
    public String getName() { return "Перевод между счетами"; }
}
