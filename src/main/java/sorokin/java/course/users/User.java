package sorokin.java.course.users;

import jakarta.persistence.*;
import sorokin.java.course.bank.account.Account;

import java.util.List;

@Table(name = "users")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "login", unique = true, updatable = false)
    private String login;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Account> accountList;

    public User() {
    }

    public User(Long id, String login, List<Account> accountList) {
        this.id = id;
        this.login = login;
        this.accountList = accountList;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", accountList=" + accountList +
                '}';
    }
}
