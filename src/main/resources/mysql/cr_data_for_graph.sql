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

END$$
DELIMITER ;