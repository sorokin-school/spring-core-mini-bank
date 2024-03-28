package sorokin.java.course.operations.commands;

import org.springframework.stereotype.Component;
import sorokin.java.course.bank.account.AccountService;
import sorokin.java.course.operations.ConsoleOperationType;
import sorokin.java.course.operations.OperationCommand;

import java.util.Scanner;

@Component
public class AccountWithdrawCommand implements OperationCommand {

    private final AccountService accountService;
    private final Scanner scanner;

    public AccountWithdrawCommand(AccountService accountService, Scanner scanner) {
        this.accountService = accountService;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.println("Enter account ID to withdraw from:");
        var accountId = Long.parseLong(scanner.nextLine());
        System.out.println("Enter amount to withdraw:");
        int amount = Integer.parseInt(scanner.nextLine());
        accountService.withdraw(accountId, amount);
        System.out.println("Amount " + amount + " withdrawn from account ID " + accountId + ".");
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_WITHDRAW;
    }
}

