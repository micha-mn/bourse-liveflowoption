CREATE TABLE live_option_flow_data (
  id BIGINT NOT NULL,
  flow VARCHAR(255),
  flow_date DATETIME NOT NULL,
  product VARCHAR(255),
  PRIMARY KEY (id)
) ENGINE=MyISAM;

create table live_option_flow_data_sequence (next_val bigint);
INSERT INTO live_option_flow_data_sequence (next_val) VALUES (1);

-- new 


 create table cr_bitcoin_high_low (id bigint not null, closeeur decimal(19,2), closeint decimal(19,2), high decimal(19,2), low decimal(19,2), marketcap decimal(19,2), openeur decimal(19,2), openint decimal(19,2), refer_date datetime, volume decimal(19,2), primary key (id));
 
 create table cr_bitcoin_high_low_seq (next_val bigint) ;
 
 insert into cr_bitcoin_high_low_seq values ( 1 );		  
		  
 create table cr_binance_high_low (id bigint not null, closeeur decimal(19,2), closeint decimal(19,2), high decimal(19,2), low decimal(19,2), marketcap decimal(19,2), openeur decimal(19,2), openint decimal(19,2), refer_date datetime, volume decimal(19,2), primary key (id));
 
 create table cr_binance_high_low_seq (next_val bigint) ;
 
 insert into cr_binance_high_low_seq values ( 1 );		  
		  	  
 create table cr_ethereum_high_low (id bigint not null, closeeur decimal(19,2), closeint decimal(19,2), high decimal(19,2), low decimal(19,2), marketcap decimal(19,2), openeur decimal(19,2), openint decimal(19,2), refer_date datetime, volume decimal(19,2), primary key (id));
 
 create table cr_ethereum_high_low_seq (next_val bigint) ;
 
 insert into cr_ethereum_high_low_seq values ( 1 );		  
	
 create table cr_shiba_high_low (id bigint not null, closeeur decimal(19,2), closeint decimal(19,2), high decimal(19,2), low decimal(19,2), marketcap decimal(19,2), openeur decimal(19,2), openint decimal(19,2), refer_date datetime, volume decimal(19,2), primary key (id));
 
 create table cr_shiba_high_low_seq (next_val bigint) ;
 
 insert into cr_shiba_high_low_seq values ( 1 );		  
		  
 create table cr_solana_high_low (id bigint not null, closeeur decimal(19,2), closeint decimal(19,2), high decimal(19,2), low decimal(19,2), marketcap decimal(19,2), openeur decimal(19,2), openint decimal(19,2), refer_date datetime, volume decimal(19,2), primary key (id));
 
 create table cr_solana_high_low_seq (next_val bigint) ;
 
 insert into cr_solana_high_low_seq values ( 1 );		  
		  	
	
 create table cr_xrp_high_low (id bigint not null, closeeur decimal(19,2), closeint decimal(19,2), high decimal(19,2), low decimal(19,2), marketcap decimal(19,2), openeur decimal(19,2), openint decimal(19,2), refer_date datetime, volume decimal(19,2), primary key (id));
 
 create table cr_xrp_high_low_seq (next_val bigint) ;
 
 insert into cr_xrp_high_low_seq values ( 1 );		  
		  


	

CREATE TABLE cr_bitcoin_four_hours (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
	openeur DECIMAL(30,8),
    openint DECIMAL(30,8),
    closeint DECIMAL(30,8),
	closeeur DECIMAL(30,8),
    high DECIMAL(30,8),
    low DECIMAL(30,8),
    volume DECIMAL(30,8),
    marketcap DECIMAL(30,8),
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);		  
create table cr_bitcoin_four_hours_seq (next_val bigint);
          insert into cr_bitcoin_four_hours_seq values ( 1 );	

		  
CREATE TABLE cr_ethereum_four_hours  (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    openeur DECIMAL(30,8),
    openint DECIMAL(30,8),
    closeint DECIMAL(30,8),
	closeeur DECIMAL(30,8),
    high DECIMAL(30,8),
    low DECIMAL(30,8),
    volume DECIMAL(30,8),
    marketcap DECIMAL(30,8),
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);		  
create table cr_ethereum_four_hours_seq (next_val bigint);
          insert into cr_ethereum_four_hours_seq values ( 1 );	
		  
CREATE TABLE cr_solana_four_hours  (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    openeur DECIMAL(30,8),
    openint DECIMAL(30,8),
    closeint DECIMAL(30,8),
	closeeur DECIMAL(30,8),
    high DECIMAL(30,8),
    low DECIMAL(30,8),
    volume DECIMAL(30,8),
    marketcap DECIMAL(30,8),
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);		  
	create table cr_solana_four_hours_seq (next_val bigint);
          insert into cr_solana_four_hours_seq values ( 1 );	
		  
CREATE TABLE cr_shiba_four_hours  (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    openeur DECIMAL(30,8),
    openint DECIMAL(30,8),
    closeint DECIMAL(30,8),
	closeeur DECIMAL(30,8),
    high DECIMAL(30,8),
    low DECIMAL(30,8),
    volume DECIMAL(30,8),
    marketcap DECIMAL(30,8),
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);		  
	create table cr_shiba_four_hours_seq (next_val bigint);
          insert into cr_shiba_four_hours_seq values ( 1 );	
		  		  
