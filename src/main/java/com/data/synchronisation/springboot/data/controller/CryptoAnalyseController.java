package com.data.synchronisation.springboot.data.controller;

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

import com.data.synchronisation.springboot.data.dto.CurrencyDTO;
import com.data.synchronisation.springboot.data.dto.DataDTO;
import com.data.synchronisation.springboot.data.dto.GraphDataReqDTO;
import com.data.synchronisation.springboot.data.dto.GraphFulllResponseDTO;
import com.data.synchronisation.springboot.data.dto.GraphGeneralResponseDTO;
import com.data.synchronisation.springboot.data.dto.GraphResponseDTO;
import com.data.synchronisation.springboot.data.dto.SupportResistantPointsDTO;
import com.data.synchronisation.springboot.data.dto.TradeHistoryResDTO;
import com.data.synchronisation.springboot.data.dto.TradeReqDTO;
import com.data.synchronisation.springboot.data.dto.TradeResponseDTO;
import com.data.synchronisation.springboot.data.service.CryptoAnalyseService;


@RestController
public class CryptoAnalyseController {

	@Autowired
	private final CryptoAnalyseService cryptoAnalyseService;

	public CryptoAnalyseController(CryptoAnalyseService cryptoAnalyseService) {
		this.cryptoAnalyseService = cryptoAnalyseService;
	}

	@RequestMapping(value = "/input-screen")
	public ModelAndView dataReadRxcelWritedb(ModelMap model) {
		return new ModelAndView("html/index");
	}
	
	@RequestMapping(value = "/graph")
	public ModelAndView dataGraph(ModelMap model) {
		return new ModelAndView("html/graph");
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
		return cryptoAnalyseService.getGraphData(req);
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
}
