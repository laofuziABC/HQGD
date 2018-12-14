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
				//组装图表series属性的data格式【开始】
//				var dataArr=[];
//				for(let m=0; m<seriesData.length; m++){
//					var tempArr=seriesData[m];
//					var array=new Array();
//					for(let n=0; n<tempArr.length; n++){
//						array.push(parseInt(tempArr[n]));
//					}
//					dataArr.push(array);
//				}
				//组装图表series属性的data格式【结束】
				for(let i=0; i<LegendData.length; i++){
//					var serie = {name: LegendData[i], data: dataArr[i], type:"line" };
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