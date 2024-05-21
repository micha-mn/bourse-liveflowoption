package com.data.synchronisation.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.data.synchronisation.springboot.domain.entity.Bnb;
import com.data.synchronisation.springboot.domain.entity.Btc;
import com.data.synchronisation.springboot.domain.entity.Doge;
import com.data.synchronisation.springboot.domain.entity.Ena;
import com.data.synchronisation.springboot.domain.entity.EnaTradeHistoryInfo;
import com.data.synchronisation.springboot.domain.entity.EnaTradeInfo;
import com.data.synchronisation.springboot.domain.entity.Eth;
import com.data.synchronisation.springboot.domain.entity.EthFITradeHistoryInfo;
import com.data.synchronisation.springboot.domain.entity.Saga;
import com.data.synchronisation.springboot.domain.entity.W;
@Repository
public interface EthFITradeHistoryInfoRepository extends JpaRepository<EthFITradeHistoryInfo, Long> {
	
	 
	  
}
