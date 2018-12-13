/**
 * 绘制highChart图表
 * 此文档用于设置图表的相关配置
 */
var seriesData=[
	[1,2,3,4,5,6],
	[2,4,6,8,10,12],
	[3,6,9,12,15,18],
	[4,8,12,16,20,24],
	[5,10,15,20,25,30],
	[6,12,18,24,30,36],
];
//图表1：设备各通道的历史温度曲线
function drawingHistoryChart(url, param){
	if(url!=null){
		$.ajax({ url: url, type: "post", data: param, dataType: "json",
			success: function(result){
				var series=[];
				var data = result.data;
				var LegendData = data.channelNumArr;
				var seriesData = data.channelTemArr;
				for(let i=0; i<LegendData.length; i++){
					var serie = {name: LegendData[i], data: seriesData[i], type:"line", pointInterval: 24*3600*1000, pointStart: Date.UTC(1900, 0, 1) };
					series.push(serie);
				}
				alert(series);
				var historyjson = {};
				historyjson.chart =  {zoomType: 'x' };
				historyjson.title = {text: '设备历史温度曲线'};
				historyjson.subtitle = {text: 'laofuzi 2018.12.13' };
				historyjson.legend = {enabled: true };
				historyjson.xAxis = {type: 'datetime', minRange: 24*3600*1000 };
				historyjson.yAxis = {title: {text: '温度值（℃）'} };
				historyjson.series = series;
				$("#chart_history").highcharts(historyjson);
			}
		});
	}
}