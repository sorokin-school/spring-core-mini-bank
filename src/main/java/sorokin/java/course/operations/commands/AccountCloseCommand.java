package sorokin.java.course.operations.commands;

import org.springframework.stereotype.Component;
import sorokin.java.course.bank.account.AccountService;
import sorokin.java.course.operations.ConsoleOperationType;
import sorokin.java.course.operations.OperationCommand;
import sorokin.java.course.users.UserService;

import java.util.Scanner;

@Component
public class AccountCloseCommand implements OperationCommand {

    private final AccountService accountService;
    private final UserService userService;
    private final Scanner scanner;

    public AccountCloseCommand(AccountService accountService, UserService userService, Scanner scanner) {
        this.accountService = accountService;
        this.userService = userService;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.println("Enter account ID to close:");
        int accountId = Integer.parseInt(scanner.nextLine());
        var closedAccount = accountService.closeAccount(accountId);
        var user = userService.findUserById(closedAccount.getUserId());
        user.getAccountList().remove(closedAccount);
        System.out.println("Account with ID " + accountId + " has been closed.");
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_CLOSE;
    }
}

