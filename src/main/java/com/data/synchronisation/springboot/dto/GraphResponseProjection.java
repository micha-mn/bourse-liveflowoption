package com.data.synchronisation.springboot.dto;

public interface GraphResponseProjection {
	// If you don't actually need an id from the query, you can omit it here
    String getAction();   // action
    String getPrice();  // price
    String getPercentage();  // percentage
}
