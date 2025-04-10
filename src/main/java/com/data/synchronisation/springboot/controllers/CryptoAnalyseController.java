package com.data.synchronisation.springboot.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.data.synchronisation.springboot.domain.entity.CrBTCHighLow;
import com.data.synchronisation.springboot.domain.entity.CrBinanceHighLow;
import com.data.synchronisation.springboot.domain.entity.CrEthereumHighLow;
import com.data.synchronisation.springboot.domain.entity.CrShibaHighLow;
import com.data.synchronisation.springboot.domain.entity.CrSolanaHighLow;
import com.data.synchronisation.springboot.domain.entity.CrXrpHighLow;
import com.data.synchronisation.springboot.dto.CurrencyDTO;
import com.data.synchronisation.springboot.dto.DataDTO;
import com.data.synchronisation.springboot.dto.GraphDataReqDTO;
import com.data.synchronisation.springboot.dto.GraphFulllResponseDTO;
import com.data.synchronisation.springboot.dto.GraphGeneralResponseDTO;
import com.data.synchronisation.springboot.dto.GraphResponseDTO;
import com.data.synchronisation.springboot.dto.GraphResponseProjection;
import com.data.synchronisation.springboot.dto.OrderBookResponseDTO;
import com.data.synchronisation.springboot.dto.SupportResistantPointsDTO;
import com.data.synchronisation.springboot.dto.TradeHistoryResDTO;
import com.data.synchronisation.springboot.dto.TradeReqDTO;
import com.data.synchronisation.springboot.dto.TradeResponseDTO;
import com.data.synchronisation.springboot.service.CryptoAnalyseHighLowService;
import com.data.synchronisation.springboot.service.CryptoAnalyseService;


@RestController
public class CryptoAnalyseController {

	@Autowired
	private final CryptoAnalyseService cryptoAnalyseService;
	
	@Autowired
	private final CryptoAnalyseHighLowService cryptoAnalyseHighLowService;

	public CryptoAnalyseController(CryptoAnalyseService cryptoAnalyseService , 
								   CryptoAnalyseHighLowService cryptoAnalyseHighLowService) {
		this.cryptoAnalyseService = cryptoAnalyseService;
		this.cryptoAnalyseHighLowService = cryptoAnalyseHighLowService;
	}

	@RequestMapping(value = "/input-screen")
	public ModelAndView dataReadRxcelWritedb(ModelMap model) {
		return new ModelAndView("html/index");
	}
	
	@RequestMapping(value = "/graph")
	public ModelAndView dataGraph(ModelMap model) {
		return new ModelAndView("html/graph");
	}
	@RequestMapping(value = "/dashboard")
	public ModelAndView dashboardView(ModelMap model) {
		return new ModelAndView("html/dashboard/index");
	}

	@GetMapping("/tablename")
	public ResponseEntity<List<Map<String, String>>> getTableNameEnum() {

		return ResponseEntity.ok(cryptoAnalyseService.getTableNameEnum());
	}

	/*
	@PostMapping("/insertData")
	public boolean insertData(@RequestBody DataDTO dataDTO) {
		return cryptoAnalyseService.insertIntoTable(dataDTO);
	}
	*/

	@GetMapping("/getdata/{tableName}")
	public ResponseEntity<List<DataDTO>> getData(@PathVariable("tableName") String tableName) {

		return ResponseEntity.ok(cryptoAnalyseService.getData(tableName));
	}

	@PostMapping("/updatedata")
	public boolean updateData(@RequestBody DataDTO dataDTO) {
		return cryptoAnalyseService.updateData(dataDTO);
	}
	@DeleteMapping(value = "deletedata/{tablename}/{id}")
	public ResponseEntity<HttpStatus> deleteData(@PathVariable("tablename") String tablename, @PathVariable("id") String id) {
		cryptoAnalyseService.deleteData(tablename,id);
		return new ResponseEntity<>(HttpStatus.OK);
	} 
	
	@PostMapping(value = "getGraphData")
	public GraphFulllResponseDTO getGraphData(@RequestBody GraphDataReqDTO req) {
		  req.setFromDate("2025-02-21 21:36:04");
		  req.setToDate("2025-02-21 21:36:04");
		 return cryptoAnalyseService.getGraphData(req);
	}
	
	// to get candle
	@PostMapping(value = "getCandleGraphData")
	public GraphFulllResponseDTO getCandleGraphData(@RequestBody GraphDataReqDTO req) {
		return cryptoAnalyseService.getCandleGraphData(req);
	}
	@PostMapping(value = "getSupportResistantForGraph")
	public ResponseEntity<SupportResistantPointsDTO> getSupportResistantForGraph(@RequestBody GraphDataReqDTO req) {
		
		SupportResistantPointsDTO resp = cryptoAnalyseService.getSupportResistantForGraph(req);
		return new ResponseEntity<>(resp,HttpStatus.OK);
	}
	
	@PostMapping(value = "data/trade/history")
	public ResponseEntity<TradeResponseDTO> getTradeHistory(@RequestBody TradeReqDTO req) {
		
		TradeResponseDTO resp = cryptoAnalyseService.getTradeHistory(req);
		return new ResponseEntity<>(resp,HttpStatus.OK);
	}
	
	@GetMapping(value = "data/currency/list")
	public ResponseEntity<List<CurrencyDTO>> getCurrencyList() {
		
		List<CurrencyDTO> resp = cryptoAnalyseService.getCurrencyList();
		return new ResponseEntity<>(resp,HttpStatus.OK);
	}

	@GetMapping(value = "/api/btc/latest")
	   public ResponseEntity<CrBTCHighLow> getLatestBtc() {
		 return cryptoAnalyseHighLowService.getLatestBtc().map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

	@GetMapping(value = "/api/eth/latest")
	   public ResponseEntity<CrEthereumHighLow> getLatestEthereum() {
		 return cryptoAnalyseHighLowService.getLatestEthereum().map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }
	
	@GetMapping(value = "/api/sol/latest")
	   public ResponseEntity<CrSolanaHighLow> getLatestSolana() {
		 return cryptoAnalyseHighLowService.getLatestSolana().map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }
	

	@GetMapping(value = "/api/shib/latest")
	   public ResponseEntity<CrShibaHighLow> getLatestShiba() {
		 return cryptoAnalyseHighLowService.getLatestShiba().map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }
	

	@GetMapping(value = "/api/bnb/latest")
	   public ResponseEntity<CrBinanceHighLow> getLatestBinance() {
		 return cryptoAnalyseHighLowService.getLatestBinance().map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }
	
	@GetMapping(value = "/api/xrp/latest")
	   public ResponseEntity<CrXrpHighLow> getLatestXrp() {
		 return cryptoAnalyseHighLowService.getLatestXrp().map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
 }
}
