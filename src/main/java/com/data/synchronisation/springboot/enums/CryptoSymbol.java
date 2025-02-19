package com.data.synchronisation.springboot.enums;

import lombok.Getter;

@Getter
public enum CryptoSymbol {
	BTCUSDT("cr_btc_high_low", "71"),
    ETHUSDT("cr_ethereum_high_low", "72"),
    SOLUSDT("cr_solana_high_low", "73"),
    SHIBUSDT("cr_shiba_high_low", "74"),
    BNBUSDT("cr_binance_high_low", "75"),
    XRPUSDT("cr_xrp_high_low", "76");

    private final String tableName;
    private final String groupId;

    CryptoSymbol(String tableName, String groupId) {
        this.tableName = tableName;
        this.groupId = groupId;
    }

    public static CryptoSymbol fromString(String symbol) {
        for (CryptoSymbol cs : CryptoSymbol.values()) {
            if (cs.name().equalsIgnoreCase(symbol)) {
                return cs;
            }
        }
        throw new IllegalArgumentException("Unknown symbol: " + symbol);
    }
}
