package com.data.synchronisation.springboot.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
@Table(name = "CR_ORDER_BOOK_CONSOLIDATED")
public class CRBTCOrderBookConsolidated {
	@Id
	 @GeneratedValue(generator = "CR_ORDER_BOOK_CONSOLIDATED_SEQ")
	 @GenericGenerator(
	      name = "CR_ORDER_BOOK_CONSOLIDATED_SEQ",
	      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	      parameters = {
	        @Parameter(name = "sequence_name", value = "CR_ORDER_BOOK_CONSOLIDATED_SEQ"),
	        @Parameter(name = "initial_value", value = "1"),
	        @Parameter(name = "increment_size", value = "1")
	        }
	    )
	private Long id;
	@Column(name = "PRICE")
    private BigDecimal price;
	@Column(name = "QUANTITY")
    private BigDecimal quantity;
	@Column(name = "REFER_DATE")
    private LocalDateTime referDate;
	@Column(name = "ORDER_TIMESTAMP")
    private Long orderTimestamp;
	@Column(name = "ACTION")      //buy
    private String action;  //sell
	@Column(name = "COIN")      //buy
    private String coin;  //sell
}
