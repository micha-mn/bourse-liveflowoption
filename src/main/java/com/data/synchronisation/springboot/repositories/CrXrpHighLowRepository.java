package com.data.synchronisation.springboot.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.data.synchronisation.springboot.domain.entity.CrXrpHighLow;

public interface CrXrpHighLowRepository extends JpaRepository<CrXrpHighLow, Long>  {
	 Optional<CrXrpHighLow> findTopByOrderByStartTimeStampDesc();

}
