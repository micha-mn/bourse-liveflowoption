drop procedure cr_calculate_max_min_graph;
delete from cr_ena_max_min;
select * from cr_ena_max_min;
select * from cr_ena_tracking_table;

update cr_ena_tracking_table 
                           set not_exectude_min_max_price = 0;
select * from cr_ena_tracking_table;
call cr_calculate_max_min_graph('ENNA','2024-05-10 17:22:23' , '2025-05-10 17:29:23',500);
select * from cr_ena_max_min
select * from cr_ena_max_min_seq;
delete from cr_ena_max_min;

select * from cr_ena where refer_date between '2024-05-10 17:22:23' and '2025-05-10 17:29:23';

insert into cr_ena_ma_max_min(id, max_price, refer_date_max_timeStamp, refer_date_max,
								 min_price, refer_date_min_timeStamp, refer_date_min,
                                                    refer_date_from, refer_date_to)
select   @rownum:=@rownum + 1 as id,
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
					   and y.value = tab1.min_price) refer_date_min,
		tab1.refer_date_from as refer_date_from, 
		tab1.refer_date_to as refer_date_to
		
 from( 
	   SELECT  max(value) max_price
			  ,min(value) min_price
			  ,min(refer_date) refer_date_from
			  ,max(refer_date) refer_date_to
		  FROM ( SELECT x.*,
						FLOOR(@i:=@i+1/50)*50 i 
				  FROM CR_ena x, 
				  (SELECT @i:=0) vars
		   where x.refer_date between '2024-05-10 17:22:23' and '2025-05-10 17:29:23'
		ORDER BY refer_date) n 
		GROUP BY i
	)tab1,(SELECT @rownum := 0) r
    limit 1;
    
    call cr_data_for_graph("ENNA","2024-05-11 17:00:05","2024-05-11 17:24:05",20,"MAX");

    select id, refer_date_max_timestamp as x,refer_date_max_timestamp as y
         from cr_ena_max_min
       where refer_date_max between "2024-05-11 17:00:05" and "2024-05-11 17:24:05";