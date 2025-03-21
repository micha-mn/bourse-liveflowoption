package com.data.synchronisation.springboot.repositories;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.data.synchronisation.springboot.domain.LiveOptionFlow;
public interface LiveOptionFlowRepository extends JpaRepository<LiveOptionFlow, Long> {
	
	 
	   @Query(value = "SELECT \r\n"
	   		+ "  a.product, \r\n"
	   		+ "  a.id,\r\n"
	   		+ "  a.flow_date,\r\n"
	   		+ "  a.flow,\r\n"
	   		+ "  IFNULL(count_by_product, 0) AS count_by_product\r\n"
	   		+ "FROM live_option_flow_data a\r\n"
	   		+ "LEFT JOIN (\r\n"
	   		+ "  SELECT \r\n"
	   		+ "    product, \r\n"
	   		+ "    COUNT(product) AS count_by_product\r\n"
	   		+ "  FROM live_option_flow_data\r\n"
	   		+ "   where (flow_date BETWEEN :fromDate AND :toDate)  OR flow IS NULL \r\n"
	   		+ "  GROUP BY product \r\n"
	   		+ ") AS subquery\r\n"
	   		+ "ON a.product = subquery.product\r\n"
	   		+ "WHERE\r\n"
	   		+ "  (flow_date BETWEEN :fromDate AND :toDate) \r\n"
	   		+ "  OR a.flow IS NULL\r\n"
	   		+ "ORDER BY FIELD(a.product,  'BUND', 'BOBL', 'BUXL', 'SHATZ', 'OAT', 'BTP', 'EURIBOR', 'TY'), a.flow_date DESC;"
	   		,
				      nativeQuery = true)
   List<LiveOptionFlow> findLiveOptionFlowByFlowDate(@Param("fromDate") String  fromDate, @Param("toDate") String  toDate);
   
 	 
   @Query(value =  "SELECT \r\n"
   		+ "  a.product, \r\n"
   		+ "  a.id,\r\n"
   		+ "  a.flow_date,\r\n"
   		+ "  a.flow,\r\n"
   		+ "  IFNULL(count_by_product, 0) AS count_by_product\r\n"
   		+ "FROM live_option_flow_data a\r\n"
   		+ "LEFT JOIN (\r\n"
   		+ "  SELECT \r\n"
   		+ "    product, \r\n"
   		+ "    COUNT(product) AS count_by_product\r\n"
   		+ "  FROM live_option_flow_data\r\n"
   		+ "   where  product LIKE CONCAT('%', :value, '%')\r\n"
   		+ "  OR DATE_FORMAT(flow_date, '%Y-%m-%d') LIKE CONCAT('%', :value, '%')\r\n"
   		+ "  OR flow LIKE CONCAT('%', :value, '%') OR flow IS NULL \r\n"
   		+ "  GROUP BY product \r\n"
   		+ ") AS subquery\r\n"
   		+ "ON a.product = subquery.product\r\n"
   		+ "WHERE\r\n"
   		+ "  a.product LIKE CONCAT('%', :value, '%')\r\n"
   		+ "  OR DATE_FORMAT(a.flow_date, '%Y-%m-%d') LIKE CONCAT('%', :value, '%')\r\n"
   		+ "  OR a.flow LIKE CONCAT('%', :value, '%')\r\n"
   		+ "  OR a.flow IS NULL\r\n"
   		+ "ORDER BY FIELD(a.product,  'BUND', 'BOBL', 'BUXL', 'SHATZ', 'OAT', 'BTP', 'EURIBOR', 'TY'), a.flow_date DESC;\r\n"
   		,
			      nativeQuery = true)
   List<LiveOptionFlow> findAllNewsByDescription(@Param("value") String value);
   
   @Query(value = "select product, id,"
   		+ "   		  CASE "
   		+ "   		       WHEN flow_date = '3000-01-01 00:00:00' THEN NULL"
   		+ "   		   ELSE flow_date"
   		+ "   		END as flow_date ,"
   		+ "   	      flow "
   		+ " from live_option_flow_data a "
   		+ "where a.product=:product "
   		+ "and  (DATE_FORMAT(a.flow_date , '%Y-%m-%d') LIKE CONCAT('%',:value,'%') "
   		+ "or  a.flow LIKE CONCAT('%',:value,'%') or a.flow is null  ) "
   		+ "order by FIELD(a.product,  'BUND', 'BOBL', 'BUXL', 'SHATZ', 'OAT', 'BTP', 'EURIBOR', 'TY'),a.flow_date desc",
		      nativeQuery = true) 
	 List<LiveOptionFlow> findAllNewsByDescriptionAndProduct(@Param("value") String value,@Param("product") String product);
}
