
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
	    DATE_FORMAT(start_time, ''%d-%b-%y %H:%i'') AS x, 
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
			ORDER BY trade_date, period)t1  ,
        (SELECT @row_number:=0) AS t 
					WHERE start_time BETWEEN ''',fromDate,''' and ''',toDateDate,'''
					ORDER BY  start_time  ASC; ');
          
    PREPARE stmt from @sql_text;
	EXECUTE stmt;  
	
END$$

 -- -------------------------------------
 


USE `bourse`;
DROP procedure IF EXISTS `cr_dyanamic_calculation_graph`;

USE `bourse`;
DROP procedure IF EXISTS `bourse`.`cr_dyanamic_calculation_graph`;
;
DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `cr_dyanamic_calculation_graph`(
																		 IN fromDate VARCHAR(255),
																		 IN toDate VARCHAR(255),
																		 IN tableName VARCHAR(255),
                                                                         IN columnName VARCHAR(255),
                                                                         IN factor VARCHAR(255),
																		 IN dayOrweek VARCHAR(1))
BEGIN
DECLARE sqlTxt varchar(255);
	SET GLOBAL sql_mode = '';
	SET SESSION sql_mode = '';  
    
	 SET @sql_text:= CONCAT(' select  (@row_number:=@row_number + 1) AS id,
					  DATE_FORMAT(STR_TO_DATE(refer_date,''%d-%m-%Y''), ''%d-%b-%y'') as x,
						replace(',columnName,',''%'','''') as y
			      from (
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
			ORDER BY trade_date, period)t1  ,
        (SELECT @row_number:=0) AS t 
					WHERE start_time BETWEEN ''',fromDate,''' and ''',toDate,'''
					ORDER BY  start_time  ASC; ');
    PREPARE stmt from @sql_text;
	EXECUTE stmt;  
	
END$$

DELIMITER ;




-- new ---------------------------------------------------
USE `bourse`;
DROP PROCEDURE IF EXISTS `cr_dynamic_calculation_candlestick_graph`;

DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `cr_dynamic_calculation_candlestick_graph`(
    IN fromDate VARCHAR(255),
    IN toDateDate VARCHAR(255),
    IN tableName VARCHAR(255),
    IN period VARCHAR(10)
)
BEGIN
    DECLARE interval_seconds INT;
    DECLARE sqlTxt VARCHAR(5000);

    -- Setting SQL mode
    SET GLOBAL sql_mode = '';
    SET SESSION sql_mode = '';  

    -- Convert period to seconds for grouping
    CASE period
        WHEN '1m'  THEN SET interval_seconds = 60;
        WHEN '15m' THEN SET interval_seconds = 900;
        WHEN '1h'  THEN SET interval_seconds = 3600;
        WHEN '4h'  THEN SET interval_seconds = 14400;
        WHEN '1d'  THEN SET interval_seconds = 86400;
        ELSE SET interval_seconds = 3600; -- Default to 1 hour if invalid period
    END CASE;
    if(period='1m') then 
       SET @sql_text = CONCAT(
        'SELECT (@row_number:=@row_number + 1) AS id, 
            DATE_FORMAT(start_time, ''%d-%b-%y %H:%i'') AS x, 
            JSON_ARRAY(open, high, low, close) AS y  
        FROM  `', tableName, '` t1, 
        (SELECT @row_number:=0) AS t 
        WHERE start_time BETWEEN ''', fromDate, ''' AND ''', toDateDate, '''
        ORDER BY start_time ASC;'
    );
    else
    SET @sql_text = CONCAT(
        'SELECT (@row_number:=@row_number + 1) AS id, 
            DATE_FORMAT(start_time, ''%d-%b-%y %H:%i'') AS x, 
            JSON_ARRAY(open, high, low, close) AS y  
        FROM (
            SELECT 
                DATE(start_time) AS trade_date,
                FROM_UNIXTIME(FLOOR(UNIX_TIMESTAMP(start_time) / ', interval_seconds, ') * ', interval_seconds, ') AS period_start,
                MIN(start_time) AS start_time,
                MAX(end_time) AS end_time,
                -- Open Price from first row in interval
                (SELECT open FROM `', tableName, '` t2 
                 WHERE t2.start_time = MIN(t1.start_time) LIMIT 1) AS open,
                -- Close Price from last row in interval
                (SELECT close FROM `', tableName, '` t3 
                 WHERE t3.end_time = MAX(t1.end_time) LIMIT 1) AS close,
                MAX(high) AS high,
                MIN(low) AS low
            FROM `', tableName, '` t1
            GROUP BY trade_date, period_start
            ORDER BY trade_date, period_start
        ) t1,
        (SELECT @row_number:=0) AS t 
        WHERE start_time BETWEEN ''', fromDate, ''' AND ''', toDateDate, '''
        ORDER BY start_time ASC;'
    );
	end if;
    -- Prepare and Execute Statement
    PREPARE stmt FROM @sql_text;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END$$

DELIMITER ;






