package org.example.service.handler;

import org.example.service.AccountService;
import org.example.service.Command;
import org.example.service.TransferService;
import org.example.service.UserService;

public class ShowUsersHandler implements Command {
    @Override
    public void execute(UserService userService, AccountService accountService, TransferService transferService) {
        System.out.println("Список всех пользователей:");
        userService.showAllUsers();
    }

    @Override
    public String getName() {
        return "";
    }
}
