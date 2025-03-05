package com.data.synchronisation.springboot.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.data.synchronisation.springboot.domain.entity.CrShibaHighLow;

public interface CrShibaHighLowRepository extends JpaRepository<CrShibaHighLow, Long>  {

	 Optional<CrShibaHighLow> findTopByOrderByStartTimeStampDesc();

}
