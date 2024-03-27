package com.data.synchronisation.springboot.controllers;

import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.data.synchronisation.springboot.domain.LiveOptionFlow;
import com.data.synchronisation.springboot.dto.LiveOptionFlowDTO;
import com.data.synchronisation.springboot.service.LiveFlowOptionService;
@RestController
@RequestMapping(value = "flow")
public class LiveFlowOptionController {

	@Autowired
	private final LiveFlowOptionService liveFlowOptionService;
	private final TemplateEngine templateEngine;

	public LiveFlowOptionController(LiveFlowOptionService liveFlowOptionService,
									TemplateEngine templateEngine)
	{
		this.liveFlowOptionService = liveFlowOptionService;
		this.templateEngine = templateEngine;
	}
	
	 @GetMapping("/liveoptionflow")
	 @ResponseBody
	 public String getTemplate() {
		    ModelAndView modelAndView = new ModelAndView("liveOptionFlow");
	     
	        String renderedHtml = templateEngine.process(modelAndView.getViewName(), new Context(null, modelAndView.getModel()));
	        return renderedHtml;
	    }
	 @GetMapping("/historicalflow")
	 @ResponseBody
	 public String getHistoricalflowTemplate() {
		    ModelAndView modelAndView = new ModelAndView("historicalFlow");
	     
	        String renderedHtml = templateEngine.process(modelAndView.getViewName(), new Context(null, modelAndView.getModel()));
	        return renderedHtml;
	    }
	 @GetMapping("/flowsearchengine")
	 @ResponseBody
	 public String getFlowSearchEngineTemplate() {
		    ModelAndView modelAndView = new ModelAndView("flowSearchEngine");
	     
	        String renderedHtml = templateEngine.process(modelAndView.getViewName(), new Context(null, modelAndView.getModel()));
	        return renderedHtml;
	    }
	 @PostMapping(value = "saveLiveOptionFlowData")
	 public LiveOptionFlow saveLiveOptionFlowData(@RequestBody List<LiveOptionFlowDTO> liveOptionFlowDataList){
		  return  liveFlowOptionService.saveLiveOptionFlowData(liveOptionFlowDataList);
	    }
	 @GetMapping(value = "getLiveOptionByDate/{date}", produces = "application/json;charset=UTF-8")
	    public ResponseEntity <List<LiveOptionFlow>> getLiveOptionByDate(@PathVariable("date") String date){
			return new ResponseEntity<>(liveFlowOptionService.getLiveOptionFlowDataByDate(date), HttpStatus.OK);
	    }
	 @GetMapping(value = "getLiveOptionFlowDataByValue/{value}", produces = "application/json;charset=UTF-8")
	    public ResponseEntity <List<LiveOptionFlow>> getLiveOptionFlowDataByValue(@PathVariable("value") String value){
			return new ResponseEntity<>(liveFlowOptionService.getLiveOptionFlowDataByValue(value), HttpStatus.OK);
	    }
	 @GetMapping(value = "getLiveOptionFlowDataByValueAndProduct/{value}/{product}", produces = "application/json;charset=UTF-8")
	    public ResponseEntity <List<LiveOptionFlow>> getLiveOptionFlowDataByValueAndProduct(@PathVariable("value") String value,@PathVariable("product") String product){
			return new ResponseEntity<>(liveFlowOptionService.getLiveOptionFlowDataByValueAndProduct(value,product), HttpStatus.OK);
	    } 
	 @DeleteMapping(value = "deleteLiveOptionById/{id}")
		public  ResponseEntity<Object> deleteLiveOptionById(@PathVariable("id") long id) {
		 		liveFlowOptionService.deleteLiveOptionFlow(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}
	 @PostMapping(value = "updateflowbyid")
		public LiveOptionFlow UpdateFlowById(@RequestBody LiveOptionFlow liveOptionFlow) {
			return liveFlowOptionService.updateFlowById(liveOptionFlow);
		} 
}
