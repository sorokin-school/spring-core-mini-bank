package org.example.service.handler;

import org.example.model.User;
import org.example.service.AccountService;
import org.example.service.Command;
import org.example.service.ConsoleInput;
import org.example.service.TransferService;
import org.example.service.UserService;

public class CreateUserHandler implements Command {

    @Override
    public void execute(UserService userService, AccountService accountService, TransferService transferService) {
        String name = ConsoleInput.readString("Введите имя пользователя: ");
        int age = ConsoleInput.readInt("Введите возраст: ", 1, 120);

        User user = userService.createUser(name, age);
        System.out.println("Пользователь создан: " + user);
    }

    @Override
    public String getName() { return "Создать пользователя"; }
}
