package com.data.synchronisation.springboot.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.data.synchronisation.springboot.domain.entity.CrBinanceHighLow;
import com.data.synchronisation.springboot.domain.entity.CrXrpHighLow;

public interface CrBinanceHighLowRepository extends JpaRepository<CrBinanceHighLow, Long>  {

	 Optional<CrBinanceHighLow> findTopByOrderByStartTimeStampDesc();

}
