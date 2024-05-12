package com.app.unitech.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MockThirdPartyCurrencyServiceTest {

    @Test
    void testFetchCurrencyRate_USD_AZN() {
        // Given
        MockThirdPartyCurrencyService currencyService = new MockThirdPartyCurrencyService();
        String currencyPair = "USD/AZN";

        // When
        BigDecimal rate = currencyService.fetchCurrencyRate(currencyPair);

        // Then
        assertEquals(BigDecimal.valueOf(1.7), rate);
    }

    @Test
    void testFetchCurrencyRate_AZN_TL() {
        // Given
        MockThirdPartyCurrencyService currencyService = new MockThirdPartyCurrencyService();
        String currencyPair = "AZN/TL";

        // When
        BigDecimal rate = currencyService.fetchCurrencyRate(currencyPair);

        // Then
        assertEquals(BigDecimal.valueOf(8), rate);
    }

    @Test
    void testFetchCurrencyRate_UnknownPair() {
        // Given
        MockThirdPartyCurrencyService currencyService = new MockThirdPartyCurrencyService();
        String unknownCurrencyPair = "EUR/USD";

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> currencyService.fetchCurrencyRate(unknownCurrencyPair));
    }

}