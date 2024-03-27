package com.data.synchronisation.springboot.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor 
@AllArgsConstructor
@Entity
@Table(name = "LiveOptionFlowData")
public class LiveOptionFlow {
	@Id
	 @GeneratedValue(generator = "live_option_flow_data_sequence")
	 @GenericGenerator(
	      name = "asset_news_order_sequence",
	      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	      parameters = {
	        @Parameter(name = "sequence_name", value = "live_option_flow_data_sequence"),
	        @Parameter(name = "initial_value", value = "1"),
	        @Parameter(name = "increment_size", value = "1")
	        }
	    )
	private Long id;
    private String product;
    @Column(nullable = false, name = "flow_date",
    columnDefinition= "TIMESTAMP WITH TIME ZONE")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date flowDate;
    private String flow;
}
