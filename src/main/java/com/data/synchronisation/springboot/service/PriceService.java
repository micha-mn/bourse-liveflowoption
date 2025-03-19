package com.data.synchronisation.springboot.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.data.synchronisation.springboot.domain.entity.Btc;
import com.data.synchronisation.springboot.domain.entity.CrBTCHighLow;
import com.data.synchronisation.springboot.domain.entity.CrBinanceHighLow;
import com.data.synchronisation.springboot.domain.entity.CrEthereumHighLow;
import com.data.synchronisation.springboot.domain.entity.CrShibaHighLow;
import com.data.synchronisation.springboot.domain.entity.CrSolanaHighLow;
import com.data.synchronisation.springboot.domain.entity.CrXrpHighLow;
import com.data.synchronisation.springboot.dto.DataDTO;
import com.data.synchronisation.springboot.dto.GraphResponseDTO;
import com.data.synchronisation.springboot.dto.prices.BtcDTO;
import com.data.synchronisation.springboot.dto.prices.CurrencyPreviousPriceDTO;
import com.data.synchronisation.springboot.enums.CryptoSymbol;
import com.data.synchronisation.springboot.repositories.BtcRepository;
import com.data.synchronisation.springboot.repositories.CrBTCHighLowRepository;
import com.data.synchronisation.springboot.repositories.CrBinanceHighLowRepository;
import com.data.synchronisation.springboot.repositories.CrEthereumHighLowRepository;
import com.data.synchronisation.springboot.repositories.CrShibaHighLowRepository;
import com.data.synchronisation.springboot.repositories.CrSolanaHighLowRepository;
import com.data.synchronisation.springboot.repositories.CrXrpHighLowRepository;


@Service
public class PriceService {
	
	@Autowired
	private BtcRepository btcRepository;
	
	public  List<BtcDTO> getCurrentPreviousPrice() {
	
		List<Btc> resp = btcRepository.findTop2ByOrderByReferDateDesc();
		
		List<BtcDTO> dtos = resp.stream()
			    .map(BtcDTO::new)
			    .collect(Collectors.toList());
		return dtos;
		
	}
}
