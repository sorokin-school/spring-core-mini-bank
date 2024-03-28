package sorokin.java.course.bank.account;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import sorokin.java.course.users.User;

import javax.annotation.processing.SupportedOptions;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class AccountService {

    private final SessionFactory sessionFactory;
    private final AccountProperties accountProperties;

    public AccountService(
            SessionFactory sessionFactory,
            AccountProperties accountProperties
    ) {
        this.sessionFactory = sessionFactory;
        this.accountProperties = accountProperties;
    }

    public Account createAccount(User user) { //todo executeInNewTransactionOrSupportExisted
        return executeInTransaction(() -> {
            Account newAccount = new Account(null, user, accountProperties.getDefaultAmount());
            sessionFactory.getCurrentSession().persist(newAccount);
            return newAccount;
        });
    }

    public void withdraw(Long fromAccountId, Integer amount) {
        executeInTransaction(() -> {
            Account account = findAccountById(fromAccountId)
                    .orElseThrow(() -> new IllegalArgumentException("No such account: id=%s".formatted(fromAccountId)));

            if (amount > account.getMoneyAmount()) {
                throw new IllegalArgumentException(
                        "No such money to withdraw from account: id=%s, moneyAmount=%s, attemptedWithdraw=%s"
                                .formatted(account.getId(), account.getMoneyAmount(), amount)
                );
            }
            account.setMoneyAmount(account.getMoneyAmount() - amount);
            return 0;
        });
    }

    public void deposit(Long toAccountId, Integer amount) {
        executeInTransaction(() -> {
            var account = findAccountById(toAccountId)
                    .orElseThrow(() -> new IllegalArgumentException("No such account: id=%s".formatted(toAccountId)));
            account.setMoneyAmount(account.getMoneyAmount() + amount);
            return 0;
        });
    }

    public Account closeAccount(Long accountId) {
        return executeInTransaction(() -> {
            Account accountToClose = findAccountById(accountId)
                    .orElseThrow(() -> new IllegalArgumentException("No such account: id=%s".formatted(accountId)));

            var userAccounts = accountToClose.getUser().getAccountList();
            if (userAccounts.size() == 1) {
                throw new IllegalStateException("Can't close the only one account");
            }
            sessionFactory.getCurrentSession().remove(accountToClose);

            var accountToTransferMoney = userAccounts.stream()
                    .filter(it -> !it.getId().equals(accountId))
                    .findFirst()
                    .orElseThrow();

            var newAmount = accountToTransferMoney.getMoneyAmount() + accountToClose.getMoneyAmount();
            accountToTransferMoney.setMoneyAmount(newAmount);
            return accountToClose;
        });
    }

    public void transfer(Long fromAccountId, Long toAccountId, int amount) {
        executeInTransaction(() -> {
            Account accountFrom = findAccountById(fromAccountId)
                    .orElseThrow(() -> new IllegalArgumentException("No such account: id=%s".formatted(fromAccountId)));
            Account accountTo = findAccountById(toAccountId)
                    .orElseThrow(() -> new IllegalArgumentException("No such account: id=%s".formatted(toAccountId)));

            if (amount > accountFrom.getMoneyAmount()) {
                throw new IllegalArgumentException(
                        "No such money to transfer from account: id=%s, moneyAmount=%s, attemptedWithdraw=%s"
                                .formatted(accountFrom.getId(), accountFrom.getMoneyAmount(), amount)
                );
            }
            accountFrom.setMoneyAmount(accountFrom.getMoneyAmount() - amount);

            int amountToTransfer = accountTo.getUser() == accountFrom.getUser()
                    ? amount
                    : (int) Math.round(amount * (1 - accountProperties.getTransferCommission()));
            accountTo.setMoneyAmount(accountTo.getMoneyAmount() + amountToTransfer);
            return 0;
        });
    }

    private Optional<Account> findAccountById(Long id) {
        return Optional.ofNullable(
                sessionFactory.getCurrentSession().get(Account.class, id)
        );
    }

    private<T> T executeInTransaction(
            Supplier<T> supplier
    ) {
        var session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            var returnValue = supplier.get();
            session.getTransaction().commit();
            return returnValue;
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
    }
}
