package com.data.synchronisation.springboot.data.dto;

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
@Entity
public class SupResDTO 
{
@Id
private String id;
private String resistant;
private String support;
}
