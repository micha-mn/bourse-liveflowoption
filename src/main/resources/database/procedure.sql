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
    INSERT INTO tmp_cry_bitcoin_four_hours (openint,openeur, closeint ,closeeur, high, low, volume, marketcap, start_time, end_time)
		SELECT
			(SELECT openint
			 FROM tmp_cry_bitcoin_high_low
			 WHERE start_time >= utc_start_time
			   AND end_time <= utc_end_time
			 ORDER BY start_time ASC
			 LIMIT 1) AS `openint`,
			(SELECT openint
			 FROM tmp_cry_bitcoin_high_low
			 WHERE start_time >= cet_start_time
			   AND end_time <= cet_end_time
			 ORDER BY start_time ASC
			 LIMIT 1) AS `openeur`,
			(SELECT closeint
			 FROM tmp_cry_bitcoin_high_low
			 WHERE start_time >= utc_start_time
			   AND end_time <= utc_end_time
			 ORDER BY start_time DESC
			 LIMIT 1) AS `closeint`,
			(SELECT closeint
			 FROM tmp_cry_bitcoin_high_low
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
		FROM tmp_cry_bitcoin_high_low
			WHERE start_time >= utc_start_time
			   AND end_time <= utc_end_time;

    -- Ethereum
 /*   INSERT INTO tmp_cry_ethereum_four_hours (openint,openeur, closeint ,closeeur, high, low, volume, marketcap, start_time, end_time)
		SELECT
			(SELECT openint
			 FROM tmp_cry_ethereum_seconds
			 WHERE refer_date >= utc_start_time
			   AND refer_date < utc_end_time
			 ORDER BY refer_date ASC
			 LIMIT 1) AS `openint`,
             		(SELECT openeur
			 FROM tmp_cry_ethereum_seconds
			 WHERE refer_date >= utc_start_time
			   AND refer_date < utc_end_time
			 ORDER BY refer_date ASC
			 LIMIT 1) AS `openeur`,
			(SELECT closeint
			 FROM tmp_cry_ethereum_seconds
			 WHERE refer_date >= utc_start_time
			   AND refer_date < utc_end_time
			 ORDER BY refer_date DESC
			 LIMIT 1) AS `closeint`,
             (SELECT closeeur
			 FROM tmp_cry_ethereum_seconds
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
		FROM tmp_cry_ethereum_seconds
		WHERE refer_date >= utc_start_time
		  AND refer_date < utc_end_time;
  
    -- Solana
   INSERT INTO tmp_cry_solana_four_hours (openint,openeur, closeint ,closeeur, high, low, volume, marketcap, start_time, end_time)
		SELECT
			(SELECT openint
			 FROM tmp_cry_solana_seconds
			 WHERE refer_date >= utc_start_time
			   AND refer_date < utc_end_time
			 ORDER BY refer_date ASC
			 LIMIT 1) AS `openint`,
             (SELECT openeur
			 FROM tmp_cry_solana_seconds
			 WHERE refer_date >= utc_start_time
			   AND refer_date < utc_end_time
			 ORDER BY refer_date ASC
			 LIMIT 1) AS `openeur`,
			(SELECT closeint
			 FROM tmp_cry_solana_seconds
			 WHERE refer_date >= utc_start_time
			   AND refer_date < utc_end_time
			 ORDER BY refer_date DESC
			 LIMIT 1) AS `closeint`,
             (SELECT closeeur
			 FROM tmp_cry_solana_seconds
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
		FROM tmp_cry_solana_seconds
		WHERE refer_date >= utc_start_time
		  AND refer_date < utc_end_time;

    -- Shiba
    INSERT INTO tmp_cry_shiba_four_hours (openint,openeur, closeint ,closeeur, high, low, volume, marketcap, start_time, end_time)
			SELECT
				(SELECT openint
				 FROM tmp_cry_shiba_seconds
				 WHERE refer_date >= utc_start_time
				   AND refer_date < utc_end_time
				 ORDER BY refer_date ASC
				 LIMIT 1) AS `openint`,
                 (SELECT openeur
				 FROM tmp_cry_shiba_seconds
				 WHERE refer_date >= utc_start_time
				   AND refer_date < utc_end_time
				 ORDER BY refer_date ASC
				 LIMIT 1) AS `openeur`,
				(SELECT closeint
				 FROM tmp_cry_shiba_seconds
				 WHERE refer_date >= utc_start_time
				   AND refer_date < utc_end_time
				 ORDER BY refer_date DESC
				 LIMIT 1) AS `closeint`,
                 (SELECT closeeur
				 FROM tmp_cry_shiba_seconds
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
			FROM tmp_cry_shiba_seconds
			WHERE refer_date >= utc_start_time
			  AND refer_date < utc_end_time;

    -- Binance Coin
    INSERT INTO tmp_cry_binance_four_hours (openint,openeur, closeint ,closeeur, high, low, volume, marketcap, start_time, end_time)
		SELECT
			(SELECT openint
			 FROM tmp_cry_binance_seconds
			 WHERE refer_date >= utc_start_time
			   AND refer_date < utc_end_time
			 ORDER BY refer_date ASC
			 LIMIT 1) AS `openint`,
             (SELECT openeur
			 FROM tmp_cry_binance_seconds
			 WHERE refer_date >= utc_start_time
			   AND refer_date < utc_end_time
			 ORDER BY refer_date ASC
			 LIMIT 1) AS `openeur`,
			(SELECT closeint
			 FROM tmp_cry_binance_seconds
			 WHERE refer_date >= utc_start_time
			   AND refer_date < utc_end_time
			 ORDER BY refer_date DESC
			 LIMIT 1) AS `closeint`,
             (SELECT closeeur
			 FROM tmp_cry_binance_seconds
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
		FROM tmp_cry_binance_seconds
		WHERE refer_date >= utc_start_time
		  AND refer_date < utc_end_time;

    -- XRP
	INSERT INTO tmp_cry_xrp_four_hours (openint,openeur, closeint ,closeeur, high, low, volume, marketcap, start_time, end_time)
	SELECT
		(SELECT openint
		 FROM tmp_cry_xrp_seconds
		 WHERE refer_date >= utc_start_time
		   AND refer_date < utc_end_time
		 ORDER BY refer_date ASC
		 LIMIT 1) AS `openint`,
         (SELECT openeur
		 FROM tmp_cry_xrp_seconds
		 WHERE refer_date >= utc_start_time
		   AND refer_date < utc_end_time
		 ORDER BY refer_date ASC
		 LIMIT 1) AS `openeur`,
		(SELECT closeint
		 FROM tmp_cry_xrp_seconds
		 WHERE refer_date >= utc_start_time
		   AND refer_date < utc_end_time
		 ORDER BY refer_date DESC
		 LIMIT 1) AS `closeint`,
         (SELECT closeeur
		 FROM tmp_cry_xrp_seconds
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
	FROM tmp_cry_xrp_seconds
	WHERE refer_date >= utc_start_time
	  AND refer_date < utc_end_time;
   */
END$$

DELIMITER ;