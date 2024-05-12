package com.app.unitech.service;

import com.app.unitech.dto.response.CurrencyResponse;

public interface CurrencyService {

    CurrencyResponse getCurrencyRate(String currencyPair);

    void clearCache();

}
