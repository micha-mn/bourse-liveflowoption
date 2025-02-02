

USE `bourse`;
DROP procedure IF EXISTS `dynamic_calculation_candlestick_graph_four_hour_interval`;

USE `bourse`;
DROP procedure IF EXISTS `bourse`.`dynamic_calculation_candlestick_graph_four_hour_interval`;
;
DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `dynamic_calculation_candlestick_graph_four_hour_interval`(
																		 IN fromDate VARCHAR(255),
																		 IN toDateDate VARCHAR(255),
																		 IN tableName VARCHAR(255),
                                                                         IN column1 VARCHAR(255),
                                                                         IN column2 VARCHAR(255),
                                                                         IN column3 VARCHAR(255),
                                                                         IN column4 VARCHAR(255))
BEGIN
DECLARE sqlTxt varchar(255);
	SET GLOBAL sql_mode = '';
	SET SESSION sql_mode = '';  
    
	 SET @sql_text:= CONCAT('  SELECT (@row_number:=@row_number + 1) AS id, 
	    DATE_FORMAT(start_time, ''%d-%b-%y %h:%i'') AS x, 
	   JSON_ARRAY(open, high, low, close) AS y  from (
			SELECT 
			  DATE(start_time) AS trade_date,
			  -- This expression computes the period start hour (0, 4, 8, …)
			  CONCAT(LPAD(FLOOR(HOUR(start_time)/4)*4, 2, ''0''), '':00:00'') AS period,
			  MIN(start_time) AS start_time,
			  MAX(end_time) AS end_time,
			  
			  -- Get the open value from the first record of the group
			  (SELECT open 
			   FROM `',tableName,'` t2 
			   WHERE t2.start_time = MIN(t1.start_time)
			   LIMIT 1) AS open,
			  
			  -- Get the close value from the last record of the group
			  (SELECT close 
			   FROM  `',tableName,'`  t3 
			   WHERE t3.end_time = MAX(t1.end_time)
			   LIMIT 1) AS close,
			  
			  MAX(high) AS high,
			  MIN(low) AS low,
			  SUM(volume) AS volume,
			  (SELECT marketcap
			FROM  `',tableName,'`  t
			WHERE t.end_time <= MAX(t1.end_time)
			  AND marketcap <> 0
			ORDER BY t.end_time DESC
			LIMIT 1) AS marketcap
			FROM  `',tableName,'`  t1
			GROUP BY trade_date, period
			ORDER BY trade_date, period)t  
					WHERE start_time BETWEEN ''',fromDate,''' and ''',toDateDate,'''
					ORDER BY  start_time  ASC; ');
          
    PREPARE stmt from @sql_text;
	EXECUTE stmt;  
	
END$$

DELIMITER ;




-- ---------------------------------------------------
