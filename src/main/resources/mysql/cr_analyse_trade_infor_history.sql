CREATE DEFINER=`root`@`localhost` PROCEDURE `cr_analyse_trade_infor_history`(IN currencyCode VARCHAR(255))
BEGIN
        if currencyCode = 'ENA' then
         Insert into cr_ena_trade_history_analysis 
         select  (select next_val+1 from cr_ena_trade_history_analysis_SEQ) as id,
                 (select sum(quote_qty) from cr_ena_trade_history_info where IS_BUYER_MAKER = 0) buy_QT,
                 (select  sum(quote_qty) from cr_ena_trade_history_info where IS_BUYER_MAKER = 1) sell_qty,
                   (select sum(quote_qty) from cr_ena_trade_history_info where IS_BUYER_MAKER = 0)
                 - (select  sum(quote_qty) from cr_ena_trade_history_info where IS_BUYER_MAKER = 1) as difference_sell_buy,
				 min(time) from_time,
                 max(time) to_time
                from bourse.cr_ena_trade_history_info;
         update cr_ena_trade_history_analysis_seq
        set next_val = next_val+1;
        update cr_ena_tracking_table
        set LAST_HISTORICAL_DATA_ID = (select max(ID) from bourse.cr_ena_trade_history_info);
       truncate bourse.cr_ena_trade_history_info;
        end if ;      
 END