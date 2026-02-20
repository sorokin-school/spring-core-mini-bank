package sorokin.java.course.operations.commands;

import org.springframework.stereotype.Component;
import sorokin.java.course.account.AccountService;
import sorokin.java.course.console.ConsoleInput;
import sorokin.java.course.operations.ConsoleOperationType;
import sorokin.java.course.operations.OperationCommand;

@Component
public class AccountCloseCommand implements OperationCommand {

    private final AccountService accountService;
    private final ConsoleInput consoleInput;

    public AccountCloseCommand(AccountService accountService, ConsoleInput consoleInput) {
        this.accountService = accountService;
        this.consoleInput = consoleInput;
    }

    @Override
    public void execute() {
        long accountId = consoleInput.readPositiveLong("Enter account id to close:", "account id");
        AccountService.CloseAccountResult result = accountService.closeAccount(accountId);
        System.out.println(
                "Account " + result.closedAccountId() +
                        " closed. Remaining balance " + result.transferredAmount() +
                        " transferred to account " + result.targetAccountId() + "."
        );
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_CLOSE;
    }
}
