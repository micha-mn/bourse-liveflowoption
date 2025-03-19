package com.data.synchronisation.springboot.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.data.synchronisation.springboot.domain.entity.Btc;
@Repository
	
public interface BtcRepository extends JpaRepository<Btc, Long> {
	
	List<Btc> findTop2ByOrderByReferDateDesc(); 
}
