package org.example.service;

import org.example.config.SystemProperties;
import org.example.model.Account;
import org.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class TransferService {

    private static final BigDecimal MAX_SINGLE_TRANSFER = new BigDecimal("1000000");

    private final AccountRepository accountRepository;
    private final SystemProperties systemProperties;

    @Autowired
    public TransferService(AccountRepository accountRepository, SystemProperties systemProperties) {
        this.accountRepository = accountRepository;
        this.systemProperties = systemProperties;
    }

    public boolean transfer(String fromAccountUuid, String toAccountUuid, BigDecimal amount) {


        if (!antifraudCheck(amount)) {
            System.out.println("  [!] Ошибка: сумма перевода должна быть от 0.01 до " + MAX_SINGLE_TRANSFER + ".");
            return false;
        }
        if (fromAccountUuid.equals(toAccountUuid)) {
            System.out.println("  [!] Ошибка: счёт-источник и счёт-получатель совпадают.");
            return false;
        }

        Account from = accountRepository
                .getAccount(fromAccountUuid);
        Account to   = accountRepository
                .getAccount(toAccountUuid);

        if (from == null) {
            System.out.println("  [!] Ошибка: счёт-источник «" + fromAccountUuid + "» не найден.");
            return false;
        }
        if (to == null) {
            System.out.println("  [!] Ошибка: счёт-получатель «" + toAccountUuid + "» не найден.");
            return false;
        }
        BigDecimal commission = BigDecimal.ZERO;
        boolean sameOwner = from.getOwnerUUID().equals(to.getOwnerUUID());
        if (!sameOwner) {
            commission = amount
                    .multiply(systemProperties.getCommission())
                    .setScale(2, RoundingMode.HALF_UP);
        }
        BigDecimal totalDeducted = amount.add(commission);
        if (from.getBalance().compareTo(totalDeducted) < 0) {
            System.out.printf("  [!] Ошибка: недостаточно средств. " +
                    "Баланс: %.2f, требуется: %.2f (сумма %.2f + комиссия %.2f).%n",
                    from.getBalance(), totalDeducted, amount, commission);
            return false;
        }

        from.setBalance(from.getBalance().subtract(totalDeducted));
        to.setBalance(to.getBalance().add(amount));

        System.out.printf("  [OK] Перевод выполнен: %.2f → счёт %s%n", amount, toAccountUuid);
        if (commission.compareTo(BigDecimal.ZERO) > 0) {
            System.out.printf("       Комиссия (%.0f%%): %.2f%n",
                    systemProperties.getCommission().multiply(BigDecimal.valueOf(100)), commission);
        }
        System.out.printf("       Новый баланс отправителя: %.2f%n", from.getBalance());
        System.out.printf("       Новый баланс получателя:  %.2f%n", to.getBalance());

        return true;
    }

    private boolean antifraudCheck(BigDecimal amount) {
        if (amount == null) return false;
        if (amount.compareTo(BigDecimal.ZERO) <= 0) return false;
        if (amount.compareTo(MAX_SINGLE_TRANSFER) > 0) return false;
        return true;
    }
}
