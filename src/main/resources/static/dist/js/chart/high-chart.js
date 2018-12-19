/**
 * 绘制highChart图表
 * 此文档用于设置图表的相关配置
 */
//设置公共配置项【开始】
var legend={enabled: true, backgroundColor: '#ffffff'};
var tooltip={shared: true};
var yAxis={title: {text: '温度值（℃）', style:{color: '#ffffff'} }, labels: {style: {color: '#ffffff'}}, min: 0, max: 50 };
var plotOptions={spline: {marker: {radius: 1, lineWidth: 1 } } };
var colors=['#058DC7', '#50B432', '#ED561B', '#DDDF00', '#24CBE5', '#64E572', '#FF9655', '#FFF263', '#6AF9C4'];
//设置公共配置项【结束】
//配置历史数据监测曲线图
var historyOption = {
	chart: {zoomType: 'x', backgroundColor: '#21242e' },
	title: {text: '设备历史温度曲线', style: {color: '#ffffff'}},
	legend: legend, tooltip: tooltip, yAxis: yAxis, plotOptions: plotOptions, colors: colors
};
var history_xAxis={type: 'datetime', labels: {style: {color: '#ffffff'}} };
//配置当前数据监测统计图
var currentOption={
	chart: { type: 'spline', backgroundColor: "#21242e" },
    time: { useUTC: true },
    title: { text: '设备通道温度实时监测', style: {color: '#ffffff'} },
    yAxis: yAxis, tooltip: tooltip, legend: legend, plotOptions: plotOptions, colors: colors,
//    xAxis: {type: 'datetime', labels: {style: {color: '#ffffff'}}, 
//    	formatter:function(){
//    		var value=this.value;
//			if(value.length>10){
//				return "长度"+this.value+"大";
//			}
//		}
//    }
};
var  current_xAxis={type: 'datetime', labels: {style: {color: '#ffffff'}, step: 5} };
//图表1：绘制设备各通道的历史温度曲线
function drawingHistoryChart(url, param){
	if(url!=null){
		$.ajax({ url: url, type: "post", data: param, dataType: "json",
			success: function(result){
				var series=[];
				var data = result.data;
				var LegendData = data.channelNumArr;
				var seriesData = data.channelTemArr;
				//设定x轴相关配置
				var totalCount = seriesData[0].length;
				
				for(let i=0; i<LegendData.length; i++){
					var serie = {name: LegendData[i], data: seriesData[i], type:"spline" };
					totalCount=(totalCount>seriesData[i].length)?totalCount:(seriesData[i].length);
					series.push(serie);
				}
				history_xAxis.categories=data.receiveTime;
				history_xAxis.labels.step=Math.floor(totalCount/6);
				historyOption.xAxis=history_xAxis;
				historyOption.series = series;
				$("#chart_history").empty();
				$("#chart_history").highcharts(historyOption);
			}
		});
	}
}

//图表2：绘制设备实时监测曲线图
function initCurrentChart(){
	var startTime = parent.formatInitDateToString(new Date());
	var endTime = parent.formatDateToString(new Date());
	var url = "dataAcquisition/historicalCurve";
	var param = {"equipmentId": equiId, "startTime": startTime, "endTime": endTime };
	$.ajax({ url: url, type: "post", data: param, dataType: "json",
		success: function(result){
			var series=[];
			var data = result.data;
			var LegendData = data.channelNumArr;
			var seriesData = data.channelTemArr;
			//设定x轴相关配置
			var totalCount = seriesData[0].length;
			for(let i=0; i<LegendData.length; i++){
				var serie = {name: LegendData[i], data: seriesData[i], type:"spline" };
				totalCount=(totalCount>seriesData[i].length)?totalCount:(seriesData[i].length);
				series.push(serie);
			}
			current_xAxis.categories=data.receiveTime;
			current_xAxis.labels.step=Math.floor(totalCount/6);
			currentOption.xAxis=current_xAxis;
			currentOption.series = series;
			$("#chart_current").empty();
			$("#chart_current").highcharts(currentOption);
		}
	});
}

//获取当前通道最新温度值
function getCurrentChartData(url, param){
	var resultMap={};
	if(url!=null){
		$.ajax({ url: url, type: "post", data: param, dataType: "json", async:false,
			success: function(result){resultMap=result;},
			error: function(){resultMap=null; }
		});
	}
	return resultMap;
}
