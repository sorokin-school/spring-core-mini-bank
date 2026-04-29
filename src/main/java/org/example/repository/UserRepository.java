package org.example.repository;

import org.example.model.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;


@Repository
public class UserRepository {
    private HashMap<String, User> users;
    public UserRepository() {
        users = new HashMap<>();

    }
    public HashMap<String, User> getUsers() {
        return users;
    }
    public void setUsers(HashMap<String, User> users) {
        this.users = users;
    }

}
