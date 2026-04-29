package org.example.service.handler;

import org.example.model.Account;
import org.example.model.User;
import org.example.service.AccountService;
import org.example.service.Command;
import org.example.service.ConsoleInput;
import org.example.service.TransferService;
import org.example.service.UserService;

import java.math.BigDecimal;

public class WithdrawHandler implements Command {

    @Override
    public void execute(UserService userService, AccountService accountService, TransferService transferService) {
        User user = ConsoleInput.readExistingUser(
                "Введите имя пользователя: ",
                userService.getUserRepository().getUsers()
        );
        int idx = ConsoleInput.readAccountIndex("Выберите индекс счёта: ", user);
        BigDecimal amount = ConsoleInput.readBigDecimal("Введите сумму снятия: ");

        Account account = user.getAccounts().get(idx);
        if (account.getBalance().compareTo(amount) < 0) {
            System.out.printf("  [!] Недостаточно средств. Баланс: %.2f%n", account.getBalance());
            return;
        }
        account.setBalance(account.getBalance().subtract(amount));
        System.out.printf("Снятие выполнено. Новый баланс: %.2f%n", account.getBalance());
    }

    @Override
    public String getName() { return "Снять со счёта"; }
}
