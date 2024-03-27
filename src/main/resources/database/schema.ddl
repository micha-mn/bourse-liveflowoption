CREATE TABLE live_option_flow_data (
  id BIGINT NOT NULL,
  flow VARCHAR(255),
  flow_date DATETIME NOT NULL,
  product VARCHAR(255),
  PRIMARY KEY (id)
) ENGINE=MyISAM;

create table live_option_flow_data_sequence (next_val bigint);
INSERT INTO live_option_flow_data_sequence (next_val) VALUES (1);