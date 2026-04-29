package org.example.service.handler;

import org.example.model.Account;
import org.example.model.User;
import org.example.service.AccountService;
import org.example.service.Command;
import org.example.service.ConsoleInput;
import org.example.service.TransferService;
import org.example.service.UserService;

public class CreateAccountHandler implements Command {

    @Override
    public void execute(UserService userService, AccountService accountService, TransferService transferService) {
        User user = ConsoleInput.readExistingUser(
                "Введите имя пользователя: ",
                userService.getUserRepository().getUsers()
        );

        int walletNumber = user.getAccounts().size() + 1;
        Account account = accountService.addAccount(walletNumber, user.getId());
        user.addAccount(account);
        accountService.getAccountRepository().addAccount(account);

        System.out.println("Счёт успешно создан: " + account);
    }

    @Override
    public String getName() { return "Создать счёт"; }
}
