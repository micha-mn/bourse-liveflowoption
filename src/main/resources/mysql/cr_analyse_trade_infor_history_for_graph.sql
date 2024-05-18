DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `cr_analyse_trade_infor_history_for_graph`(IN currencyCode VARCHAR(255),
                                                                                       IN datePoint varchar(255),
																					   IN intervals varchar(255))
BEGIN
        if currencyCode = 'ENA' then
         SELECT sum(buy_QTY) as buy 
               ,sum(sell_qty) as sell -- ,sum(sell_qty) - sum(buy_QTY)
           FROM cr_ena_trade_history_analysis
          WHERE from_time <= DATE_ADD(datePoint, INTERVAL 15 MINUTE)
            AND to_time   >= DATE_SUB(datePoint, INTERVAL 15 MINUTE);
        end if ;      
 END$$
DELIMITER ;