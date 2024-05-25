CREATE DEFINER=`root`@`localhost` PROCEDURE `cr_analyse_trade_infor_history`(IN currencyCode VARCHAR(255))
BEGIN  
        if currencyCode = 'ENNA' then
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
        set LAST_HISTORICAL_DATA_ID = (select max(ID) from bourse.cr_ena_trade_history_info),
            last_historical_data_date_executed =  sysdate();
        
        truncate bourse.cr_ena_trade_history_info;
        end if ;  
        
        if currencyCode = 'ETHFI' then
         Insert into cr_ethfi_trade_history_analysis 
         select  (select next_val+1 from cr_ethfi_trade_history_analysis_seq) as id,
                 (select sum(quote_qty) from cr_ethfi_trade_history_info where IS_BUYER_MAKER = 0) buy_QT,
                 (select  sum(quote_qty) from cr_ethfi_trade_history_info where IS_BUYER_MAKER = 1) sell_qty,
                   (select sum(quote_qty) from cr_ethfi_trade_history_info where IS_BUYER_MAKER = 0)
                 - (select  sum(quote_qty) from cr_ethfi_trade_history_info where IS_BUYER_MAKER = 1) as difference_sell_buy,
				 min(time) from_time,
                 max(time) to_time
                from bourse.cr_ethfi_trade_history_info;
         update cr_ethfi_trade_history_analysis_seq
            set next_val = next_val+1;
            
        update cr_ethfi_tracking_table
           set LAST_HISTORICAL_DATA_ID = (select max(ID) from bourse.cr_ethfi_trade_history_info),
               last_historical_data_date_executed =  sysdate();
        
        truncate bourse.cr_ethfi_trade_history_info;
        end if ;  
 END