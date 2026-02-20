package sorokin.java.course.user;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.stereotype.Component;
import sorokin.java.course.account.AccountService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Component
public class UserService {

    private final AccountService accountService;
    private final SessionFactory sessionFactory;

    public UserService(AccountService accountService, SessionFactory sessionFactory) {
        this.accountService = accountService;
        this.sessionFactory = sessionFactory;
    }

    public User createUser(String login) {
        String normalizedLogin = validateLogin(login);

        return executeInTransactionOrJoin(() -> {
            Session session = sessionFactory.getCurrentSession();
            User existedUser = session.createQuery("FROM User WHERE login = :login", User.class)
                    .setParameter("login", normalizedLogin)
                    .getSingleResultOrNull();

            if (existedUser != null) {
                throw new IllegalArgumentException("User already exists with login=%s".formatted(normalizedLogin));
            }

            User user = new User(null, normalizedLogin, new ArrayList<>());
            session.persist(user);
            accountService.createAccount(user.getId());
            return user;
        });
    }

    public User findUserById(Long id) {
        validatePositiveId(id, "user id");
        return executeInTransactionOrJoin(() -> {
            Session session = sessionFactory.getCurrentSession();
            User user = session.get(User.class, id);
            if (user == null) {
                throw new IllegalArgumentException("No such user with id=%s".formatted(id));
            }
            return user;
        });
    }

    public List<User> findAll() {
        return executeInTransactionOrJoin(() -> {
            Session session = sessionFactory.getCurrentSession();
            return session.createQuery("FROM User", User.class).list();
        });
    }

    private String validateLogin(String login) {
        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("login must not be blank");
        }
        return login.trim();
    }

    private void validatePositiveId(Long id, String fieldName) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(fieldName + " must be > 0");
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
}
