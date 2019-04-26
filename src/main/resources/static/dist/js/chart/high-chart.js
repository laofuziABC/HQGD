/**
 * 绘制highChart图表 此文档用于设置图表的相关配置
 */
// 设置图表公用配置项【开始】
Highcharts.setOptions({global: { useUTC: false}});
var legend={enabled: true, backgroundColor: '#ffffff'};
var tooltip={shared: true, useHTML: true,
		xDateFormat: '%Y-%m-%d %H:%M:%S',
		pointFormatter: function(){
			var s='';
			switch(this.y){
				case -437: s='<b style="color: red;">- - - - -</b>'; break;
				case 2999: s='<b style="color: green;">系统调整中</b>'; break;
				case 3000: s='<b style="color: red;">- - -</b>'; break;
				default: s='<b style="color: green;">'+parseFloat(this.y)+'</b>'; break;
			}
			return '<br/><span style="color:'+this.color+'">\u25CF</span>'+this.series.name+'：'+s;
		}
};
var plotOptions={spline: {marker: {radius: 0, lineWidth: 1 } } };
var colors=['#058DC7', '#50B432', '#ED561B', '#DDDF00', '#24CBE5', '#64E572', '#FF9655', '#FFF263', '#6AF9C4'];
var lang={loading: "数据载入中……"};
var loading={labelStyle: {color: "red", fontWeight: "bold"}, 
		style: {backgroundColor: "rgba(255,255,255,0.7)", backgroundImage: "url('login/img/jiazaizhong.gif')", backgroundSize: '100% 100%'}
};
// 设置图表公用配置项【结束】
// 配置历史数据监测曲线图配置项
var historyOption = {
	chart: {zoomType: ['x','y'], backgroundColor: '#21242e', marginRight: 20, panning: true, panKey: 'ctrl'},
	title: {text: '历史温度曲线图', style: {color: '#ffffff'}},
	lang: lang, loading: loading, legend: legend, tooltip: tooltip, plotOptions: plotOptions, colors: colors, credits:{enabled: false}, 
	yAxis: { title: {text: '温度值（℃）', style:{color: '#ffffff'} }, gridLineDashStyle: 'dot', gridLineColor: '#ffffff', labels: {style: {color: '#ffffff'}}, min: 0, max: 100 },
	xAxis: {type: 'datetime', tickWidth: 0, labels: {style: {color: '#ffffff'}, format: '{value: %H:%M:%S<br/>%Y-%m-%d}', step:2 } },
	series:[{name: '查询数据', data: [], type:"spline", pointInterval: 6e4}]
};

// 配置当前数据监测统计图
var currentOption={
	chart: { type: 'spline', backgroundColor: "#21242e", zoomType: ['x','y'], events: {load: addPoints }, marginRight: 20 },
    title: { text: '实时温度监测图', style: {color: '#ffffff'} }, time: {useUTC: false },
    lang: lang, loading: loading, tooltip: tooltip, legend: legend, plotOptions: plotOptions, colors: colors, credits:{enabled: false},
    yAxis: { title: {text: '温度值（℃）', style:{color: '#ffffff'} }, gridLineDashStyle: 'dot', gridLineColor: '#ffffff', labels: {style: {color: '#ffffff'}}, min: 0, max: 100 },
    xAxis: {type: 'datetime', tickWidth: 0, labels: {style: {color: '#ffffff'}, format: '{value: %H:%M:%S<br/>%m-%d}' } },
    series:[{name: '查询数据', data: [], type:"spline", pointInterval: 6e4}]
};
// 配置设备通道最值统计图
var extremumOption={
	chart: { type: 'column', backgroundColor: "#21242e", marginRight: 10 },
    title: { text: '通道温度最值统计图', style: {color: '#ffffff'} },
    lang: lang, loading: loading, legend: legend, colors: colors, credits:{enabled: false},
    yAxis: { title: {text: '温度值（℃）', style:{color: '#ffffff'} }, gridLineDashStyle: 'dot', gridLineColor: '#ffffff', labels: {style: {color: '#ffffff'}}, min: 0, max: 100 },
    xAxis: {type: 'category', labels: {style: {color: '#ffffff'} } },
    plotOptions: {column: {dataLabels: {enabled: true, verticalAlign: 'top', inside: true, style:{color: '#ffffff'} } } },
    series:[{name: '通道温度最值', data: []}]
};
// 配置设备故障类型统计图
var errorTypeOption={
	chart: {backgroundColor: "#21242e" },
	title: { text: "设备故障类型统计图", style: {color: "#ffffff"} },
	loading: loading, colors: colors, credits:{enabled: false},
	legend:{enabled: true, itemStyle:{"color": "#fff", "cursor": "pointer", "fontSize": "16px"}, align:"center" },
	tooltip: {formatter: function(){var s=this.y+'，占比：'+Math.round(this.point.percentage*100)/100+'%';
			return this.series.name+'<br><span style="color:'+this.color+'">\u25CF</span>'+this.point.name+'：'+s;} },
	plotOptions:{pie: {cursor: "pointer", dataLabels: {color: "#fff" },  showInLegend: true }, series:{events:{click: changeChannelChart}} },
	series:[{type: "pie", name: "故障类型",data:[]}]
};
// 配置设备各通道发生故障次数统计图
var errorChannelOption={
	chart: { backgroundColor: "#21242e" },
	title: { text: "通道发生故障次数统计图", style: {color: "#ffffff"} },
	loading: loading, colors: colors, credits:{enabled: false},
	legend:{enabled: true, itemStyle:{ "color": "#fff", "cursor": "pointer", "fontSize": "16px"}, align:"center" },
	tooltip: {formatter: function(){var s=this.y+'，占比：'+Math.round(this.point.percentage*100)/100+'%';
			return this.series.name+'<br><span style="color:'+this.color+'">\u25CF</span>'+this.point.name+'：'+s;} },
	plotOptions:{pie: {cursor: "pointer", dataLabels: {color: "#fff" },  showInLegend: true } },
	series:[{type: "pie", name: "通道次数统计",data:[]}]
};

