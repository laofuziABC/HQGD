/**
 * 绘制highChart图表
 * 此文档用于设置图表的相关配置
 */
//设置公共配置项【开始】
var legend={enabled: true, backgroundColor: '#ffffff'};
var tooltip={shared: true};
var yAxis={title: {text: '温度值（℃）', style:{color: '#ffffff'} }, gridLineDashStyle: 'dot', labels: {style: {color: '#ffffff'}}, min: 0, max: 100 };
var plotOptions={spline: {marker: {radius: 1, lineWidth: 1 } } };
var colors=['#058DC7', '#50B432', '#ED561B', '#DDDF00', '#24CBE5', '#64E572', '#FF9655', '#FFF263', '#6AF9C4'];
var xAxis={type: 'datetime', tickWidth: 0, labels: {style: {color: '#ffffff'},
		formatter: function(){
			var str1=this.value.substr(0,10);
			var str2=this.value.substr(11,5);
			return String.prototype.concat(str2,"<br />", str1);
		}
	}
};
//设置公共配置项【结束】
//配置历史数据监测曲线图
var historyOption = {
	chart: {zoomType: 'x', backgroundColor: '#21242e' },
	title: {text: '设备历史温度曲线', style: {color: '#ffffff'}},
	legend: legend, tooltip: tooltip,xAxis: xAxis, yAxis: yAxis, plotOptions: plotOptions, colors: colors,
};
//配置当前数据监测统计图
var currentOption={
	chart: { type: 'spline', backgroundColor: "#21242e" },
    time: { useUTC: true },
    title: { text: '设备通道温度实时监测', style: {color: '#ffffff'} },
    xAxis: xAxis, yAxis: yAxis, tooltip: tooltip, legend: legend, plotOptions: plotOptions, colors: colors, 
};
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
				historyOption.xAxis.categories=data.receiveTime;
				historyOption.xAxis.tickInterval=Math.floor(totalCount/11);
				historyOption.series = series;
				$("#chart_history").empty();
				$("#chart_history").highcharts(historyOption);
			}
		});
	}
}

//图表2：绘制设备实时监测曲线图
function initCurrentChart(){
//	var startTime = parent.formatInitDateToString(new Date());
	var startTime = "1900-01-01 00:00:00";
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
			currentOption.xAxis.categories=data.receiveTime;
			currentOption.xAxis.tickInterval=Math.floor(totalCount/11);
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
