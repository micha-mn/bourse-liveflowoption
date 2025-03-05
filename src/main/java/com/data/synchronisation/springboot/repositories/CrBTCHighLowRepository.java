package com.data.synchronisation.springboot.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.data.synchronisation.springboot.domain.entity.CrBTCHighLow;

public interface CrBTCHighLowRepository extends JpaRepository<CrBTCHighLow, Long>  {

	 Optional<CrBTCHighLow> findTopByOrderByStartTimeStampDesc();
}
