package com.data.synchronisation.springboot.dto.orderBook;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.data.synchronisation.springboot.dto.GraphResponseProjection;
import com.data.synchronisation.springboot.dto.OrderBookByActionObjectProjection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor 
@AllArgsConstructor
public class OrderBookPercentageResponseDTO {
	
	private List<GraphResponseProjection> orderBookPercentage;

}
