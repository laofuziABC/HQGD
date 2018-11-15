/* 设置统计图图表相关配置属性 (老夫子 2018.11.12) */

/*历史温度折线图(line-chart)【开始】*/
/*
 * 以获取到的检测温度时间为统计图的横坐标，温度值作为纵坐标，异步获取并加载统计图
 * 1、通过设备主键，获取设备的横坐标（时间数组）；
 * 2、通过设备主键，获取设备的所有通道（图例数组）；
 * 3、通过设备主键和通道编号，获取到所有通道在不同时间里的温度值（单一通道为温度数组）
 */
var lineOption={
	title: {text: '历史温度折线图', x: 'center'},
	xAxis: {type: 'category', data: ['time1', 'time2', 'time3', 'time4', 'time5', 'time6'] },
    yAxis: {type: 'value', text:'温度'},
    tooltip: {trigger: 'axis'},
    legend: {top: 30, data:['通道一','通道二','通道三','通道四','通道五','通道六'] },
    grid: {x: 30, y: 30, x2: 0, y2: 20},
    series: [
	    {type: 'line',smooth: true, name: '通道一', data: [21.6, 18.5, 22.4, 27.1, 25.2, 24.4]},
	    {type: 'line',smooth: true, name: '通道二', data: [18.5, 22.4, 27.1, 25.2, 24.4, 21.6]},
	    {type: 'line',smooth: true, name: '通道三', data: [22.4, 27.1, 25.2, 24.4, 21.6, 18.5]},
	    {type: 'line',smooth: true, name: '通道四', data: [27.1, 25.2, 24.4, 21.6, 18.5, 22.4]},
	    {type: 'line',smooth: true, name: '通道五', data: [25.2, 24.4, 21.6, 18.5, 22.4, 27.1]},
	    {type: 'line',smooth: true, name: '通道六', data: [24.4, 21.6, 18.5, 22.4, 27.1, 25.2]}
    ]
}
/*历史温度折线图(line-chart)【结束】*/
