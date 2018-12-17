/**
 * 绘制highChart图表
 * 此文档用于设置图表的相关配置
 */
var historyOption = {
	chart: {zoomType: 'x', backgroundColor: '#21242e' },
	title: {text: '设备历史温度曲线', style: {color: '#ffffff'}},
	subtitle: {text: 'laofuzi 2018.12.13', style: {color: '#ffffff'}},
	legend: {enabled: true },
	tooltip: {shared: true},
	yAxis: {title: {text: '温度值（℃）'}, labels: {style: {color: '#ffffff'}} },
	plotOptions: {spline: {marker: {radius: 1, lineWidth: 1 } } },
	colors: ['#058DC7', '#50B432', '#ED561B', '#DDDF00', '#24CBE5', '#64E572', '#FF9655', '#FFF263', '#6AF9C4']
};
var historyxAxis={type: 'datetime', labels: {style: {color: '#ffffff'}} };
//图表1：设备各通道的历史温度曲线
function drawingHistoryChart(url, param){
	if(url!=null){
		$.ajax({ url: url, type: "post", data: param, dataType: "json",
			success: function(result){
				var data = result.data;
				var series=[];
				var LegendData = data.channelNumArr;
				var seriesData = data.channelTemArr;
				historyxAxis.categories=data.receiveTime;
				for(let i=0; i<LegendData.length; i++){
					var serie = {name: LegendData[i], data: seriesData[i], type:"spline" };
					series.push(serie);
				}
				historyOption.xAxis=historyxAxis;
				historyOption.series = series;
				$("#chart_history").highcharts(historyOption);
			}
		});
	}
}

//图表2：设备实时监测曲线图
function getCurrentChartData(url, param){
	var resultMap={};
	if(url!=null){
		$.ajax({ url: url, type: "post", data: param, dataType: "json", async:false,
			success: function(result){resultMap = result; }
		});
	}
	return resultMap;
}

var currentChart={
    chart: {type: 'spline', animation: Highcharts.svg, marginRight: 10, backgroundColor: "#21242e", events: {load: addPoints } },
    time: { useUTC: false },
    title: { text: '设备通道温度实时监测', style: {color: '#ffffff'} },
    xAxis: { type: 'datetime', labels: {style: {color: '#ffffff'}} },
    yAxis: { title: { text: '温度值（℃）' }, labels: {style: {color: '#ffffff'}} },
    tooltip: { shared: true },
    legend: { enabled: true },
    plotOptions: {spline: {marker: {radius: 1, lineWidth: 1 } } },
    colors: ['#058DC7', '#50B432', '#ED561B', '#DDDF00', '#24CBE5', '#64E572', '#FF9655', '#FFF263', '#6AF9C4'],
    series: [
    	{name: 'CH1', data: getSeriesData(0) },
    	{name: 'CH2', data: getSeriesData(1) },
    	{name: 'CH3', data: getSeriesData(2) },
    	{name: 'CH4', data: getSeriesData(3) },
    	{name: 'CH5', data: getSeriesData(4) },
    	{name: 'CH6', data: getSeriesData(5) }
    ]
};
//随机生成坐标值
function getSeriesData(num){
	var data = [], time = (new Date()).getTime(), i;
	//只展示60个点（一个小时的数据），暂时设定每3秒刷新一次
	for (i = -59; i <= 0; i += 1) {
		data.push({ x: time + i * 3000, y: Math.random()*10+num });
	}
	return data;
}
//新增系列坐标值
function addPoints() {
    var series = this.series;
    setInterval(function () {
        var x = (new Date()).getTime(), 
        	y1 = Math.random()*10, 
        	y2 = Math.random()*10+1,
        	y3 = Math.random()*10+2,
        	y4 = Math.random()*10+3, 
        	y5 = Math.random()*10+4,
        	y6 = Math.random()*10+5;
        series[0].addPoint([x, y1], true, true);
        series[1].addPoint([x, y2], true, true);
        series[2].addPoint([x, y3], true, true);
        series[3].addPoint([x, y4], true, true);
        series[4].addPoint([x, y5], true, true);
        series[5].addPoint([x, y6], true, true);
    }, 3000);
}