package com.data.synchronisation.springboot.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.data.synchronisation.springboot.domain.entity.CrBinanceHighLow;

public interface CrBinanceHighLowRepository extends JpaRepository<CrBinanceHighLow, Long>  {


}
