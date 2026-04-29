package org.example.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;


public class User {
    private final String id;
    private final String name;
    private int age;
    private ArrayList<Account> accounts = new ArrayList<>() ;
    private UserState userState;

    public User(String name, int age) {
        this.id = name + ThreadLocalRandom.current().nextInt(10000);
        this.name = name;
        this.age = age;
        this.userState = UserState.UNKNOWN;
    }

    public void setAccounts(ArrayList<Account> accounts) {

        this.accounts = accounts;
    }
    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    public UserState getUserState() {
        return userState;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String  getId() {
        return id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", accounts=" + accounts +
                ", userState=" + userState +
                '}';
    }
}
