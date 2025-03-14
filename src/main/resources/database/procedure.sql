
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




--  ---------------------------------------------------
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
        WHEN '5m' THEN SET interval_seconds = 300;
        WHEN '15m' THEN SET interval_seconds = 900;
        WHEN '1h'  THEN SET interval_seconds = 3600;
        WHEN '4h'  THEN SET interval_seconds = 14400;
        WHEN '1d'  THEN SET interval_seconds = 86400;
        ELSE SET interval_seconds = 3600; -- Default to 1 hour if invalid period
    END CASE;
    
    SET @sql_text = CONCAT(
  'WITH grouped_data AS (
    SELECT 
        FROM_UNIXTIME(FLOOR(UNIX_TIMESTAMP(start_time) /  ', interval_seconds, ') *  ', interval_seconds, ') AS time_interval,
        MIN(low) AS min_low,
        MAX(high) AS max_high,
        MIN(start_time) AS first_start_time, -- Used for Open price
        MAX(start_time) AS last_start_time   -- Used for Close price
    FROM `', tableName, '`
    WHERE start_time BETWEEN ''', fromDate, ''' AND ''', toDateDate, '''
    GROUP BY time_interval
)
SELECT 
   (@row_number:=@row_number + 1) AS id, 
   DATE_FORMAT(time_interval, ''%d-%b-%y %H:%i'') AS x, 
   JSON_ARRAY(open, max_high, min_low, close) AS y from( 
   select o.open, g.max_high, g.min_low, c.close,g.time_interval
FROM grouped_data g
LEFT JOIN `', tableName, '` o ON g.first_start_time = o.start_time  -- Fetch Open price
LEFT JOIN `', tableName, '` c ON g.last_start_time = c.start_time   -- Fetch Close price
CROSS JOIN (SELECT @row_number:=0) AS t  -- Row numbering
ORDER BY g.time_interval ASC)tab;'
    );
    
    
    -- Prepare and Execute Statement
    PREPARE stmt FROM @sql_text;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END$$

DELIMITER ;

-- new -------------------------------------------
USE `bourse`;
DROP PROCEDURE IF EXISTS `cr_dynamic_calculation_volume_graph`;

DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `cr_dynamic_calculation_volume_graph`(
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
    WHEN '5m' THEN
      SET @period_expression = CONCAT(
        'CONCAT(',
        '  DATE(start_time), '' '',',
        '  LPAD(HOUR(start_time), 2, ''0''), '':'',',
        '  LPAD(FLOOR(MINUTE(start_time)/5)*5, 2, ''0'')',
        ')'
      );

    WHEN '15m' THEN
      SET @period_expression = CONCAT(
        'CONCAT(',
        '  DATE(start_time), '' '',',
        '  LPAD(HOUR(start_time), 2, ''0''), '':'',',
        '  LPAD(FLOOR(MINUTE(start_time)/15)*15, 2, ''0'')',
        ')'
      );

    WHEN '1h'  THEN 
      SET @period_expression = CONCAT(
        'CONCAT(',
        '  DATE(start_time), '' '',',
        '  LPAD(HOUR(start_time), 2, ''0''), '':00:00''',
        ')'
      );

    WHEN '4h'  THEN
      SET @period_expression = CONCAT(
        'CONCAT(',
        '  DATE(start_time), '' '',',
        '  LPAD(FLOOR(HOUR(start_time)/4)*4, 2, ''0''), '':00:00''',
        ')'
      );

    WHEN '1d'  THEN SET @period_expression = 'DATE(start_time)';
    ELSE SET @period_expression = 'DATE(start_time)';  -- Default to daily grouping
END CASE;

if(period='1m') then 
       SET @sql_text = CONCAT(
        'SELECT (@row_number:=@row_number + 1) AS id, 
           start_time AS x, 
		   volume AS y 
        FROM  `', tableName, '` t1, 
        (SELECT @row_number:=0) AS t 
        WHERE start_time BETWEEN ''', fromDate, ''' AND ''', toDateDate, '''
        ORDER BY start_time ASC;'
    );
    else
    SET @sql_text = CONCAT(
        'SELECT (@row_number:=@row_number + 1) AS id, 
			  start_time AS x, 
			  volume AS y 
        FROM (
            SELECT 
                DATE(start_time) AS trade_date,
                ', @period_expression, ' AS period_start,
                 MIN(start_time) AS start_time,
               sum(volume) as volume
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
-- -------------------------------------------
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
    WHEN '5m' THEN
      SET @period_expression = CONCAT(
        'CONCAT(',
        '  DATE(start_time), '' '',',
        '  LPAD(HOUR(start_time), 2, ''0''), '':'',',
        '  LPAD(FLOOR(MINUTE(start_time)/5)*5, 2, ''0'')',
        ')'
      );

    WHEN '15m' THEN
      SET @period_expression = CONCAT(
        'CONCAT(',
        '  DATE(start_time), '' '',',
        '  LPAD(HOUR(start_time), 2, ''0''), '':'',',
        '  LPAD(FLOOR(MINUTE(start_time)/15)*15, 2, ''0'')',
        ')'
      );

    WHEN '1h'  THEN 
      SET @period_expression = CONCAT(
        'CONCAT(',
        '  DATE(start_time), '' '',',
        '  LPAD(HOUR(start_time), 2, ''0''), '':00:00''',
        ')'
      );

    WHEN '4h'  THEN
      SET @period_expression = CONCAT(
        'CONCAT(',
        '  DATE(start_time), '' '',',
        '  LPAD(FLOOR(HOUR(start_time)/4)*4, 2, ''0''), '':00:00''',
        ')'
      );

    WHEN '1d'  THEN SET @period_expression = 'DATE(start_time)';
    ELSE SET @period_expression = 'DATE(start_time)';  -- Default to daily grouping
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
		SET @sql_text = CONCAT(  'WITH grouped_data AS ( SELECT 
		  ', @period_expression, ' AS time_interval,
				MIN(low) AS min_low,
				MAX(high) AS max_high,
				MIN(start_time) AS first_start_time,  -- Used for Open price
				MAX(end_time) AS last_end_time   -- Used for Close price
				-- sum(volume) as volume
			FROM  `', tableName, '`
			WHERE 
			start_time BETWEEN ''', fromDate, ''' AND ''', toDateDate, '''
			GROUP BY time_interval
		)
		 select (@row_number:=@row_number + 1) AS id, tab.*
		   from( 
         select   DATE_FORMAT(g.first_start_time, ''%d-%b-%y %H:%i'') AS x, 
            JSON_ARRAY(o.open , max_high, min_low, c.close) AS y  
		FROM grouped_data g
		LEFT JOIN  `', tableName, '` o ON g.first_start_time = o.start_time  -- Fetch Open price
		LEFT JOIN  `', tableName, '` c ON g.last_end_time = c.end_time   -- Fetch Close price
		ORDER BY g.time_interval ASC) tab
		 CROSS JOIN (SELECT @row_number:=0) AS t ;  '
			);
	end if;
    -- Prepare and Execute Statement
    PREPARE stmt FROM @sql_text;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END$$

DELIMITER ;



-- -------------------------------------------
USE `bourse`;
DROP PROCEDURE IF EXISTS `cr_data_for_graph`;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `cr_data_for_graph`(IN cryptoCurrency VARCHAR(255),
																 IN fromDate varchar(255),
																 IN toDate varchar(255),
																 IN period int,
                                                                 IN currencytype varchar(255))
BEGIN

DECLARE sqlTxt varchar(255);
SET GLOBAL sql_mode = '';
SET SESSION sql_mode = '';  

if cryptoCurrency='ENNA' then
  if (currencytype = 'NORMAL') then
	 select id ,UNIX_TIMESTAMP(refer_date) as x,value as y
	  from cr_ena
	  where refer_date between fromDate and toDate;
  ELSEIF (currencytype ='MAX') then	 
       select id, refer_date_max_timestamp as x,max_price as y
         from cr_ena_max_min
       where refer_date_max between fromDate and toDate;
   ELSEIF currencytype='MIN' then	 
         select id, refer_date_min_timestamp as x,min_price as y
           from cr_ena_max_min
          where refer_date_min between fromDate and toDate;
   end if;
end if;     

if cryptoCurrency='ETHFI' then
  if (currencytype = 'NORMAL') then
	 select id ,UNIX_TIMESTAMP(refer_date) as x,value as y
	  from cr_ethfi
	  where refer_date between fromDate and toDate;
  ELSEIF (currencytype ='MAX') then	 
       select id, refer_date_max_timestamp as x,max_price as y
         from cr_ethfi_max_min
       where refer_date_max between fromDate and toDate;
   ELSEIF currencytype='MIN' then	 
         select id, refer_date_min_timestamp as x,min_price as y
           from cr_ethfi_max_min
          where refer_date_min between fromDate and toDate;
   end if;
end if;     


if cryptoCurrency='BTC' then
  if (currencytype = 'NORMAL') then
	 select id ,UNIX_TIMESTAMP(refer_date) as x,value as y
	  from cr_BTC
	  where refer_date between fromDate and toDate;
  ELSEIF (currencytype ='MAX') then	 
       select id, refer_date_max_timestamp as x,max_price as y
         from cr_ethfi_max_min
       where refer_date_max between fromDate and toDate;
   ELSEIF currencytype='MIN' then	 
         select id, refer_date_min_timestamp as x,min_price as y
           from cr_ethfi_max_min
          where refer_date_min between fromDate and toDate;
   end if;
end if;   
END$$

DELIMITER ;

-- new 14-03-2025

USE `bourse`;
DROP procedure IF EXISTS `cr_dynamic_result`;
;
DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `cr_dynamic_result`(
																		 IN fromDate VARCHAR(255),
																		 IN toDate VARCHAR(255),
																		 IN tableName VARCHAR(255),
                                                                         IN criteria  VARCHAR(255),
                                                                         IN period  VARCHAR(10),
																		 IN pageSize INT,  
																		 IN pageNumber INT,
																		 OUT totalRecords INT  -- Declare OUT parameter properly
																	  )
BEGIN

DECLARE offsetValue INT;
DECLARE sqlTxt varchar(255);
DECLARE sqlTxtFinal varchar(255);

	SET GLOBAL sql_mode = '';
	SET SESSION sql_mode = '';  
    
   SET offsetValue = pageSize * pageNumber;
   
       -- Convert period to seconds for grouping
 CASE period
    WHEN '5m' THEN
      SET @period_expression = CONCAT(
        'CONCAT(',
        '  DATE(DATE_FORMAT(CONVERT_TZ(FROM_UNIXTIME(start_timestamp / 1000), @@session.time_zone, ''+00:00''), ''%Y-%m-%d %H:%i:%s'')), '' '',',
        '  LPAD(HOUR(DATE_FORMAT(CONVERT_TZ(FROM_UNIXTIME(start_timestamp / 1000), @@session.time_zone, ''+00:00''), ''%Y-%m-%d %H:%i:%s'')), 2, ''0''), '':'',',
        '  LPAD(FLOOR(MINUTE(DATE_FORMAT(CONVERT_TZ(FROM_UNIXTIME(start_timestamp / 1000), @@session.time_zone, ''+00:00''), ''%Y-%m-%d %H:%i:%s''))/5)*5, 2, ''0'')',
        ')'
      );

    WHEN '15m' THEN
      SET @period_expression = CONCAT(
        'CONCAT(',
        '  DATE(DATE_FORMAT(CONVERT_TZ(FROM_UNIXTIME(start_timestamp / 1000), @@session.time_zone, ''+00:00''), ''%Y-%m-%d %H:%i:%s'')), '' '',',
        '  LPAD(HOUR(DATE_FORMAT(CONVERT_TZ(FROM_UNIXTIME(start_timestamp / 1000), @@session.time_zone, ''+00:00''), ''%Y-%m-%d %H:%i:%s'')), 2, ''0''), '':'',',
        '  LPAD(FLOOR(MINUTE(DATE_FORMAT(CONVERT_TZ(FROM_UNIXTIME(start_timestamp / 1000), @@session.time_zone, ''+00:00''), ''%Y-%m-%d %H:%i:%s''))/15)*15, 2, ''0'')',
        ')'
      );

    WHEN '1h'  THEN 
      SET @period_expression = CONCAT(
        'CONCAT(',
        '  DATE(DATE_FORMAT(CONVERT_TZ(FROM_UNIXTIME(start_timestamp / 1000), @@session.time_zone, ''+00:00''), ''%Y-%m-%d %H:%i:%s'')), '' '',',
        '  LPAD(HOUR(DATE_FORMAT(CONVERT_TZ(FROM_UNIXTIME(start_timestamp / 1000), @@session.time_zone, ''+00:00''), ''%Y-%m-%d %H:%i:%s'')), 2, ''0''), '':00:00''',
        ')'
      );

    WHEN '4h'  THEN
      SET @period_expression = CONCAT(
        'CONCAT(',
        '  DATE(DATE_FORMAT(CONVERT_TZ(FROM_UNIXTIME(start_timestamp / 1000), @@session.time_zone, ''+00:00''), ''%Y-%m-%d %H:%i:%s'')), '' '',',
        '  LPAD(FLOOR(HOUR(DATE_FORMAT(CONVERT_TZ(FROM_UNIXTIME(start_timestamp / 1000), @@session.time_zone, ''+00:00''), ''%Y-%m-%d %H:%i:%s''))/4)*4, 2, ''0''), '':00:00''',
        ')'
      );
		
    WHEN '1d'  THEN SET @period_expression = 'DATE(DATE_FORMAT(CONVERT_TZ(FROM_UNIXTIME(start_timestamp / 1000), @@session.time_zone, ''+00:00''), ''%Y-%m-%d %H:%i:%s''))';
    ELSE SET @period_expression = 'DATE(DATE_FORMAT(CONVERT_TZ(FROM_UNIXTIME(start_timestamp / 1000), @@session.time_zone, ''+00:00''), ''%Y-%m-%d %H:%i:%s''))';  -- Default to daily grouping
END CASE;
    
      -- Step 1: Get total count of grouped records and store it in `totalRecords`
    SET @sql_count = CONCAT(
        'SELECT COUNT(DISTINCT ', @period_expression, ') INTO @tempTotalRecords
         FROM `', tableName, '` 
         WHERE DATE_FORMAT(CONVERT_TZ(FROM_UNIXTIME(start_timestamp / 1000), @@session.time_zone, ''+00:00''), ''%Y-%m-%d %H:%i:%s'') BETWEEN ''', fromDate, ''' AND ''', toDate, ''';'
    );

    PREPARE stmt_count FROM @sql_count;
    EXECUTE stmt_count;
    DEALLOCATE PREPARE stmt_count;

    -- Assign the temporary total count to the OUT parameter
    SET totalRecords = @tempTotalRecords;

	 SET @sql_text:= CONCAT( 'WITH grouped_data AS ( 
								SELECT 
		                        ', @period_expression, ' AS time_interval,
								MIN(low) AS min_low,
								MAX(high) AS max_high,
								MIN(DATE_FORMAT(CONVERT_TZ(FROM_UNIXTIME(start_timestamp / 1000), @@session.time_zone, ''+00:00''), ''%Y-%m-%d %H:%i:%s'')) AS first_start_time,  -- Used for Open price
								MAX(DATE_FORMAT(CONVERT_TZ(FROM_UNIXTIME(start_timestamp / 1000), @@session.time_zone, ''+00:00''), ''%Y-%m-%d %H:%i:%s'')) AS last_start_time,   -- Used for Close price
								sum(volume) as volume
							FROM `', tableName, '`
									WHERE  (DATE_FORMAT(CONVERT_TZ(FROM_UNIXTIME(start_timestamp / 1000), @@session.time_zone, ''+00:00''), ''%Y-%m-%d %H:%i:%s'') between ''', fromDate, ''' AND ''', toDate, ''' )
							GROUP BY time_interval
				)
		  select  (@row_number:=@row_number + 1) AS id,
							s1.* 
				   from( 
			 select time_interval AS start_time_utc, 
					o.start_time as start_time, 
					o.open as open, 
					g.max_high as high, 
					g.min_low as low, 
					c.close as close,
					mc.marketcap AS marketcap,  -- Fetch last market cap value
					g.volume
				FROM grouped_data g
				LEFT JOIN `', tableName, '` o ON g.first_start_time = DATE_FORMAT(CONVERT_TZ(FROM_UNIXTIME(o.start_timestamp / 1000), @@session.time_zone, ''+00:00''), ''%Y-%m-%d %H:%i:%s'')  -- Fetch Open price
				LEFT JOIN `', tableName, '` c ON g.last_start_time = DATE_FORMAT(CONVERT_TZ(FROM_UNIXTIME(c.start_timestamp / 1000), @@session.time_zone, ''+00:00''), ''%Y-%m-%d %H:%i:%s'')   -- Fetch Close price
				LEFT JOIN `', tableName, '` mc ON g.last_start_time = DATE_FORMAT(CONVERT_TZ(FROM_UNIXTIME(mc.start_timestamp / 1000), @@session.time_zone, ''+00:00''), ''%Y-%m-%d %H:%i:%s'') -- Fetch last Market Cap
			ORDER BY g.time_interval DESC) s1,
            (SELECT @row_number:=0) AS t ' );
       
        
    
    if(criteria = 'volume')
    then 
      set @sqlTxtFinal := concat('select * from ( select id, start_time as x,volume as y from (  ', @sql_text, ' )TAB     LIMIT ', pageSize, ' OFFSET ', offsetValue, ' )tab1 ORDER BY x asc; ');
	elseif(criteria = 'all')
     then
      set @sqlTxtFinal := concat('select * from ( select TAB.* from (  ', @sql_text, ' )TAB     LIMIT ', pageSize, ' OFFSET ', offsetValue, ' )tab1 ORDER BY x asc; ');
	elseif(criteria = 'obv')
     then
      set @sqlTxtFinal := concat('select * from ( select TAB.* from (  ', @sql_text, ' )TAB     LIMIT ', pageSize, ' OFFSET ', offsetValue, ' )tab1 ORDER BY x asc; ');
	elseif(criteria = 'candle')
     then
      set @sqlTxtFinal := concat('select * from ( select id, start_time as x,  JSON_ARRAY(open , high, low, close) as y from (  ', @sql_text, ' )TAB     LIMIT ', pageSize, ' OFFSET ', offsetValue, ' )tab1 ORDER BY x asc; ');
	end if;
	
    
    insert into tmp_cr_debugging(sqlTxtObv)
    select @sqlTxtFinal;
    
    PREPARE stmt from @sqlTxtFinal;
    EXECUTE stmt;  
	
END
