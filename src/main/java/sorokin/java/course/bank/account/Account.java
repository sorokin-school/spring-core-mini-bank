package sorokin.java.course.bank.account;

import jakarta.persistence.*;
import sorokin.java.course.users.User;

@Table(name = "accounts")
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "money_amount")
    private int moneyAmount;

    public Account() {
    }

    public Account(Long id, User user, int moneyAmount) {
        this.id = id;
        this.user = user;
        this.moneyAmount = moneyAmount;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public int getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(int moneyAmount) {
        if (moneyAmount < 0) {
            throw new IllegalArgumentException("Attempted to set moneyAmount less than 0");
        }
        this.moneyAmount = moneyAmount;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", moneyAmount=" + moneyAmount +
                '}';
    }
}

