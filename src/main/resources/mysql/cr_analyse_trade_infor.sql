 DELIMITER $$
CREATE PROCEDURE bourse.cr_analyse_trade_infor(IN currencyCode VARCHAR(255))
       BEGIN
        if currencyCode = 'ENA' then
         Insert into cr_ena_trade_analysis 
         select  (select next_val+1 from cr_ena_trade_analysis_SEQ) as id,
                 (select sum(quote_qty) from CR_ENA_TRADE_INFO where IS_BUYER_MAKER = 0) buy_QT,
                 (select  sum(quote_qty) from CR_ENA_TRADE_INFO where IS_BUYER_MAKER = 1) sell_qty,
                   (select sum(quote_qty) from CR_ENA_TRADE_INFO where IS_BUYER_MAKER = 0)
                 - (select  sum(quote_qty) from CR_ENA_TRADE_INFO where IS_BUYER_MAKER = 1) as difference_sell_buy,
				 min(time) from_time,
                 max(time) to_time
                from bourse.CR_ENA_TRADE_INFO;
         update cr_ena_trade_analysis_SEQ
        set next_val = next_val+1;
        truncate bourse.CR_ENA_TRADE_INFO;
        end if ;
        
        if currencyCode = 'W' then /*changed later to be dynamic*/
         Insert into cr_w_trade_analysis 
         select  (select next_val+1 from cr_w_trade_analysis_SEQ) as id,
                 (select sum(quote_qty) from CR_W_TRADE_INFO where IS_BUYER_MAKER = 0) buy_QT,
                 (select  sum(quote_qty) from CR_W_TRADE_INFO where IS_BUYER_MAKER = 1) sell_qty,
                   (select sum(quote_qty) from CR_W_TRADE_INFO where IS_BUYER_MAKER = 0)
                 - (select  sum(quote_qty) from CR_W_TRADE_INFO where IS_BUYER_MAKER = 1) as difference_sell_buy,
				 min(time) from_time,
                 max(time) to_time
                from bourse.CR_W_TRADE_INFO;
            
         update cr_w_trade_analysis_SEQ
        set next_val = next_val+1;
        truncate bourse.CR_W_TRADE_INFO;
        end if ;

             
 END$$
 DELIMITER ;