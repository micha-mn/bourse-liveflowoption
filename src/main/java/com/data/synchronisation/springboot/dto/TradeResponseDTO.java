package com.data.synchronisation.springboot.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor 
@AllArgsConstructor
public class TradeResponseDTO {
	private List history15Min     = new ArrayList<>() ;
	private List history30Min     = new ArrayList<>() ;
	private List history45Min     = new ArrayList<>() ;
	private List history1Hour     = new ArrayList<>() ;
	private List history2Hour     = new ArrayList<>() ;
	private List history4Hour     = new ArrayList<>() ;
	private List history1Day      = new ArrayList<>() ;
}