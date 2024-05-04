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
@Table(name = "cr_ena_trade_info")
public class EnaTradeInfo {
	@Id
	 @GeneratedValue(generator = "CR_ENA_TRADE_INFO_SEQ")
	 @GenericGenerator(
	      name = "CR_ENA_TRADE_INFO_SEQ",
	      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	      parameters = {
	        @Parameter(name = "sequence_name", value = "CR_ENA_TRADE_INFO_SEQ"),
	        @Parameter(name = "initial_value", value = "1"),
	        @Parameter(name = "increment_size", value = "1")
	        }
	    )
	private Long id1;
	@Column(name = "ID")
	private String id;
	@Column(name = "PRICE")
    private String price;
	@Column(name = "QTY")
    private String qty;
	@Column(name = "QUOTE_QTY")
    private String quoteQty;
	@Column(name = "TIME")
    private LocalDateTime time;
	@Column(name = "IS_BUYER_MAKER")
    private boolean isBuyerMaker;
	@Column(name = "IS_BEST_MATCH")
    private String isBestMatch;
	@Column(name = "REFER_DATE")
	@CreationTimestamp
    private LocalDateTime referDate;
}
