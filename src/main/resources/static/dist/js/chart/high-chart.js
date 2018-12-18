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
    chart: { type: 'spline', animation: Highcharts.svg, marginRight: 10, backgroundColor: "#21242e", events: {load: addPoints /* 新增系列的坐标值 */ } },
    time: { useUTC: true },
    title: { text: '设备通道温度实时监测', style: {color: '#ffffff'} },
    yAxis: yAxis, tooltip: tooltip, legend: legend, plotOptions: plotOptions, colors: colors,
};
var  current_xAxis={type: 'datetime', labels: {style: {color: '#ffffff'}} };
//图表1：绘制设备各通道的历史温度曲线
function drawingHistoryChart(url, param){
	if(url!=null){
		$.ajax({ url: url, type: "post", data: param, dataType: "json",
			success: function(result){
				var series=[];
				var data = result.data;
				var LegendData = data.channelNumArr;
				var seriesData = data.channelTemArr;
				history_xAxis.categories=data.receiveTime;
				for(let i=0; i<LegendData.length; i++){
					var serie = {name: LegendData[i], data: seriesData[i], type:"spline" };
					series.push(serie);
				}
				historyOption.xAxis=history_xAxis;
				historyOption.series = series;
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
			current_xAxis.categories=data.receiveTime;
			for(let i=0; i<LegendData.length; i++){
				var serie = {name: LegendData[i], data: seriesData[i], type:"spline" };
				series.push(serie);
			}
			currentOption.xAxis=current_xAxis;
			currentOption.series = series;
			$("#chart_current").highcharts(currentOption);
		}
	});
}
function addPoints() {
    setInterval(function () {
    	initCurrentChart();
    }, 60000);
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
