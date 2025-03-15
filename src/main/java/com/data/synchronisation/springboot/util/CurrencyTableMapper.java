package com.data.synchronisation.springboot.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CurrencyTableMapper {
private static final Map<String, String> CURRENCY_MAP;

static {
    Map<String, String> currencyMap = new HashMap<>();
    currencyMap.put("BTC", "cr_btc_high_low");
    currencyMap.put("ETH", "cr_ethereum_high_low");
    currencyMap.put("SOL", "cr_solana_high_low");
    currencyMap.put("SHIB", "cr_shiba_high_low");
    currencyMap.put("BNB", "cr_binance_high_low");
    currencyMap.put("XRP", "cr_xrp_high_low"); // Fixed: it shouldn't be "cr_binance_high_low"
    CURRENCY_MAP = Collections.unmodifiableMap(currencyMap);
}

public static String getTableName(String currencyCode) {
    return CURRENCY_MAP.get(currencyCode);
}
}