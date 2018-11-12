package com.hqgd.pms.controller.dataAcquisition;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.hqgd.pms.domain.DataAcquisitionVo;
import com.hqgd.pms.domain.QueryParametersVo;
import com.hqgd.pms.service.dataAcquisition.IDataAcquisitionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@Scope("request")
@RequestMapping("/dataAcquisition")
public class DataAcquisitionController {

	
	@Autowired
	@Qualifier("dataAcquisitionService")
	private IDataAcquisitionService dataAcquisitionService;
	
	
	@RequestMapping(value = "/realtime")
    public String getRealTimeMonitoringData(Model model,String equipmentId, HttpServletRequest request) throws ExecutionException, InterruptedException {
        
		List<DataAcquisitionVo> realTimeDateList = dataAcquisitionService.execGetRealTimeData(equipmentId);
		model.addAttribute("realTimeDateList", realTimeDateList);
		String json = new Gson().toJson(realTimeDateList).toString();    
        return json;
    }
	@RequestMapping(value = "/historical")
    public String getHistoricalData(Model model,QueryParametersVo queryVo, HttpServletRequest request) throws ExecutionException, InterruptedException {
        
		List<DataAcquisitionVo> historicalDataList = dataAcquisitionService.getHistoricalData(queryVo);
		model.addAttribute("historicalDataList", historicalDataList);
		String json = new Gson().toJson(historicalDataList).toString();
		return json;
    }
}
