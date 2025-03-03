package com.data.synchronisation.springboot.domain.entity;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.hibernate.annotations.CreationTimestamp;
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
@Table(name = "cr_btc_tracking_table")
public class BTCTrackingTable {
	@Id
	 @GeneratedValue(generator = "cr_btc_tracking_table_SEQ")
	 @GenericGenerator(
	      name = "cr_btc_tracking_table_SEQ",
	      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	      parameters = {
	        @Parameter(name = "sequence_name", value = "cr_btc_tracking_table_SEQ"),
	        @Parameter(name = "initial_value", value = "1"),
	        @Parameter(name = "increment_size", value = "1")
	        }
	    )
	@Column(name = "ID")
	private long id;
	@Column(name = "NOT_EXECUTED_MIN_MAX_PRICE")
    private String notExecutedMinMaxPrice;
	@Column(name = "LAST_HISTORICAL_DATA_ID")
    private String lastHistoricalDataId;
	@Column(name = "LAST_DATE_MIN_MAX_EXECUTED")
    private LocalDateTime lastDateMinMaxExecuted;
	@Column(name = "LAST_HISTORICAL_DATA_DATE_EXECUTED")
    private LocalDateTime lastHistoricalDataDateExecuted;
}