/**
 * 以下的方法用于同步或者异步获取图表的数据资源 其中实时监控图的相关内容，需同步获取 其它无需依靠当前时间加载的图表数据，为异步获取
 */
/*
 * 方法1：同步获取指定地址下的数据资源 url：获取资源的途径，不能传空值；param：获取资源的传参。
 * data：返回值，可能为null，也可能为空值，其本身为一个对象。
 */
function getChartData(url, param){
	var resultMap={};
	$.ajax({ url: url, type: "post", data: param, dataType: "json", async:false,
		success: function(result){resultMap=result;},
		error: function(){resultMap=null; }
	});
	return resultMap;
}
/*
 * 方法2：异步获取指定时间段内的图表数据 url：获取资源的途径，不能传空值；param：获取资源的传参。
 */
function drawingHistoryChart(url, param){
	$.ajax({url: url, type: "post", data: param, dataType: "json",
		success: function(data){
			var equiName = data.equipmentName;
			var seriesData = data.data;
			var temperatures = data.temperatures;
			if(seriesData.length>0){
				var series=[];
				for(var i=0; i<seriesData.length; i++){
					$.each(seriesData[i],function(item,value){
						var serie = {name: item, data: value, type:"spline", pointInterval: 6e4};
						series.push(serie);
					});
				}
				var max=Math.max.apply(null, temperatures);
				var min=Math.min.apply(null, temperatures);
				var state=$("#data-type").val();
				historyOption.yAxis.max=(state==0)?((max>100)?100:(max+10)):(max+10);
				historyOption.yAxis.min=(state==0)?((min<0)?0:(min-10)):(min-10);
				historyOption.series = series;
			}else{
				historyOption.yAxis.max=100;
				historyOption.yAxis.min=0;
				historyOption.series=[{name: '无相关数据', data: [], type:"spline", pointInterval: 6e4}];
			}
			historyOption.title.text="历史温度曲线图("+equiName+")";
			$("#chart_history").highcharts().destroy();
			$("#chart_history").highcharts(historyOption);
		}
	});
}
/*
 * 方法3：绘制当前监测数据图 首先获取登录时间到当前时间里的监测数据 然后通过定时器，同步获取最新的数据点，添加在图表中
 */
