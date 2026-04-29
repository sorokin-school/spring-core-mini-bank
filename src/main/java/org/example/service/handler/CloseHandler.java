package org.example.service.handler;

import org.example.model.Account;
import org.example.model.User;
import org.example.service.AccountService;
import org.example.service.Command;
import org.example.service.ConsoleInput;
import org.example.service.TransferService;
import org.example.service.UserService;

public class CloseHandler implements Command {

    @Override
    public void execute(UserService userService, AccountService accountService, TransferService transferService) {
        User user = ConsoleInput.readExistingUser(
                "Введите имя пользователя: ",
                userService.getUserRepository().getUsers()
        );
        Account account = ConsoleInput.readAccountOfUser("UUID счёта для закрытия: ", user);
        boolean removed = accountService.getAccountRepository().deleteAccount(account.getUuid());
        if (removed) {
            user.getAccounts().remove(account);
            System.out.println("Счёт " + account.getUuid() + " закрыт.");
        } else {
            System.out.println("  [!] Счёт не найден в репозитории.");
        }
    }

    @Override
    public String getName() { return "Закрыть счёт"; }
}
