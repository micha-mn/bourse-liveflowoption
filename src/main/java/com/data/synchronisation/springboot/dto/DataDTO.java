package com.data.synchronisation.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor 
@AllArgsConstructor
public class DataDTO {
	private Long id;
    private String referDate;
    private String value;
    private String tableName;
}