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
			  		  
		  
		  
		  