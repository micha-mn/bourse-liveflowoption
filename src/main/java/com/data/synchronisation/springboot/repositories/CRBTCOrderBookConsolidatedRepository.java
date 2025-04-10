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
import com.data.synchronisation.springboot.dto.OrderBookByActionObjectProjection;
@Repository
public interface CRBTCOrderBookConsolidatedRepository extends JpaRepository<CRBTCOrderBookConsolidated, Long> {
	
	 
	@Query(value = "SELECT \r\n"
			+ "    action,\r\n"
			+ "    SUM(quantity * price) / SUM(quantity) AS price,\r\n"
			+ "    SUM(quantity) AS total_volume,\r\n"
			+ "    ROUND(\r\n"
			+ "        (SUM(quantity) / (\r\n"
			+ "            SELECT SUM(quantity) \r\n"
			+ "            FROM cr_order_book_consolidated \r\n"
			+ "            WHERE refer_date >= NOW() - INTERVAL :period HOUR \r\n"
			+ "        )) * 100, \r\n"
			+ "        2\r\n"
			+ "    ) AS percentage\r\n"
			+ "FROM \r\n"
			+ "    cr_order_book_consolidated\r\n"
			+ "WHERE \r\n"
			+ "    refer_date >= NOW() - INTERVAL :period HOUR \r\n"
			+ "GROUP BY \r\n"
			+ "    action", 
    nativeQuery = true)
	 List<GraphResponseProjection> getOrderBookConsolidatedHourPeriod(@Param("period") String period);
	
	
	
	@Query(value = "SELECT \r\n"
			+ "    action,\r\n"
			+ "    SUM(quantity * price) / SUM(quantity) AS price,\r\n"
			+ "    SUM(quantity) AS total_volume,\r\n"
			+ "    ROUND(\r\n"
			+ "        (SUM(quantity) / (\r\n"
			+ "            SELECT SUM(quantity) \r\n"
			+ "            FROM cr_order_book_consolidated \r\n"
			+ "            WHERE refer_date >= NOW() - INTERVAL :period MINUTE \r\n"
			+ "        )) * 100, \r\n"
			+ "        2\r\n"
			+ "    ) AS percentage\r\n"
			+ "FROM \r\n"
			+ "    cr_order_book_consolidated\r\n"
			+ "WHERE \r\n"
			+ "    refer_date >= NOW() - INTERVAL :period MINUTE \r\n"
			+ "GROUP BY \r\n"
			+ "    action", 
    nativeQuery = true)
	 List<GraphResponseProjection> getOrderBookConsolidatedMinutePeriod(@Param("period") String period);
	
	
	
	@Query(value = "select quantity as volume,price \r\n"
			+ "      FROM cr_order_book_consolidated \r\n"
			+ "      where action = :action\r\n"
			+ "	order by refer_date desc,  price DESC \r\n"
			+ "    limit :limit", 
    nativeQuery = true)
	 List<OrderBookByActionObjectProjection> getOrderBookByActionDesc(@Param("limit") int limit,@Param("action")  String action);
	  

	@Query(value = "select quantity as volume,price \r\n"
			+ "      FROM cr_order_book_consolidated \r\n"
			+ "      where action = :action\r\n"
			+ "	order by refer_date desc,  price Asc \r\n"
			+ "    limit :limit", 
    nativeQuery = true)
	 List<OrderBookByActionObjectProjection> getOrderBookByActionAsc(@Param("limit") int limit,@Param("action")  String action);
	  
	 
}