CREATE TABLE cr_binance_four_hours  (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    openeur DECIMAL(30,8),
    openint DECIMAL(30,8),
    closeint DECIMAL(30,8),
	closeeur DECIMAL(30,8),
    high DECIMAL(30,8),
    low DECIMAL(30,8),
    volume DECIMAL(30,8),
    marketcap DECIMAL(30,8),
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);		  
	create table cr_binance_four_hours_seq (next_val bigint);
          insert into cr_binance_four_hours_seq values ( 1 );	
		  		  		  
CREATE TABLE cr_xrp_four_hours  (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    openeur DECIMAL(30,8),
    openint DECIMAL(30,8),
    closeint DECIMAL(30,8),
	closeeur DECIMAL(30,8),
    high DECIMAL(30,8),
    low DECIMAL(30,8),
    volume DECIMAL(30,8),
    marketcap DECIMAL(30,8),
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);		  
	create table cr_xrp_four_hours_seq (next_val bigint);
          insert into cr_xrp_four_hours_seq values ( 1 );	
          
 
ALTER TABLE `bourse`.`cr_btc_high_low` 
CHANGE COLUMN `close` `close` DECIMAL(30,8) NULL DEFAULT NULL ,
CHANGE COLUMN `high` `high` DECIMAL(30,8) NULL DEFAULT NULL ,
CHANGE COLUMN `low` `low` DECIMAL(30,8) NULL DEFAULT NULL ,
CHANGE COLUMN `marketcap` `marketcap` DECIMAL(30,8) NULL DEFAULT NULL ,
CHANGE COLUMN `open` `open` DECIMAL(30,8) NULL DEFAULT NULL ,
CHANGE COLUMN `volume` `volume` DECIMAL(30,8) NULL DEFAULT NULL ;

-- new 			  		  
create table cr_btc_order_book (id bigint not null, action varchar(255), 
	ORDER_TIMESTAMP bigint, quantity decimal(19,2), refer_date datetime, 
     value decimal(19,2), primary key (id)) ;
     create table cr_btc_order_book_seq (next_val bigint);
insert into cr_btc_order_book_seq values ( 1 );		

 create table cr_order_book_consolidated(
 id bigint not null, 
 action varchar(255),
 coin varchar(255),
 quantity decimal(19,4),
 price decimal(19,4),
refer_date datetime,
 ORDER_TIMESTAMP bigint,
primary key (id));

create table cr_order_book_consolidated_seq (next_val bigint);
insert into cr_order_book_consolidated_seq values ( 1 );



DELIMITER $$
CREATE EVENT filter_orderbook_event
ON SCHEDULE EVERY 60 MINUTE
DO
BEGIN
  insert into cr_order_book_consolidated(id,action,coin,quantity,price,refer_date,ORDER_TIMESTAMP)
  select ROW_NUMBER() OVER (ORDER BY tab.action)+ (select max(next_val) from cr_order_book_consolidated_seq) AS id,
         tab.action,
         tab.coin,
         tab.quant,
         tab.average_price,
         tab.mx_refer_date,
         tab.order_timestamp
	 from(
	 select distinct sum(quantity) over(partition by action) as quant, 
			avg(value)over(partition by action) as average_price,
			max(refer_date)over(partition by action) as mx_refer_date,
			 max(ORDER_TIMESTAMP) over(partition by action) as order_timestamp,
			action,
			'BTC' as coin
	from  cr_btc_order_book k
	where ABS(k.value - (select btc.value
						from cr_btc btc
					   ORDER BY btc.refer_date DESC
							LIMIT 1))/k.value < 0.1)tab;
							
 insert into cr_btc_tracking_table_SEQ values ( 1 );
                            
                            
CREATE TABLE `cr_btc_tracking_table` (
`id` bigint NOT NULL,
`last_date_min_max_executed` datetime DEFAULT NULL,
`not_executed_min_max_price` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
`last_historical_data_id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
  
create table cr_btc_tracking_table_SEQ (next_val bigint);
  
insert into cr_btc_tracking_table(id,last_date_min_max_executed,not_executed_min_max_price,last_historical_data_id)
values(1,null,0,'1423721032');


CREATE TABLE `CR_BTC_TRADE_INFO` (
  `id1` bigint NOT NULL,
  `id` varchar(1000) ,
  `price` varchar(1000),
  `qty` varchar(1000) ,
  `QUOTE_QTY` varchar(1000) ,
  `time` datetime,
  `IS_BUYER_MAKER` varchar(1000) ,
  `IS_BEST_MATCH` varchar(1000) ,
  `refer_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id1`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
 create table CR_BTC_TRADE_INFO_SEQ (next_val bigint);   
insert into CR_BTC_TRADE_INFO_SEQ value(1);


CREATE TABLE `cr_BTC_trade_history_info` (
  `id1` bigint NOT NULL,
  `id` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `price` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `qty` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `QUOTE_QTY` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `IS_BUYER_MAKER` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `IS_BEST_MATCH` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `refer_date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
 

create table CR_BTC_TRADE_HISTORY_INFO_SEQ (next_val bigint) ;
 insert into CR_BTC_TRADE_HISTORY_INFO_SEQ values ( 1 );

		  
		  