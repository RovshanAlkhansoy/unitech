package com.app.unitech.service.impl;

import com.app.unitech.dto.response.CurrencyResponse;
import com.app.unitech.service.CurrencyRateCache;
import com.app.unitech.service.CurrencyService;
import com.app.unitech.service.ThirdPartyCurrencyService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRateCache cache;
    private final ThirdPartyCurrencyService thirdPartyService;


    public CurrencyResponse getCurrencyRate(String currencyPair) {
        BigDecimal rate = cache.getCurrencyRate(currencyPair);
        if (rate == null) {
            rate = thirdPartyService.fetchCurrencyRate(currencyPair);
            cache.setCurrencyRate(currencyPair, rate);
        }
        return CurrencyResponse.builder()
                .rate(rate)
                .build();

    }

    @Scheduled(fixedDelay = 59000)
    @Override
    public void clearCache() {
        cache.clear();
    }

}
