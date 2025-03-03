package com.data.synchronisation.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.data.synchronisation.springboot.domain.entity.CRBTCOrderBook;
@Repository
public interface CRBTCOrderBookRepository extends JpaRepository<CRBTCOrderBook, Long> {
	
	 
	  
}
