USE `bourse`;
DROP procedure IF EXISTS `cr_support_resistant_for_graph`;

USE `bourse`;
DROP procedure IF EXISTS `bourse`.`cr_support_resistant_for_graph`;
;

DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `cr_support_resistant_for_graph`(IN cryptoCurrency VARCHAR(255))
BEGIN

DECLARE sqlTxt varchar(255);
SET GLOBAL sql_mode = '';
SET SESSION sql_mode = '';  
-- insert into cr_debug_data_for_graph_table values(cryptoCurrency,fromDate,toDate,period,currencytype);
if cryptoCurrency='ENNA' then
	 select id
      , max(max_price)as resistant
      , min(min_price) as support
       /*,
       max(refer_date_max) as refer_date_max,
       min(refer_date_min) as refer_date_min,
       refer_date_from, 
       refer_date_to,
       i
       */
from(  
		select @row_number := @row_number +1 as id
               ,max_price, 
               refer_date_max,
               min_price,
               refer_date_min,
               refer_date_from,
               refer_date_to,
               FLOOR(@i:=@i+1/(select round(count(1)/2) 
                                  from cr_ena_max_min 
								 where refer_date_max >= DATE_SUB(NOW(),INTERVAL 1 HOUR)))
						*(select round(count(1)/2) 
                            from cr_ena_max_min 
						    where refer_date_max >= DATE_SUB(NOW(),INTERVAL 1 HOUR)) i
             
		from  cr_ena_max_min,
			  (SELECT @i:=0) vars,
              (SELECT @row_number:=0) vars1
		where refer_date_max >= DATE_SUB(NOW(),INTERVAL 1 HOUR)
		order by refer_date_max desc) tab
    group by i
    limit 2;
   end if;   
   
if cryptoCurrency='ETHFI' then   
   select id
      , max(max_price)as resistant
      , min(min_price) as support
       /*,
       max(refer_date_max) as refer_date_max,
       min(refer_date_min) as refer_date_min,
       refer_date_from, 
       refer_date_to,
       i
       */
from(  
		select @row_number := @row_number +1 as id
               ,max_price, 
               refer_date_max,
               min_price,
               refer_date_min,
               refer_date_from,
               refer_date_to,
               FLOOR(@i:=@i+1/(select round(count(1)/2) 
                                  from cr_ethfi_max_min 
								 where refer_date_max >= DATE_SUB(NOW(),INTERVAL 1 HOUR)))
						*(select round(count(1)/2) 
                            from cr_ethfi_max_min 
						    where refer_date_max >= DATE_SUB(NOW(),INTERVAL 1 HOUR)) i
             
		from  cr_ethfi_max_min,
			  (SELECT @i:=0) vars,
              (SELECT @row_number:=0) vars1
		where refer_date_max >= DATE_SUB(NOW(),INTERVAL 1 HOUR)
		order by refer_date_max desc) tab
    group by i
    limit 2;
end if;
END$$
DELIMITER ;