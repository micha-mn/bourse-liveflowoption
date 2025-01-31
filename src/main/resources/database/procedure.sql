USE `bourse`;
DROP procedure IF EXISTS `calculation_audit_cryptos_4_hour_data`;

USE `bourse`;
DROP procedure IF EXISTS `bourse`.`calculation_audit_cryptos_4_hour_data`;
;
DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `calculation_audit_cryptos_4_hour_data`(IN referDate VARCHAR(255),IN groupId VARCHAR(255))
BEGIN
    IF groupId = 71 THEN
        call calculating_audit_cryptos_4_hour_data(referDate,groupId,'cr_bitcoin_four_hours');
    ELSEIF groupId = 72 THEN
        call calculating_audit_cryptos_4_hour_data(referDate,groupId,'cr_ethereum_four_hours');
    ELSEIF groupId = 73 THEN
        call calculating_audit_cryptos_4_hour_data(referDate,groupId,'cr_solana_four_hours');
    ELSEIF groupId = 74 THEN
        call calculating_audit_cryptos_4_hour_data(referDate,groupId,'cr_shiba_four_hours');
    ELSEIF groupId = 75 THEN
        call calculating_audit_cryptos_4_hour_data(referDate,groupId,'cr_binance_four_hours');  
    ELSEIF groupId = 76 THEN
        call calculating_audit_cryptos_4_hour_data(referDate,groupId,'cr_xrp_four_hours');  
         END IF;

END$$

DELIMITER ;

-- -----------------------------------------
USE `bourse`;
DROP procedure IF EXISTS `insert_cryptos_4_hour_data`;

