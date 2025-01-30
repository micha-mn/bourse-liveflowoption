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

 create table tmp_cry_bitcoin_high_low (id bigint not null, closeeur decimal(19,2), closeint decimal(19,2), high decimal(19,2), low decimal(19,2), marketcap decimal(19,2), openeur decimal(19,2), openint decimal(19,2), refer_date datetime, volume decimal(19,2), primary key (id));
 
 create table tmp_cry_bitcoin_high_low_seq (next_val bigint) ;
 
 insert into tmp_cry_bitcoin_high_low_seq values ( 1 );		  
		  
		  