package org.example.service;


public interface Command {
    void execute(UserService userService, AccountService accountService, TransferService transferService);
    String getName();
}
