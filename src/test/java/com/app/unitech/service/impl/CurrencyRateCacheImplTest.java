package com.app.unitech.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CurrencyRateCacheImplTest {

    private CurrencyRateCacheImpl currencyRateCache;

    @BeforeEach
    void setUp() {
        currencyRateCache = new CurrencyRateCacheImpl();
    }

    @Test
    void testGetCurrencyRate_ExistingPair() {
        String currencyPair = "USD/EUR";
        BigDecimal rate = BigDecimal.valueOf(1.2);
        currencyRateCache.setCurrencyRate(currencyPair, rate);

        BigDecimal retrievedRate = currencyRateCache.getCurrencyRate(currencyPair);

        assertEquals(rate, retrievedRate);
    }

    @Test
    void testGetCurrencyRate_NonExistingPair() {
        String currencyPair = "GBP/USD";

        BigDecimal retrievedRate = currencyRateCache.getCurrencyRate(currencyPair);

        assertNull(retrievedRate);
    }

    @Test
    void testSetCurrencyRate() {
        String currencyPair = "EUR/JPY";
        BigDecimal rate = BigDecimal.valueOf(130.5);

        currencyRateCache.setCurrencyRate(currencyPair, rate);

        assertEquals(rate, currencyRateCache.getCurrencyRate(currencyPair));
    }

    @Test
    void testClear() {
        currencyRateCache.setCurrencyRate("USD/EUR", BigDecimal.valueOf(1.2));
        currencyRateCache.setCurrencyRate("EUR/JPY", BigDecimal.valueOf(130.5));

        currencyRateCache.clear();

        assertNull(currencyRateCache.getCurrencyRate("USD/EUR"));
        assertNull(currencyRateCache.getCurrencyRate("EUR/JPY"));
    }
}