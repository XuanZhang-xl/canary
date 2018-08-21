package com.xl.canary.simulator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 汇率模拟器
 * created by XUAN on 2018/08/20
 */
public class ExchangeRateSimulator {

    /**
     * 获取牌价, 1个currency货币对应x个convert货币
     * @param currency
     * @param convert
     * @return
     */
    public static BigDecimal getTicker(String currency, String convert) {
        // 写死1
        return BigDecimal.ONE;
    }

    public static Map<String, BigDecimal> listTickers(List<String> currencies, String convert) {
        // 写死1
        Map<String, BigDecimal> tickers = new HashMap<String, BigDecimal>();
        for (String currency : currencies) {
            tickers.put(currency, BigDecimal.ONE);
        }
        return tickers;
    }
}
