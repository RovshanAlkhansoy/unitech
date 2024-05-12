package com.app.unitech.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.app.unitech.dto.response.CurrencyResponse;
import com.app.unitech.service.CurrencyRateCache;
import com.app.unitech.service.ThirdPartyCurrencyService;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CurrencyServiceImplTest {

    @Mock
    private CurrencyRateCache mockCache;

    @Mock
    private ThirdPartyCurrencyService mockThirdPartyService;

    private CurrencyServiceImpl currencyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        currencyService = new CurrencyServiceImpl(mockCache, mockThirdPartyService);
    }

    @Test
    void testGetCurrencyRate_CacheHit() {
        String currencyPair = "USD/EUR";
        BigDecimal rate = BigDecimal.valueOf(1.2);
        when(mockCache.getCurrencyRate(currencyPair)).thenReturn(rate);

        CurrencyResponse response = currencyService.getCurrencyRate(currencyPair);

        assertEquals(rate, response.getRate());
        verify(mockCache, times(1)).getCurrencyRate(currencyPair);
        verify(mockThirdPartyService, never()).fetchCurrencyRate(anyString());
        verify(mockCache, never()).setCurrencyRate(anyString(), any(BigDecimal.class));
    }

    @Test
    void testGetCurrencyRate_CacheMiss() {
        String currencyPair = "USD/EUR";
        BigDecimal rate = BigDecimal.valueOf(1.2);
        when(mockCache.getCurrencyRate(currencyPair)).thenReturn(null);
        when(mockThirdPartyService.fetchCurrencyRate(currencyPair)).thenReturn(rate);

        CurrencyResponse response = currencyService.getCurrencyRate(currencyPair);

        assertEquals(rate, response.getRate());
        verify(mockCache, times(1)).getCurrencyRate(currencyPair);
        verify(mockThirdPartyService, times(1)).fetchCurrencyRate(currencyPair);
        verify(mockCache, times(1)).setCurrencyRate(currencyPair, rate);
    }

    @Test
    void testClearCache() {
        currencyService.clearCache();

        verify(mockCache, times(1)).clear();
    }

}