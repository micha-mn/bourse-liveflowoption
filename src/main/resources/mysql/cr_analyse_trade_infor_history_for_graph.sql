DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `cr_analyse_trade_infor_history_for_graph`(IN currencyCode VARCHAR(255),
                                                                                       IN datePoint varchar(255),
																					   IN intervals varchar(255))
BEGIN
        if currencyCode = 'ENNA' then
        select 1 as id,
 (SELECT sum(buy_QTY) as buy15 
        
	FROM cr_ena_trade_history_analysis
          WHERE from_time <= DATE_ADD(datePoint, INTERVAL 15 MINUTE)
            AND to_time   >= DATE_SUB(datePoint, INTERVAL 15 MINUTE) ) buy15Min,
 (SELECT sum(sell_qty) as sell
	FROM cr_ena_trade_history_analysis
          WHERE from_time <= DATE_ADD(datePoint, INTERVAL 15 MINUTE)
            AND to_time   >= DATE_SUB(datePoint, INTERVAL 15 MINUTE)) sell15Min,
	(SELECT sum(buy_QTY) as buy 
	FROM cr_ena_trade_history_analysis
          WHERE from_time <= DATE_ADD(datePoint, INTERVAL 30 MINUTE)
            AND to_time   >= DATE_SUB(datePoint, INTERVAL 30 MINUTE) ) buy30Min,
 (SELECT sum(sell_qty) as sell
	FROM cr_ena_trade_history_analysis
          WHERE from_time <= DATE_ADD(datePoint, INTERVAL 30 MINUTE)
            AND to_time   >= DATE_SUB(datePoint, INTERVAL 30 MINUTE)) as sell30Min,
	(SELECT sum(buy_QTY) as buy 
	FROM cr_ena_trade_history_analysis
          WHERE from_time <= DATE_ADD(datePoint, INTERVAL 45 MINUTE)
            AND to_time   >= DATE_SUB(datePoint, INTERVAL 45 MINUTE) ) as buy45Min,
 (SELECT sum(sell_qty) as sell
	FROM cr_ena_trade_history_analysis
          WHERE from_time <= DATE_ADD(datePoint, INTERVAL 45 MINUTE)
            AND to_time   >= DATE_SUB(datePoint, INTERVAL 45 MINUTE)) as sell45Min,
	(SELECT sum(buy_QTY) as buy 
	FROM cr_ena_trade_history_analysis
          WHERE from_time <= DATE_ADD(datePoint, INTERVAL 1 HOUR)
            AND to_time   >= DATE_SUB(datePoint, INTERVAL  1 HOUR) ) as buy1Hour,
 (SELECT sum(sell_qty) as sellonsole
	FROM cr_ena_trade_history_analysis
          WHERE from_time <= DATE_ADD(datePoint, INTERVAL  1 HOUR)
            AND to_time   >= DATE_SUB(datePoint, INTERVAL  1 HOUR)) as sell1Hour,
	(SELECT sum(buy_QTY) as buy 
	FROM cr_ena_trade_history_analysis
          WHERE from_time <= DATE_ADD(datePoint, INTERVAL 2 HOUR)
            AND to_time   >= DATE_SUB(datePoint, INTERVAL  2 HOUR) ) as buy2Hour,
 (SELECT sum(sell_qty) as sell
	FROM cr_ena_trade_history_analysis
          WHERE from_time <= DATE_ADD(datePoint, INTERVAL  2 HOUR)
            AND to_time   >= DATE_SUB(datePoint, INTERVAL  2 HOUR)) as sell2Hour,
	(SELECT sum(buy_QTY) as buy 
	FROM cr_ena_trade_history_analysis
          WHERE from_time <= DATE_ADD(datePoint, INTERVAL 4 HOUR)
            AND to_time   >= DATE_SUB(datePoint, INTERVAL  4 HOUR) ) as buy4Hour,
 (SELECT sum(sell_qty) as sell
	FROM cr_ena_trade_history_analysis
          WHERE from_time <= DATE_ADD(datePoint, INTERVAL  4 HOUR)
            AND to_time   >= DATE_SUB(datePoint, INTERVAL  4 HOUR)) as sell4Hour,
	(SELECT sum(buy_QTY) as buy 
	FROM cr_ena_trade_history_analysis
          WHERE from_time <= DATE_ADD(datePoint, INTERVAL 24 HOUR)
            AND to_time   >= DATE_SUB(datePoint, INTERVAL  24 HOUR) ) as buy1Day,
 (SELECT sum(sell_qty) as sell
	FROM cr_ena_trade_history_analysis
          WHERE from_time <= DATE_ADD(datePoint, INTERVAL  24 HOUR)
            AND to_time   >= DATE_SUB(datePoint, INTERVAL  24 HOUR)) as sell1Day
from dual;
        end if ;      
 END$$
DELIMITER ;