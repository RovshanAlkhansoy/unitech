package com.app.unitech.service.impl;

import com.app.unitech.service.ThirdPartyCurrencyService;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class MockThirdPartyCurrencyService implements ThirdPartyCurrencyService {

    private static final String USD_AZN_PAIR = "USD/AZN";
    private static final String AZN_TL_PAIR = "AZN/TL";

    @Override
    public BigDecimal fetchCurrencyRate(String currencyPair) {

        if (USD_AZN_PAIR.equals(currencyPair)) {
            return BigDecimal.valueOf(1.7);
        } else if (AZN_TL_PAIR.equals(currencyPair)) {
            return BigDecimal.valueOf(8);
        } else {

            throw new IllegalArgumentException("Unknown currency pair: " + currencyPair);
        }
    }

}
