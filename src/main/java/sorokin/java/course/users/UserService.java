package sorokin.java.course.users;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import sorokin.java.course.bank.account.AccountService;

import java.util.*;

@Component
public class UserService {

    private final AccountService accountService;
    private final SessionFactory sessionFactory;

    public UserService(AccountService accountService, SessionFactory sessionFactory) {
        this.accountService = accountService;
        this.sessionFactory = sessionFactory;
    }

    public User createUser(String login) { //in one transaction to do
        var session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        try {
            User existedUser = session.createQuery("FROM User WHERE login = :login", User.class)
                    .setParameter("login", login)
                    .getSingleResultOrNull();

            if (existedUser != null) {
                throw new IllegalArgumentException("User already exists with login=" + login);
            }

            User user = new User(null, login, new ArrayList<>());
            session.persist(user);

            // Создание дефолтного аккаунта для пользо
            // вателя
//            accountService.createAccount(user);
            session.getTransaction().commit();
            return user;
        } finally {
            if (session.isOpen()) session.close();
        }
    }

    public User findUserById(Long id) {
        try (var session = sessionFactory.getCurrentSession()) {
            User user = session.get(User.class, id);
            if (user == null) {
                throw new IllegalArgumentException("No such user with id=" + id);
            }
            return user;
        }
    }

    public List<User> findAll() {
        try (var session = sessionFactory.openSession()) {
            return session.createQuery("FROM User", User.class)
                    .list();
        }
    }
}
