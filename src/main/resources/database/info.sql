SELECT *
FROM information_schema.EVENTS
WHERE EVENT_SCHEMA = 'bourse'
  AND EVENT_NAME = 'filter_orderbook_event';
  
ALTER EVENT filter_orderbook_event DISABLE;

ALTER EVENT filter_orderbook_event
ON SCHEDULE EVERY 1 MINUTE;