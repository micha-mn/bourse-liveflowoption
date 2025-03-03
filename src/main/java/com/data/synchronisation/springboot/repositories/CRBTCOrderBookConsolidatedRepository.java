package com.data.synchronisation.springboot.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.data.synchronisation.springboot.domain.entity.CRBTCOrderBook;
import com.data.synchronisation.springboot.domain.entity.CRBTCOrderBookConsolidated;
import com.data.synchronisation.springboot.dto.GraphResponseDTO;
import com.data.synchronisation.springboot.dto.GraphResponseProjection;
@Repository
public interface CRBTCOrderBookConsolidatedRepository extends JpaRepository<CRBTCOrderBookConsolidated, Long> {
	
	 
	@Query(value = "SELECT \r\n"
			+ "  action, \r\n"
			+ "  AVG(price) AS price,\r\n"
			+ "  SUM(quantity) AS total_quantity,\r\n"
			+ "  (SUM(quantity) / (\r\n"
			+ "    SELECT SUM(quantity) \r\n"
			+ "    FROM cr_order_book_consolidated \r\n"
			+ "    WHERE action IN ('sell', 'buy')\r\n"
			+ "      AND refer_date >= NOW() - INTERVAL :minutes MINUTE\r\n"
			+ "  ) * 100) AS percentage\r\n"
			+ "FROM cr_order_book_consolidated\r\n"
			+ "WHERE action IN ('sell', 'buy')\r\n"
			+ "  AND refer_date >= NOW() - INTERVAL :minutes MINUTE\r\n"
			+ "GROUP BY action ", 
    nativeQuery = true)
	 List<GraphResponseProjection> getOrderBookConsolidated(@Param("minutes") int minutes);
	  
}
