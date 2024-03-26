package sorokin.java.course.users;

import org.springframework.stereotype.Component;
import sorokin.java.course.bank.account.AccountService;

import java.util.*;

@Component
public class UserService {

    private int idCounter;
    private final Map<Integer, User> userMap;
    private final Set<String> takenLogins;
    private final AccountService accountService;

    public UserService(AccountService accountService) {
        this.idCounter = 0;
        this.userMap = new HashMap<>();
        this.takenLogins = new HashSet<>();
        this.accountService = accountService;
    }

    public User createUser(String login) {
        if (takenLogins.contains(login)) {
            throw new IllegalArgumentException("User already exists with login=%s".formatted(login));
        }

        idCounter++;
        var user = new User(idCounter, login, new ArrayList<>());
        var defaultAccount = accountService.createAccount(user);
        user.getAccountList().add(defaultAccount);

        userMap.put(idCounter, user);
        takenLogins.add(login);
        return user;
    }

    public User findUserById(Integer id) {
        var user = userMap.get(id);
        if (user == null) {
            throw new IllegalArgumentException("No such user with id=%s".formatted(id));
        }
        return user;
    }

    public List<User> findAll() {
        return userMap.values().stream().toList();
    }
}
