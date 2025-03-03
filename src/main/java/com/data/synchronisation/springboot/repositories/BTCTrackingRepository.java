package com.data.synchronisation.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.data.synchronisation.springboot.domain.entity.BTCTrackingTable;
import com.data.synchronisation.springboot.domain.entity.Bnb;
import com.data.synchronisation.springboot.domain.entity.Btc;
import com.data.synchronisation.springboot.domain.entity.Doge;
import com.data.synchronisation.springboot.domain.entity.Ena;
import com.data.synchronisation.springboot.domain.entity.EnaTrackingTable;
import com.data.synchronisation.springboot.domain.entity.EthFITrackingTable;
import com.data.synchronisation.springboot.domain.entity.Saga;
import com.data.synchronisation.springboot.domain.entity.W;
@Repository
public interface BTCTrackingRepository extends JpaRepository<BTCTrackingTable, Long> {
	
	 
	  
}
