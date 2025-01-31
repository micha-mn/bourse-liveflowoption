package com.data.synchronisation.springboot.repositories;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.data.synchronisation.springboot.domain.entity.CrBitcoinHighLow;

public interface CrBitcoinHighLowRepository extends JpaRepository<CrBitcoinHighLow, Long>  {


}
