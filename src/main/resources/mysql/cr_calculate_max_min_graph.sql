DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `cr_calculate_max_min_graph`(IN cryptoCurrency VARCHAR(255),
																        IN fromDate varchar(255),
																        IN toDate varchar(255),
                                                                        IN period int)
BEGIN
-- SELECT somevalue INTO myvar FROM mytable WHERE uid = 1;


DECLARE sqlTxt varchar(255);
SET GLOBAL sql_mode = '';
SET SESSION sql_mode = '';  

     -- select not_exectude_min_max_price  from cr_ena_tracking_table into @not_exectude_min_max_price ;
     -- select last_date_min_max_executed  from cr_ena_tracking_table into @last_date_min_max_executed;
     
     SET @sql_text := 'select not_executed_min_max_price  from cr_ena_tracking_table  INTO @not_executed_min_max_price';
	 PREPARE stmt from @sql_text;
	 EXECUTE stmt;
     
     SET @sql_text := 'select last_date_min_max_executed  from cr_ena_tracking_table  INTO @last_date_min_max_executed';
	 PREPARE stmt from @sql_text;
	 EXECUTE stmt;
         
   -- insert into cr_debug_table values(cryptoCurrency,@not_executed_min_max_price,@last_date_min_max_executed,toDate,period);
    if cryptoCurrency='ENNA' and @not_executed_min_max_price > 10 then
                   insert into cr_ena_max_min(id,max_price, refer_date_max_timeStamp, refer_date_max,
													min_price, refer_date_min_timeStamp, refer_date_min,
                                                    refer_date_from, refer_date_to)
                   select   @rownum:=@rownum + (select next_val from cr_ena_max_min_SEQ) as id,
                             tab1.max_price,
							 UNIX_TIMESTAMP((select max(refer_date) 
											  from CR_ena y 
											 where y.refer_date between tab1.refer_date_from and tab1.refer_date_to
											   and y.value = tab1.max_price)) refer_date_max_timeStamp,
							 (select max(refer_date) 
											  from CR_ena y 
											 where y.refer_date between tab1.refer_date_from and tab1.refer_date_to
											   and y.value = tab1.max_price) as refer_date_max,
							 tab1.min_price, 
							 UNIX_TIMESTAMP((select max(refer_date) 
											   from CR_ena y 
										 where y.refer_date between tab1.refer_date_from and tab1.refer_date_to
										   and y.value = tab1.min_price)) refer_date_min_timeStamp,
							 (select max(refer_date) 
											   from CR_ena y 
										 where y.refer_date between tab1.refer_date_from and tab1.refer_date_to
										   and y.value = tab1.min_price) as refer_date_min,
							tab1.refer_date_from as refer_date_from, 
							tab1.refer_date_to as refer_date_to
                            
					 from( 
                           SELECT  max(value) max_price
								  ,min(value) min_price
						          ,min(refer_date) refer_date_from
						          ,max(refer_date) refer_date_to
					          FROM ( SELECT x.*,
                                            FLOOR(@i:=@i+1/period)*period i 
							          FROM CR_ena x, 
                                      (SELECT @i:=0) vars
							   where x.refer_date between @last_date_min_max_executed and toDate
                              -- where x.refer_date < DATE_SUB(sysdate(), INTERVAL 4 HOUR)
                              --   where x.refer_date >= toDate
                             -- where x.refer_date >= @last_date_min_max_executed
							ORDER BY refer_date) n 
							GROUP BY i
						)tab1,(SELECT @rownum := 0) r;
                        
                        update cr_ena_max_min_SEQ set next_val = (select max(id) + 1 from cr_ena_max_min);
                        
                        update cr_ena_tracking_table 
                           set last_date_min_max_executed = toDate,
                               not_executed_min_max_price = 0;
     ELSEIF 1=1 then
        		 
       select 1;
	 
     end if;

END$$
DELIMITER ;