// 获取并计算常量
function getUrlParam(string) {
    var reg = new RegExp("(^|&)" + string + "=([^&]*)(&|$)", "i");  
    var l = decodeURI(window.location.search);
    var r = l.substr(1).match(reg);
    if (r != null){
    	var result = decodeURI(r[2]);
    	return unescape(result);
    }
}
var LOGIN_TIME=getUrlParam("LOGINTIME");
var ONE_DAY=1000*3600*24;
var START_TIME=new Date(parseInt(LOGIN_TIME));
var ST_VALUE=START_TIME.getTime();
var NOW_TIME=new Date();
var NT_VALUE=NOW_TIME.getTime();
// 获取并计算常量【结束】
function initCurrentChart(){
	START_TIME=(NT_VALUE-LOGIN_TIME>ONE_DAY)?(new Date(NT_VALUE-ONE_DAY)):(new Date((ST_VALUE-1000*60*15)));
	var startTime = parent.formatTimeToString(START_TIME);
	var endTime = parent.formatTimeToString(new Date());
	var url = "dataAcquisition/realTimeCurve";
	var param = {"equipmentId": equiId, "startTime": startTime, "endTime": endTime };
	var result=getChartData(url, param);
	if(result!=null){
		var equiName = result.equipmentName;
		var seriesData=result.data;
		if(seriesData.length>0){
			var series=[];
			for(var i=0; i<seriesData.length; i++){
				$.each(seriesData[i],function(item,value){
					var serie = {name: item, data: value, type:"spline", pointInterval: 6e4};
					series.push(serie);
				});
			}
			currentOption.series = series;
		}else{
			currentOption.series=[{name: '查询数据', data: [], type:"spline", pointInterval: 6e4}];
		}
		currentOption.title.text="实时温度监测图("+equiName+")";
		drawCurrentChannels(result.realTimeDate);
	}
	$("#chart_current").highcharts().destroy();
	$("#chart_current").highcharts(currentOption);
}
// 计算点的坐标，落在图表中
function addPoints(body){
	debugger;
		var pointsData = body.body;
		if(pointsData==undefined){
			
		}else{
			pointsData = JSON.parse(pointsData);
		}
		if(pointsData!=null && pointsData.length>0 && (pointsData.length==pointsData[0].numOfCh)){
			var thisPointTime = (new Date(pointsData[0].receiveTime)).getTime();
			var nowtime = (new Date()).getTime();
			var tempValues=[];
			for(let i=0; i<pointsData.length; i++){
				var yValue=parseFloat(pointsData[i].temperature);
				tempValues.push(yValue);
			}
			setValueRangeForCChart(tempValues);
			// 判断此点是否在图表中，再绘制此点
			if(thisPointTime>ST_VALUE && nowtime-thisPointTime<1000*60*2){
				for(let i=0; i<tempValues.length; i++){
					// 确定图表是否需要平移，当前点的采集时间与开始时间（startTime）间隔超过一天，图表向左平移
					if(thisPointTime-ST_VALUE>ONE_DAY){series[i].addPoint([thisPointTime, tempValues[i]], true, true); }
					else{series[i].addPoint([thisPointTime, tempValues[i]], true, false); }
				}
				drawCurrentChannels(pointsData);
			}else{
				$("#last-time").css({"color":"red"});
			}
		}
	
	
}
// 初始化实时监控表
function showCurrentContent(){
	if(equiId!=null){
// showBlocks();
		$("#chart_current").highcharts(currentOption);
		var chart = $("#chart_current").highcharts();
		chart.showLoading();
		initCurrentChart();
	}
}
// 展示实时监控通道最新监测的温度
function showBlocks(){
	var url="dataAcquisition/realtime";
	var param={"equipmentId": equiId};
	var result = getChartData(url, param);
	drawCurrentChannels(result);
}
function drawCurrentChannels(param){
	// 设置DIV高度
	var channel = "";
	var num;
	if(param==null){ num=0; $("#channelDiv").css({"height":($(window).height())*0.45}); }
	else{ num=param.length; }
	if(num>0){
		// 超过2分钟提示
		lTime=(new Date(param[0].receiveTime)).getTime();
		cTime=(new Date()).getTime();
		var timetext = "最新通道温度(单位:℃，最后监测时间："+param[0].receiveTime+")";
		$("#last-time").text(timetext);
		if((cTime-lTime)>(2*60*1000)){ $("#last-time").css({"color":"red"}); }
		else{$("#last-time").css({"color":"#21242e"});}
		let count = Math.ceil(num/3);
		var divH=$(window).height()/3;
		var trH=(count==1)?(divH/2+"px;"):(divH/count+"px;");
		var divW=$(window).width()*0.9;
		var tdW=divW/3+"px;";
		for(let i=0; i<count; i++){
			let startIndex = 3*i;
			let endIndex = (3*i+3>num)?num:(3*i+3);
			channel+="<tr style='height:"+trH+"'>";
			for(let j=startIndex; j<endIndex; j++){
				let state=parseInt(param[j].state);
				let innerText=(param[j].opticalFiberPosition=="" || param[j].opticalFiberPosition==null)?param[j].channelNum:param[j].opticalFiberPosition;
				if(state==5){
					var resultMsg=(param[j].temperature==2999)?("系统调整中"):(param[j].temperature);
					channel+="<td class='green' style='width:"+tdW+"; padding-left: 1%;'><span class='span_left'>"+innerText+": </span><span class='span_right'>"+resultMsg+"</span></td>"; 
				}
				else if(state==4){channel+="<td class='red' style='width:"+tdW+"; padding-left: 1%;'><span class='span_left'>"+innerText+": </span><span class='span_right'>- - - - -</span></td>"; }
				else if(state==3){channel+="<td class='red' style='width:"+tdW+"; padding-left: 1%;'><span class='span_left'>"+innerText+": </span><span class='span_right'>- - -</span></td>"; }
				else{channel+="<td class='yellow' style='width:"+tdW+"; padding-left: 1%;'><span class='span_left'>"+innerText+": </span><span class='span_right'>"+param[j].temperature+"</span></td>"; }
			}
			channel+="</tr>";
		}
	}else{
		channel+="<h3 style='color: red;'>未查找到通道监测信息！</h3>"; 
	}
	$("#channelsInfo").html(channel);
}
// 根据实际需求设定纵轴温度值域
function setValueRangeForCChart(param){
	if(param.length>0){
		var chart = $("#chart_current").highcharts();
		var max=Math.max.apply(null, param);
		var min=Math.min.apply(null, param);
		max=(max>chart.yAxis[0].getExtremes().dataMax)?max:(chart.yAxis[0].getExtremes().dataMax);
		min=(min<chart.yAxis[0].getExtremes().dataMin)?min:(chart.yAxis[0].getExtremes().dataMin);
		chart.yAxis[0].setExtremes((min > -10)?(min-10):0, (max < 100)?(max+10):100);
	}else{
		currentOption.yAxis.max=100;
		currentOption.yAxis.min=0;
	}
}
// 加载通道温度最值统计图
function fetchExtremumChartData(url, param){
	$.ajax({
		url: url, type: "post", data: param, dataType: "json",
		success: function(result){
			var equiName = result.equipment.equipmentName;
			var dataList = result.extremumList;
			var channels=[], maxs=[], mins=[];
			if(dataList.length>0){
				for(var i=0; i<dataList.length; i++){
					channels.push(dataList[i].channel);
					maxs.push(dataList[i].max);
					mins.push(dataList[i].min);
				}
			}
			loadingExtremumChart(maxs, mins, channels, equiName);
		}
	});
}
function loadingExtremumChart(maxs, mins, channels, equiName){
	if(maxs.length!=channels.length) return;
	if(mins.length!=channels.length) return;
	if(channels.length>0){
		var maxValue=(maxValue>Math.max.apply(null, maxs))?maxValue:(Math.max.apply(null, maxs));
		var minValue=(minValue<Math.min.apply(null, mins))?minValue:(Math.min.apply(null, mins));
		extremumOption.yAxis.min=(minValue<0)?minValue:0;
		extremumOption.yAxis.max=maxValue;
		extremumOption.xAxis.categories=channels;
		extremumOption.series=[{name: "最大值", data: maxs, type: 'column'}, {name: "最小值", data: mins, type: 'column'}];
	}else{
		extremumOption.series=[{name: "无相关数据", data: [], type: 'column'}];
	}
	extremumOption.title.text="通道温度最值统计图("+equiName+")";
	$("#chart_extremum").highcharts().destroy();
	$("#chart_extremum").highcharts(extremumOption);
}
// 加载通道报错信息统计图
var errors_forchart;
function fetchErrorChartData(url,param){
	var resultMap={};
	$.ajax({url: url, type: "post", data: param, dataType: "json", async:false,
		success: function(result){resultMap=result;},
		error: function(){resultMap=null; }
	});
	var result_data=resultMap.data;
	if(result_data==null || result_data.length==0) {
		var equiName=resultMap.equipment.equipmentName;
		errorTypeOption.title.text="设备故障类型统计图("+equiName+")";
		errorTypeOption.series=[{type: "pie", name: "通道次数统计",data: [{name: '无异常数据', y: 1}]}];
		errorChannelOption.title.text="通道故障次数统计图("+equiName+")";
		errorChannelOption.series=[{type: "pie", name: "故障类型",data:[{name: '无异常数据', y: 1}]}];
		$("#chart_error_channel").highcharts(errorChannelOption);
		$("#chart_error_type").highcharts(errorTypeOption);
		return;
	}
	var channels=[], code2=[], code3=[], code4=[], code9=[];
	for(var i=0; i<result_data.length; i++){
		channels.push(result_data[i].channelNum);
		code2.push(result_data[i].communicate);
		code3.push(result_data[i].fiber);
		code4.push(result_data[i].thermometer);
		code9.push(result_data[i].overTemperature);
	}
	var errors={"channels":channels, "code2":code2, "code3":code3, "code4":code4, "code9":code9, "equipment":resultMap.equipment };
	errors_forchart=errors;
	drawingErrorTypesChart(errors);
	drawingErrorChannelsChart(errors);
}
// 绘制设备异常类型统计图
function drawingErrorTypesChart(param){
	var equiName=param.equipment.equipmentName;
	errorTypeOption.title.text="设备故障类型统计图("+equiName+")";
	if(param.code2.addition()+param.code3.addition()+param.code4.addition()+param.code9.addition()==0){
		errorTypeOption.series=[{type: "pie", name: "故障类型",
			data: [{name: '无异常数据', y: 1}]
		}];
	}else{
		errorTypeOption.series=[{type: "pie", name: "故障类型",
			data: [{name: '485通信故障', y: param.code2.addition()}, {name: '光纤故障', y: param.code3.addition()}, 
				{name: '测温仪故障', y: param.code4.addition()}, {name: '超温故障', y: param.code9.addition()}]
		}];
	}
	$("#chart_error_type").highcharts().destroy();
	$("#chart_error_type").highcharts(errorTypeOption);
}
// 绘制通道异常次数统计图
function drawingErrorChannelsChart(param){
	var channels=param.channels;
	var equiName=param.equipment.equipmentName;
	var resultData=param.code2.countall(param.code3).countall(param.code4).countall(param.code9);
	if(resultData.length!=channels.length) return;
	if(resultData.addition()==0){
		errorChannelOption.series=[{type: "pie", name: "通道次数统计",data:[{name: '无异常数据', y: 1}]}];
	}else{
		var result=[];
		for(var i=0;i<channels.length;i++){
			result.push({name:channels[i], y: resultData[i]});
		}
		errorChannelOption.series[0].data=result;
	}
	errorChannelOption.title.text="通道故障次数统计图("+equiName+")";
	$("#chart_error_channel").highcharts().destroy();
	$("#chart_error_channel").highcharts(errorChannelOption);
}
function changeChannelChart(e){
	var data=e.point.options;
	var channels=errors_forchart.channels;
	var mydata=[], result=[];
	if(data.name=="485通信故障"){mydata=errors_forchart.code2;}
	if(data.name=="光纤故障"){mydata=errors_forchart.code3;}
	if(data.name=="测温仪故障"){mydata=errors_forchart.code4;}
	if(data.name=="超温故障"){mydata=errors_forchart.code9;}
	if(mydata.addition()==0) return;
	for(var i=0;i<mydata.length;i++){
		result.push({name:channels[i], y: mydata[i]});
	}
	var title=errorChannelOption.title.text;
	title=(title.indexOf(":")>-1)?title.substring(0,title.indexOf(":")):title;
	errorChannelOption.title.text=title+": "+data.name;
	errorChannelOption.series[0].data=result;
	$("#chart_error_channel").highcharts().destroy();
	$("#chart_error_channel").highcharts(errorChannelOption);
}
function datetimeValidate(e){if(e.value==""||e.value==null){e.classList.add("error");return}else if(e.id=="startDate"||e.id=="startTime"){e.classList.remove("error");return}else{if(e.id=="endDate"){($("#startDate").val())>($("#endDate").val())?(e.classList.add("error")):(e.classList.remove("error"))}else if(e.id=="endTime"){($("#endTime").val())>($("#startTime").val())?(e.classList.remove("error")):(e.classList.add("error"))}}};
Array.prototype.addition=function(){var count=0;for(var i=0;i<this.length;i++){count+=this.valueOf()[i];}return count;};Array.prototype.countall=function(array){if(this.length!=array.length)return;var result=[];for(var i=0;i<this.length;i++){result.push(this.valueOf()[i]+array[i]);}return result;};