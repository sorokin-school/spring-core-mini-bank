package org.example.service;

import org.example.model.Account;
import org.example.model.User;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleInput {


    private static final Scanner SCANNER = new Scanner(System.in);

    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = SCANNER.nextInt();
                SCANNER.nextLine();
                return value;
            } catch (InputMismatchException e) {
                SCANNER.nextLine();
                System.out.println("  [!] Ожидается целое число. Попробуйте ещё раз.");
            }
        }
    }
    public static int readInt(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.printf("  [!] Значение должно быть от %d до %d.%n", min, max);
        }
    }
    public static String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = SCANNER.nextLine().trim();
            if (value.isEmpty()) {
                System.out.println("  [!] Строка не может быть пустой.");
            } else if (!value.matches("[\\p{L} ]+")) {
                System.out.println("  [!] Допустимы только буквы и пробелы.");
            } else {
                return value;
            }
        }
    }
    public static BigDecimal readBigDecimal(String prompt) {
        while (true) {
            System.out.print(prompt);
            String raw = SCANNER.nextLine().trim().replace(',', '.');
            try {
                BigDecimal value = new BigDecimal(raw);
                if (value.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("  [!] Сумма должна быть больше нуля.");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("  [!] Введите корректное числовое значение (например: 100 или 99.50).");
            }
        }
    }
    public static User readExistingUser(String prompt, Map<String, User> usersMap) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("  [!] Введите имя пользователя.");
                continue;
            }
            User found = usersMap.values().stream()
                    .filter(u -> u.getName().equalsIgnoreCase(input))
                    .findFirst()
                    .orElse(null);

            if (found == null) {
                System.out.println("  [!] Пользователь «" + input + "» не найден. Попробуйте ещё раз.");
            } else {
                return found;
            }
        }
    }
    public static int readAccountIndex(String prompt, User user) {
        List<Account> accounts = user.getAccounts();
        if (accounts.isEmpty()) {
            throw new IllegalStateException("У пользователя " + user.getName() + " нет открытых счетов.");
        }
        System.out.println("  Доступные счета:");
        for (int i = 0; i < accounts.size(); i++) {
            System.out.printf("    [%d] %s%n", i, accounts.get(i));
        }
        return readInt(prompt, 0, accounts.size() - 1);
    }

    public static Account readAccountOfUser(String prompt, User user) {
        List<Account> accounts = user.getAccounts();
        if (accounts.isEmpty()) {
            throw new IllegalStateException("У пользователя " + user.getName() + " нет открытых счетов.");
        }
        System.out.println("  Счета пользователя " + user.getName() + ":");
        accounts.forEach(a -> System.out.println("    " + a));

        while (true) {
            System.out.print(prompt);
            String uuid = SCANNER.nextLine().trim();
            Account found = accounts.stream()
                    .filter(a -> a.getUuid().equals(uuid))
                    .findFirst()
                    .orElse(null);
            if (found == null) {
                System.out.println("  [!] Счёт с UUID «" + uuid + "» не найден у этого пользователя.");
            } else {
                return found;
            }
        }
    }
}
