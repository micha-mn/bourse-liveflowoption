package com.data.synchronisation.springboot.dto;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor 
@AllArgsConstructor
public class GraphResponseDTO {
	
	@Id
    private Long id;
	private String x;
	private String y;
}
