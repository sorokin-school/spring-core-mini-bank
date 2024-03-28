package sorokin.java.course.operations.commands;

import org.springframework.stereotype.Component;
import sorokin.java.course.bank.account.AccountService;
import sorokin.java.course.operations.ConsoleOperationType;
import sorokin.java.course.operations.OperationCommand;

import java.util.Scanner;

@Component
public class AccountTransferCommand implements OperationCommand {

    private final AccountService accountService;
    private final Scanner scanner;

    public AccountTransferCommand(AccountService accountService, Scanner scanner) {
        this.accountService = accountService;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.println("Enter source account ID:");
        Long fromAccountId = Long.parseLong(scanner.nextLine());
        System.out.println("Enter target account ID:");
        var toAccountId = Long.parseLong(scanner.nextLine());
        System.out.println("Enter amount to transfer:");
        int amount = Integer.parseInt(scanner.nextLine());
        accountService.transfer(fromAccountId, toAccountId, amount);
        System.out.println("Amount " + amount + " transferred from account ID " + fromAccountId + " to account ID " + toAccountId + ".");
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_TRANSFER;
    }
}
