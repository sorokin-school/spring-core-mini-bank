package sorokin.java.course.operations.commands;

import org.springframework.stereotype.Component;
import sorokin.java.course.bank.account.Account;
import sorokin.java.course.bank.account.AccountService;
import sorokin.java.course.operations.ConsoleOperationType;
import sorokin.java.course.operations.OperationCommand;
import sorokin.java.course.users.User;
import sorokin.java.course.users.UserService;

import java.util.Scanner;

@Component
public class AccountCreateCommand implements OperationCommand {

    private final AccountService accountService;
    private final UserService userService;
    private final Scanner scanner;

    public AccountCreateCommand(AccountService accountService, UserService userService, Scanner scanner) {
        this.accountService = accountService;
        this.userService = userService;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.println("Enter the user id for which to create an account:");
        var userId = scanner.nextLong();
        var user = userService.findUserById(userId);
        Account account = accountService.createAccount(user);
        System.out.println("New account created with ID: " + account.getId() + " for user: " + user.getLogin());
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_CREATE;
    }
}

