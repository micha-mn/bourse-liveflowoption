package com.data.synchronisation.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.data.synchronisation.springboot.domain.entity.EthFi;
@Repository
public interface EthFiRepository extends JpaRepository<EthFi, Long> {
	
	 
	  
}
