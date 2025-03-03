package com.data.synchronisation.springboot.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor 
@AllArgsConstructor
public class GraphFulllResponseDTO {
	private GraphGeneralResponseDTO dataNormal;
	private GraphGeneralResponseDTO dataVolume;
	private GraphGeneralResponseDTO dataCandle;
	private GraphGeneralResponseDTO dataMax;
	private GraphGeneralResponseDTO dataMin;
	private GraphGeneralResponseDTO dataSellOrder;
	private GraphGeneralResponseDTO dataBuyOrder;
}
