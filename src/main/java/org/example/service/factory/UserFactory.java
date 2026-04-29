package org.example.service.factory;

import org.example.model.User;

public class UserFactory {
    public static User createUser(String username ,int age){
        return new User(username,age);
    }
}