USE `bourse`;
DROP procedure IF EXISTS `bourse`.`insert_cryptos_4_hour_data`;
;
DELIMITER $$
CREATE PROCEDURE insert_cryptos_4_hour_data(
    IN utc_start_time DATETIME,
    IN utc_end_time DATETIME,
    IN cet_start_time DATETIME,
    IN cet_end_time DATETIME
)
BEGIN
   
    -- Bitcoin
    INSERT INTO cr_bitcoin_four_hours (openint,openeur, closeint ,closeeur, high, low, volume, marketcap, start_time, end_time)
		SELECT
			(SELECT openint
			 FROM cr_bitcoin_high_low
			 WHERE start_time >= utc_start_time
			   AND end_time <= utc_end_time
			 ORDER BY start_time ASC
			 LIMIT 1) AS `openint`,
			(SELECT openint
			 FROM cr_bitcoin_high_low
			 WHERE start_time >= cet_start_time
			   AND end_time <= cet_end_time
			 ORDER BY start_time ASC
			 LIMIT 1) AS `openeur`,
			(SELECT closeint
			 FROM cr_bitcoin_high_low
			 WHERE start_time >= utc_start_time
			   AND end_time <= utc_end_time
			 ORDER BY start_time DESC
			 LIMIT 1) AS `closeint`,
			(SELECT closeint
			 FROM cr_bitcoin_high_low
			  WHERE start_time >= cet_start_time
			   AND end_time <= cet_end_time
			 ORDER BY start_time DESC
			 LIMIT 1) AS `closeeur`,
			MAX(high) AS high,
			MIN(low) AS low,
			SUM(volume) AS volume,
			AVG(REPLACE(marketcap, ',', '') + 0) AS avg_marketcap,  
			utc_start_time AS start_time,
			utc_end_time AS end_time
		FROM cr_bitcoin_high_low
			WHERE start_time >= utc_start_time
			   AND end_time <= utc_end_time;

    -- Ethereum
   INSERT INTO cr_ethereum_four_hours (openint,openeur, closeint ,closeeur, high, low, volume, marketcap, start_time, end_time)
		SELECT
			(SELECT openint
			 FROM cr_ethereum_high_low
			 WHERE start_time >= utc_start_time
			   AND end_time <= utc_end_time
			 ORDER BY start_time ASC
			 LIMIT 1) AS `openint`,
			(SELECT openint
			 FROM cr_ethereum_high_low
			 WHERE start_time >= cet_start_time
			   AND end_time <= cet_end_time
			 ORDER BY start_time ASC
			 LIMIT 1) AS `openeur`,
			(SELECT closeint
			 FROM cr_ethereum_high_low
			 WHERE start_time >= utc_start_time
			   AND end_time <= utc_end_time
			 ORDER BY start_time DESC
			 LIMIT 1) AS `closeint`,
			(SELECT closeint
			 FROM cr_ethereum_high_low
			  WHERE start_time >= cet_start_time
			   AND end_time <= cet_end_time
			 ORDER BY start_time DESC
			 LIMIT 1) AS `closeeur`,
			MAX(high) AS high,
			MIN(low) AS low,
			SUM(volume) AS volume,
			AVG(REPLACE(marketcap, ',', '') + 0) AS avg_marketcap,  
			utc_start_time AS start_time,
			utc_end_time AS end_time
		FROM cr_ethereum_high_low
			WHERE start_time >= utc_start_time
			   AND end_time <= utc_end_time;
  
    -- Solana
   INSERT INTO cr_solana_four_hours (openint,openeur, closeint ,closeeur, high, low, volume, marketcap, start_time, end_time)
		SELECT
			(SELECT openint
			 FROM cr_solana_high_low
			 WHERE refer_date >= utc_start_time
			   AND refer_date < utc_end_time
			 ORDER BY refer_date ASC
			 LIMIT 1) AS `openint`,
             (SELECT openeur
			 FROM cr_solana_high_low
			 WHERE refer_date >= utc_start_time
			   AND refer_date < utc_end_time
			 ORDER BY refer_date ASC
			 LIMIT 1) AS `openeur`,
			(SELECT closeint
			 FROM cr_solana_high_low
			 WHERE refer_date >= utc_start_time
			   AND refer_date < utc_end_time
			 ORDER BY refer_date DESC
			 LIMIT 1) AS `closeint`,
             (SELECT closeeur
			 FROM cr_solana_high_low
			 WHERE refer_date >= utc_start_time
			   AND refer_date < utc_end_time
			 ORDER BY refer_date DESC
			 LIMIT 1) AS `closeeur`,
			MAX(high) AS high,
			MIN(low) AS low,
			SUM(volume) AS volume,
			MAX(REPLACE(marketcap, ',', '')) AS marketcap, -- Remove commas
			utc_start_time AS start_time,
			utc_end_time AS end_time
		FROM cr_solana_high_low
		WHERE refer_date >= utc_start_time
		  AND refer_date < utc_end_time;

    -- Shiba
    INSERT INTO cr_shiba_four_hours (openint,openeur, closeint ,closeeur, high, low, volume, marketcap, start_time, end_time)
			SELECT
				(SELECT openint
				 FROM cr_shiba_high_low
				 WHERE refer_date >= utc_start_time
				   AND refer_date < utc_end_time
				 ORDER BY refer_date ASC
				 LIMIT 1) AS `openint`,
                 (SELECT openeur
				 FROM cr_shiba_high_low
				 WHERE refer_date >= utc_start_time
				   AND refer_date < utc_end_time
				 ORDER BY refer_date ASC
				 LIMIT 1) AS `openeur`,
				(SELECT closeint
				 FROM cr_shiba_high_low
				 WHERE refer_date >= utc_start_time
				   AND refer_date < utc_end_time
				 ORDER BY refer_date DESC
				 LIMIT 1) AS `closeint`,
                 (SELECT closeeur
				 FROM cr_shiba_high_low
				 WHERE refer_date >= utc_start_time
				   AND refer_date < utc_end_time
				 ORDER BY refer_date DESC
				 LIMIT 1) AS `closeeur`,
				MAX(high) AS high,
				MIN(low) AS low,
				SUM(volume) AS volume,
				MAX(REPLACE(marketcap, ',', '')) AS marketcap, -- Remove commas
				utc_start_time AS start_time,
				utc_end_time AS end_time
			FROM cr_shiba_high_low
			WHERE refer_date >= utc_start_time
			  AND refer_date < utc_end_time;

    -- Binance Coin
    INSERT INTO cr_binance_four_hours (openint,openeur, closeint ,closeeur, high, low, volume, marketcap, start_time, end_time)
		SELECT
			(SELECT openint
			 FROM cr_binance_high_low
			 WHERE refer_date >= utc_start_time
			   AND refer_date < utc_end_time
			 ORDER BY refer_date ASC
			 LIMIT 1) AS `openint`,
             (SELECT openeur
			 FROM cr_binance_high_low
			 WHERE refer_date >= utc_start_time
			   AND refer_date < utc_end_time
			 ORDER BY refer_date ASC
			 LIMIT 1) AS `openeur`,
			(SELECT closeint
			 FROM cr_binance_high_low
			 WHERE refer_date >= utc_start_time
			   AND refer_date < utc_end_time
			 ORDER BY refer_date DESC
			 LIMIT 1) AS `closeint`,
             (SELECT closeeur
			 FROM cr_binance_high_low
			 WHERE refer_date >= utc_start_time
			   AND refer_date < utc_end_time
			 ORDER BY refer_date DESC
			 LIMIT 1) AS `closeeur`,
			MAX(high) AS high,
			MIN(low) AS low,
			SUM(volume) AS volume,
			MAX(REPLACE(marketcap, ',', '')) AS marketcap, -- Remove commas
			utc_start_time AS start_time,
			utc_end_time AS end_time
		FROM cr_binance_high_low
		WHERE refer_date >= utc_start_time
		  AND refer_date < utc_end_time;

    -- XRP
	INSERT INTO cr_xrp_four_hours (openint,openeur, closeint ,closeeur, high, low, volume, marketcap, start_time, end_time)
	SELECT
		(SELECT openint
		 FROM cr_xrp_high_low
		 WHERE refer_date >= utc_start_time
		   AND refer_date < utc_end_time
		 ORDER BY refer_date ASC
		 LIMIT 1) AS `openint`,
         (SELECT openeur
		 FROM cr_xrp_high_low
		 WHERE refer_date >= utc_start_time
		   AND refer_date < utc_end_time
		 ORDER BY refer_date ASC
		 LIMIT 1) AS `openeur`,
		(SELECT closeint
		 FROM cr_xrp_high_low
		 WHERE refer_date >= utc_start_time
		   AND refer_date < utc_end_time
		 ORDER BY refer_date DESC
		 LIMIT 1) AS `closeint`,
         (SELECT closeeur
		 FROM cr_xrp_high_low
		 WHERE refer_date >= utc_start_time
		   AND refer_date < utc_end_time
		 ORDER BY refer_date DESC
		 LIMIT 1) AS `closeeur`,
		MAX(high) AS high,
		MIN(low) AS low,
		SUM(volume) AS volume,
		MAX(REPLACE(marketcap, ',', '')) AS marketcap, -- Remove commas
		utc_start_time AS start_time,
		utc_end_time AS end_time
	FROM cr_xrp_high_low
	WHERE refer_date >= utc_start_time
	  AND refer_date < utc_end_time;

END$$

DELIMITER ;

-- --------------




