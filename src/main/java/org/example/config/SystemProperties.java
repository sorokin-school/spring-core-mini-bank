package org.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SystemProperties {

    @Value("${account.transfer-commission}")
    private BigDecimal commission;

    @Value("${account.default-balance}")
    private BigDecimal defaultBalance;

    public BigDecimal getCommission() {
        return commission;
    }

    public BigDecimal getDefaultBalance() {
        return defaultBalance;
    }
}
