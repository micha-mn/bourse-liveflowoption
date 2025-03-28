package com.data.synchronisation.springboot.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.data.synchronisation.springboot.domain.entity.CrEthereumHighLow;

public interface CrEthereumHighLowRepository extends JpaRepository<CrEthereumHighLow, Long>  {

	 Optional<CrEthereumHighLow> findTopByOrderByStartTimeStampDesc();

}
