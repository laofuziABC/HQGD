/**
 * 绘制highChart图表
 * 此文档用于设置图表的相关配置
 */
var historyOption = {
	chart: {zoomType: 'x' },
	title: {text: '设备历史温度曲线'},
	subtitle: {text: 'laofuzi 2018.12.13' },
	legend: {enabled: true },
	yAxis: {title: {text: '温度值（℃）'} },
};
var xAxis={type: 'datetime'};
//图表1：设备各通道的历史温度曲线
function drawingHistoryChart(url, param){
	if(url!=null){
		$.ajax({ url: url, type: "post", data: param, dataType: "json",
			success: function(result){
				var data = result.data;
				var series=[];
				var LegendData = data.channelNumArr;
				var seriesData = data.channelTemArr;
				xAxis.categories=data.receiveTime;
				for(let i=0; i<LegendData.length; i++){
					var serie = {name: LegendData[i], data: seriesData[i], type:"line" };
					series.push(serie);
				}
				historyOption.xAxis=xAxis;
				historyOption.series = series;
				$("#chart_history").highcharts(historyOption);
			}
		});
	}
}