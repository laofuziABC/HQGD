/**
 * 绘制highChart图表
 * 此文档用于设置图表的相关配置
 */
//设置图表公用配置项【开始】
var legend={enabled: true, backgroundColor: '#ffffff'};
var tooltip={shared: true, useHTML: true,
		xDateFormat: '%Y-%m-%d %H:%M:%S',
		pointFormatter: function(){
			var s='';
			switch(this.y){
				case -437: s='<b style="color: red;">- - - - -</b>'; break;
				case 2999: s='<b style="color: green;">系统调整中</b>'; break;
				case 3000: s='<b style="color: red;">- - -</b>'; break;
				default: s='<b style="color: green;">'+this.y+'</b>'; break;
			}
			return '<br/><span style="color:'+this.color+'">\u25CF</span>'+this.series.name+'：'+s;
		}
};
var yAxis={title: {text: '温度值（℃）', style:{color: '#ffffff'} }, gridLineDashStyle: 'dot', labels: {style: {color: '#ffffff'}}, min: 0, max: 100 };
var plotOptions={spline: {marker: {radius: 1, lineWidth: 1 } } };
var colors=['#058DC7', '#50B432', '#ED561B', '#DDDF00', '#24CBE5', '#64E572', '#FF9655', '#FFF263', '#6AF9C4'];
//设置图表公用配置项【结束】
//配置历史数据监测曲线图配置项
var historyOption = {
	chart: {zoomType: ['x','y'], backgroundColor: '#21242e' },
	title: {text: '历史温度曲线图', style: {color: '#ffffff'}},
	legend: legend, tooltip: tooltip, yAxis: yAxis, plotOptions: plotOptions, colors: colors, credits:{enabled: false},
	xAxis:{type: 'category', tickWidth: 0, labels: {style: {color: '#ffffff'}, 
		formatter: function(){ var str1=this.value.substr(0,10); var str2=this.value.substr(11,8); return String.prototype.concat(str2,"<br />", str1); }
	}}
};
//配置当前数据监测统计图
var currentOption={
	chart: { type: 'spline', backgroundColor: "#21242e", zoomType: ['x','y'], events: {load: addPoints } },
    title: { text: '实时温度监测图', style: {color: '#ffffff'} }, time: { useUTC: false },
    yAxis: yAxis, tooltip: tooltip, legend: legend, plotOptions: plotOptions, colors: colors, credits:{enabled: false},
    xAxis: {type: 'datetime', tickWidth: 0, labels: {style: {color: '#ffffff'}, format: '{value: %H:%M:%S<br/>%m-%d}' } },
};
//配置当空白统计图
var blankOption={
	chart: {zoomType: ['x','y'], backgroundColor: '#21242e' },
	title: {text: '温度曲线图', style: {color: '#ffffff'}},
	legend: legend, tooltip: tooltip, yAxis: yAxis, plotOptions: plotOptions, colors: colors,
	xAxis:{type: 'category'}, credits:{enabled: false},
	yAxis:{title: {text: '温度值（℃）', style:{color: '#ffffff'} }, gridLineDashStyle: 'dot', labels: {style: {color: '#ffffff'}}, min: 0, max: 100 },
	series:[{name: '数据加载中', data: [], type:"spline", pointInterval: 6e4}]
}

/**
 * 以下的方法用于同步或者异步获取图表的数据资源
 * 其中实时监控图的相关内容，需同步获取
 * 其它无需依靠当前时间加载的图表数据，为异步获取
 */
