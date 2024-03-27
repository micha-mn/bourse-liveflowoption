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

import com.data.synchronisation.springboot.data.dto.DataDTO;
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

	@GetMapping("/tablename")
	public ResponseEntity<List<Map<String, String>>> getTableNameEnum() {

		return ResponseEntity.ok(cryptoAnalyseService.getTableNameEnum());
	}

	@PostMapping("/insertData")
	public boolean insertData(@RequestBody DataDTO dataDTO) {
		return cryptoAnalyseService.insertIntoTable(dataDTO);
	}

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
}
