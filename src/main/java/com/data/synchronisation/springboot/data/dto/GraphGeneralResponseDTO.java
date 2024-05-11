package com.data.synchronisation.springboot.data.dto;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor 
@AllArgsConstructor
public class GraphGeneralResponseDTO {
	
	private String name;
	private List<GraphResponseDTO> data;
}