/* 方法1：同步获取指定地址下的数据资源
 * url：获取资源的途径，不能传空值；param：获取资源的传参。
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
/*方法2：异步获取指定时间段内的图表数据
 * url：获取资源的途径，不能传空值；param：获取资源的传参。
*/
function drawingHistoryChart(url, param){
	$.ajax({url: url, type: "post", data: param, dataType: "json",
		success: function(result){
			var series=[];
			var data = result.data;
			if(JSON.stringify(data) != '{}'){
				var legendData = data.channelNumArr;
				var seriesData = data.channelTemArr;
				var totalCount = seriesData[0].length;
				for(let i=0; i<legendData.length; i++){
					var serie = {name: legendData[i], data: seriesData[i], type:"spline"};
					totalCount=(totalCount>seriesData[i].length)?totalCount:(seriesData[i].length);
					series.push(serie);
				}
				historyOption.xAxis.categories=data.receiveTime;
				historyOption.xAxis.tickInterval=Math.floor(totalCount/11);
				historyOption.series = series;
				$("#chart_history").empty();
				$("#chart_history").highcharts(historyOption);
			}else{
				$("#chart_history").empty();
				$("#chart_history").highcharts(blankOption);
			}
		}
	});
}
/*
 * 方法3：绘制当前监测数据图
 * 首先获取登录时间到当前时间里的监测数据
 * 然后通过定时器，同步获取最新的数据点，添加在图表中
*/
//获取并计算常量【开始】
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
START_TIME=(NT_VALUE-ST_VALUE>ONE_DAY)?(new Date(NT_VALUE-ONE_DAY)):(START_TIME);
//获取并计算常量【结束】
function initCurrentChart(){
	var startTime = parent.formatDateToString(START_TIME);
	var endTime = parent.formatDateToString(new Date());
	var url = "dataAcquisition/periodDate";
	var param = {"equipmentId": equiId, "startTime": startTime, "endTime": endTime };
	var result=getChartData(url, param);
	//分别获取系列、横坐标、纵坐标集
	var channelList=result.channelList;
	var timeList=result.timeList;
	var dataList=result.dataList;
	var series=[];
	//只有通道数和系列数相等，才可以绘制图表
	if(dataList.length>0 && channelList.length==dataList.length && dataList[0].length>0){
		//组装系列值
		var totalArray=[];
		for(let i=0; i<dataList.length; i++){
			var temp = dataList[i];
			var singleArray=[];
			for(let j=0; j<timeList.length; j++){
				var tempTime=(new Date(timeList[j])).getTime();
				var tempArray = [tempTime, temp[j]];
				singleArray.push(tempArray);
			}
			totalArray.push(singleArray);
		}
		//为系列赋值
		for(let i=0; i<channelList.length; i++){
			var serie = {name: channelList[i], data: totalArray[i], type:"spline", pointInterval: 6e4};
			series.push(serie);
		}
	}else{
		for(let i=0; i<channelList.length; i++){
			var serie = {name: channelList[i], data: [], type:"spline", pointInterval: 6e4};
			series.push(serie);
		}
	}
	currentOption.series = series;
	$("#chart_current").empty();
	$("#chart_current").highcharts(currentOption);
}
//计算点的坐标，落在图表中
function addPoints(){
	var interval = 60000;
	var series = this.series;
	var timing=setInterval(function (){
		var url="dataAcquisition/realtime";
		var param={"equipmentId": equiId};
		var pointResult = getChartData(url, param);
		var pointsData = (pointResult==null)?null:pointResult.data;
		if(pointsData!=null && JSON.stringify(pointsData)!="{}" && (pointsData.length==pointsData[0].numOfCh)){
			var thisPointTime = (new Date(pointsData[0].receiveTime)).getTime();
			var nowtime = (new Date()).getTime();
			//获取所有通道的最新温度值
			var tempValues=[];
			for(let i=0; i<pointsData.length; i++){
				var yValue=parseFloat(pointsData[i].temperature);
				tempValues.push(yValue);
			}
			//设定纵轴上下限
			setValueRangeForCChart(tempValues);
			//判断此点是否在图表中，再绘制此点
			if(thisPointTime>ST_VALUE && nowtime-thisPointTime<1000*60*2){
				for(let i=0; i<tempValues.length; i++){
					//确定图表是否需要平移，当前点的采集时间与开始时间（startTime）间隔超过一天，图表向左平移
					if(thisPointTime-ST_VALUE>ONE_DAY){series[i].addPoint([thisPointTime, tempValues[i]], true, true); }
					else{series[i].addPoint([thisPointTime, tempValues[i]], true, false); }
				}
				drawCurrentChannels(pointsData);
			}else{
				$("#last-time").css({"color":"red"});
			}
		}
	}, interval);
	//清除页面多余的定时任务
	var start = (timing-60000>0) ?(timing-60000):0;
	for(var i=start; i<timing; i++){
	     clearInterval(i);
	}
}

