package com.xl.canary.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 区块链货币种类枚举
 * Created by xzhang on 2018/3/28.
 */
public enum CurrencyEnum {
    BTC(),
    ETH(),
    USDT(),
    DAI(),

    ;

    public static List<String> getAllCurrencies() {
        List<String> codes = new ArrayList<String>();
        CurrencyEnum[] currencies = CurrencyEnum.values();
        for (CurrencyEnum currency : currencies) {
            codes.add(currency.name());
        }
        return codes;
    }
}
