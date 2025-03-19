package com.data.synchronisation.springboot.dto.orderBook;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.data.synchronisation.springboot.dto.OrderBookByActionObjectProjection;
import com.data.synchronisation.springboot.dto.prices.CurrencyPreviousPriceDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor 
@AllArgsConstructor
public class OrderBookDataDTO {
	
	private OrderBookBidAskListDTO orderBookDataBidAsk;
	private OrderBookPercentageResponseDTO orderBookPercentage;
	private CurrencyPreviousPriceDTO currencyPreviousPriceDTO;

}