function drawCurrentChannels(param){
	//设置DIV高度
	$("#channelDiv").css({"height":($(window).height())*0.45});
	var channel = "";
	let num = (param==null)?0:(param.length);
	if(num>0){
		//超过2分钟提示
		lTime=(new Date(param[0].receiveTime)).getTime();
		cTime=(new Date()).getTime();
		$("#last-time").text("最后监测时间："+param[0].receiveTime);
		if((cTime-lTime)>(2*60*1000)){ $("#last-time").css({"color":"red"}); }
		else{$("#last-time").css({"color":"#21242e"});}
		//设定尺寸适应容器【开始】
		let count = Math.ceil(num/3);
		//如果只有一排，让容器填充下方位置
		var divH=$(window).height()/3;
		var trH=(count==1)?(divH/2+"px;"):(divH/count+"px;");
		var divW=$(window).width()*0.9;
		var tdW=divW/3+"px;";
		//设定尺寸适应容器【结束】
		for(let i=0; i<count; i++){
			let startIndex = 3*i;
			let endIndex = (3*i+3>num)?num:(3*i+3);
			channel+="<tr style='height:"+trH+"'>";
			//通道结果每三个循环一回，缘于界面展示效果较规整
			for(let j=startIndex; j<endIndex; j++){
				let state=parseFloat(param[j].state);
				let innerText=(param[j].opticalFiberPosition=="" || param[j].opticalFiberPosition==null)?param[j].channelNum:param[j].opticalFiberPosition;
				if(state==5){
					var resultMsg=(param[j].temperature==2999)?("系统调整中"):(param[j].temperature);
					channel+="<td class='green' style='width:"+tdW+"; padding-left: 1%;'><span class='span_left'>"+innerText+"：</span><span class='span_right'>"+resultMsg+"</span></td>"; 
				}
				else if(state==4){channel+="<td class='red' style='width:"+tdW+"; padding-left: 1%;'><span class='span_left'>"+innerText+"：</span><span class='span_right'>- - - - -</span></td>"; }
				else if(state==3){channel+="<td class='red' style='width:"+tdW+"; padding-left: 1%;'><span class='span_left'>"+innerText+"：</span><span class='span_right'>- - -</span></td>"; }
				else{channel+="<td class='yellow' style='width:"+tdW+"; padding-left: 1%;'><span class='span_left'>"+innerText+"：</span><span class='span_right'>"+param[j].temperature+"</span></td>"; }
			}
			channel+="</tr>";
		}
	}else{
		channel+="<h3 style='color: red;'>未查找到通道监测信息！</h3>"; 
	}
	$("#channelsInfo").html(channel);
}
//根据实际需求设定纵轴温度值域
function setValueRangeForCChart(param){
	if(param.length>0){
		if($.inArray(2999, param)>-1 || $.inArray(3000, param)>-1){
			currentOption.yAxis.max=100;
		}else if($.inArray(-437, param)>-1){
			currentOption.yAxis.min=0;
		}else{
			var chart = $("#chart_current").highcharts();
			if (chart.yAxis[0].getExtremes().dataMax < 20) {
			   chart.yAxis[0].setExtremes(-10, 20);
			}else if(chart.yAxis[0].getExtremes().dataMax>20 && chart.yAxis[0].getExtremes().dataMax<50){
				chart.yAxis[0].setExtremes(0, 50);
			}else{
				chart.yAxis[0].setExtremes(0, 100);
			}
		}
	}else{
		currentOption.yAxis.max=100;
		currentOption.yAxis.min=0;
	}
}