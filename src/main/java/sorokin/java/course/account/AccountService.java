package sorokin.java.course.account;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.stereotype.Component;
import sorokin.java.course.user.User;

import java.util.List;
import java.util.function.Supplier;

@Component
public class AccountService {

    private final SessionFactory sessionFactory;
    private final AccountProperties accountProperties;

    public AccountService(SessionFactory sessionFactory, AccountProperties accountProperties) {
        this.sessionFactory = sessionFactory;
        this.accountProperties = accountProperties;
    }

    public Account createAccount(Long userId) {
        validatePositiveId(userId, "user id");
        return executeInTransactionOrJoin(() -> {
            Session session = sessionFactory.getCurrentSession();
            User user = session.get(User.class, userId);
            if (user == null) {
                throw new IllegalArgumentException("No such user with id=%s".formatted(userId));
            }

            Account newAccount = new Account(null, user, accountProperties.getDefaultAmount());
            session.persist(newAccount);
            user.getAccountList().add(newAccount);
            return newAccount;
        });
    }

    public void withdraw(Long fromAccountId, Integer amount) {
        validatePositiveId(fromAccountId, "account id");
        validatePositiveAmount(amount);

        executeInTransactionOrJoin(() -> {
            Session session = sessionFactory.getCurrentSession();
            Account account = findAccountOrThrow(session, fromAccountId);

            if (amount > account.getMoneyAmount()) {
                throw new IllegalArgumentException(
                        "insufficient funds on account id=%s, moneyAmount=%s, attempted withdraw=%s"
                                .formatted(account.getId(), account.getMoneyAmount(), amount)
                );
            }

            account.setMoneyAmount(account.getMoneyAmount() - amount);
            return null;
        });
    }

    public void deposit(Long toAccountId, Integer amount) {
        validatePositiveId(toAccountId, "account id");
        validatePositiveAmount(amount);

        executeInTransactionOrJoin(() -> {
            Session session = sessionFactory.getCurrentSession();
            Account account = findAccountOrThrow(session, toAccountId);
            account.setMoneyAmount(account.getMoneyAmount() + amount);
            return null;
        });
    }

    public CloseAccountResult closeAccount(Long accountId) {
        validatePositiveId(accountId, "account id");

        return executeInTransactionOrJoin(() -> {
            Session session = sessionFactory.getCurrentSession();
            Account accountToClose = findAccountOrThrow(session, accountId);

            List<Account> userAccounts = accountToClose.getUser().getAccountList();
            if (userAccounts.size() <= 1) {
                throw new IllegalStateException("Can't close the only one account");
            }

            Account accountToTransferMoney = userAccounts.stream()
                    .filter(it -> !it.getId().equals(accountId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No account available to transfer remaining balance"));

            int transferredAmount = accountToClose.getMoneyAmount();
            accountToTransferMoney.setMoneyAmount(accountToTransferMoney.getMoneyAmount() + transferredAmount);
            userAccounts.remove(accountToClose);
            session.remove(accountToClose);

            return new CloseAccountResult(accountId, accountToTransferMoney.getId(), transferredAmount);
        });
    }

    public TransferResult transfer(Long fromAccountId, Long toAccountId, Integer amount) {
        validatePositiveId(fromAccountId, "source account id");
        validatePositiveId(toAccountId, "target account id");
        validatePositiveAmount(amount);

        if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException("source and target account id must be different");
        }

        return executeInTransactionOrJoin(() -> {
            Session session = sessionFactory.getCurrentSession();
            Account accountFrom = findAccountOrThrow(session, fromAccountId);
            Account accountTo = findAccountOrThrow(session, toAccountId);

            if (amount > accountFrom.getMoneyAmount()) {
                throw new IllegalArgumentException(
                        "insufficient funds on account id=%s, moneyAmount=%s, attempted transfer=%s"
                                .formatted(accountFrom.getId(), accountFrom.getMoneyAmount(), amount)
                );
            }

            accountFrom.setMoneyAmount(accountFrom.getMoneyAmount() - amount);

            boolean sameUser = accountFrom.getUserId().equals(accountTo.getUserId());
            int recipientAmount = sameUser
                    ? amount
                    : (int) Math.round(amount * (1 - accountProperties.getTransferCommission()));
            int commissionAmount = amount - recipientAmount;

            accountTo.setMoneyAmount(accountTo.getMoneyAmount() + recipientAmount);

            return new TransferResult(
                    accountFrom.getId(),
                    accountTo.getId(),
                    amount,
                    commissionAmount,
                    recipientAmount,
                    sameUser
            );
        });
    }

    private Account findAccountOrThrow(Session session, Long accountId) {
        Account account = session.get(Account.class, accountId);
        if (account == null) {
            throw new IllegalArgumentException("No such account: id=%s".formatted(accountId));
        }
        return account;
    }

    private void validatePositiveId(Long id, String fieldName) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(fieldName + " must be > 0");
        }
    }

    private void validatePositiveAmount(Integer amount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("amount must be > 0");
        }
    }

    private <T> T executeInTransactionOrJoin(Supplier<T> action) {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.getTransaction();
        boolean owner = transaction.getStatus() == TransactionStatus.NOT_ACTIVE;

        if (owner) {
            transaction = session.beginTransaction();
        }

        try {
            T result = action.get();
            if (owner) {
                transaction.commit();
            }
            return result;
        } catch (RuntimeException e) {
            if (owner && transaction.getStatus() == TransactionStatus.ACTIVE) {
                transaction.rollback();
            }
            throw e;
        } finally {
            if (owner && session.isOpen()) {
                session.close();
            }
        }
    }

    public record CloseAccountResult(Long closedAccountId, Long targetAccountId, int transferredAmount) {
    }

    public record TransferResult(
            Long fromAccountId,
            Long toAccountId,
            int amount,
            int commissionAmount,
            int recipientAmount,
            boolean sameUser
    ) {
    }
}
