package org.example;

import org.example.service.*;
import org.example.service.handler.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
   public static void main(String[] args) {
        Map<Integer, Command> commands = new HashMap<>();
        commands.put(1, new CreateUserHandler());
        commands.put(2, new ShowUsersHandler());
        commands.put(3, new CreateAccountHandler());
        commands.put(4, new DepositHandler());
        commands.put(5, new WithdrawHandler());
        commands.put(6, new TransferHandler());
        commands.put(7, new CloseHandler());

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext("org.example");

        TransferService transferService = (TransferService) context.getBean("transferService");
        UserService userService = context.getBean(UserService.class);
        AccountService accountService = context.getBean(AccountService.class);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            int choice = ConsoleInput.readInt("Выберите пункт меню: ");
            if (choice == 0) {
                System.out.println("До свидания!");
                context.close();
                break;
            }
            Command command = commands.get(choice);
            if (command != null) {
                try {
                    command.execute(userService, accountService, transferService);
                } catch (IllegalStateException e) {
                    System.out.println("  [!] " + e.getMessage());
                }
            } else {
                System.out.println("  [!] Неверный пункт меню.");
            }

        }

    }

    private static void printMenu() {
        System.out.println("==================================================");
        System.out.println("                BANK CONSOLE SYSTEM               ");
        System.out.println("==================================================");
        System.out.println("1  | Создать пользователя");
        System.out.println("2  | Показать всех пользователей");
        System.out.println("3  | Создать счёт");
        System.out.println("4  | Пополнить счёт");
        System.out.println("5  | Снять со счёта");
        System.out.println("6  | Перевод между счетами");
        System.out.println("7  | Закрыть счёт");
        System.out.println("0  | Выход");
        System.out.println("==================================================");
    }
}