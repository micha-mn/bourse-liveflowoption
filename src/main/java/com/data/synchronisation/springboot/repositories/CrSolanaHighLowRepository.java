package com.data.synchronisation.springboot.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.data.synchronisation.springboot.domain.entity.CrSolanaHighLow;

public interface CrSolanaHighLowRepository extends JpaRepository<CrSolanaHighLow, Long>  {

	 Optional<CrSolanaHighLow> findTopByOrderByStartTimeStampDesc();

}
