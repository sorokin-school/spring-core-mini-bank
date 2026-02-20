package sorokin.java.course.operations.commands;

import org.springframework.stereotype.Component;
import sorokin.java.course.account.AccountService;
import sorokin.java.course.console.ConsoleInput;
import sorokin.java.course.operations.ConsoleOperationType;
import sorokin.java.course.operations.OperationCommand;

@Component
public class AccountTransferCommand implements OperationCommand {

    private final AccountService accountService;
    private final ConsoleInput consoleInput;

    public AccountTransferCommand(AccountService accountService, ConsoleInput consoleInput) {
        this.accountService = accountService;
        this.consoleInput = consoleInput;
    }

    @Override
    public void execute() {
        long fromAccountId = consoleInput.readPositiveLong("Enter source account id:", "source account id");
        long toAccountId = consoleInput.readPositiveLong("Enter target account id:", "target account id");
        if (fromAccountId == toAccountId) {
            throw new IllegalArgumentException("source and target account id must be different");
        }

        int amount = consoleInput.readPositiveInt("Enter amount:", "amount");
        AccountService.TransferResult result = accountService.transfer(fromAccountId, toAccountId, amount);

        if (result.sameUser()) {
            System.out.println(
                    "Transfer completed from account " + result.fromAccountId() +
                            " to account " + result.toAccountId() +
                            ". Amount: " + result.amount() +
                            " (no commission, same user)"
            );
            return;
        }

        System.out.println(
                "Transfer completed from account " + result.fromAccountId() +
                        " to account " + result.toAccountId() +
                        ". Amount: " + result.amount() +
                        ", commission: " + result.commissionAmount() +
                        ", recipient received: " + result.recipientAmount()
        );
    }

    @Override
    public ConsoleOperationType getOperationType() {
        return ConsoleOperationType.ACCOUNT_TRANSFER;
    }
}
