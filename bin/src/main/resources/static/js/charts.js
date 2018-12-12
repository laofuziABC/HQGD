/* 设置统计图图表相关配置属性 (老夫子 2018.11.12) */

/*历史温度折线图(line-chart)【开始】*/
var lineOption={
	title: {text: '历史温度折线图', x: 'center'},
	xAxis: {type: 'category', data: [] },			//data: ['time1', 'time2', 'time3', 'time4', 'time5', 'time6']
    yAxis: {type: 'value', text:'温度'},
    tooltip: {trigger: 'axis'},
    legend: {top: 30, data:[] },						//data:['通道一','通道二','通道三','通道四','通道五','通道六']
    grid: {x: 30, y: 30, x2: 0, y2: 20},
    series: [
	    {type: 'line',smooth: true, name: '', data: []},
	    {type: 'line',smooth: true, name: '', data: []},
	    {type: 'line',smooth: true, name: '', data: []},
	    {type: 'line',smooth: true, name: '', data: []},
	    {type: 'line',smooth: true, name: '', data: []},
	    {type: 'line',smooth: true, name: '', data: []}
    ]
}
/*历史温度折线图(line-chart)【结束】*/

/*实时监控折线图(timelineOption)【开始】*/
//设定时间——每分钟更新
function randomData() {
    now = new Date(+now + 60 * 1000);
    value = Math.random() + 20;
    var valueName = now.getFullYear() + '/' + (now.getMonth() + 1) + '/' + now.getDate() 
    	+ ' ' + (now.getHours() >= 10 ? now.getHours() : '0' + now.getHours()) 
    	+ ':' + (now.getMinutes() >= 10 ? now.getMinutes() : '0' + now.getMinutes());
    return { name: now.toString(), value: [valueName, value] }
}
//设定常量
var data = [];
var now = +new Date("2018-10-01");
var value = Math.random() * 100;
for (var i = 0; i < 60; i++) {
    data.push(randomData());
}
//配置图表配置项
timelineOption = {
    title: { text: '实时监控数据统计图' },
    tooltip: {
        trigger: 'axis',
        formatter: function (params) {
            params = params[0];
            var date = new Date(params.name);
            var message = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+"&nbsp;"
            	+date.getHours() + '时' + date.getMinutes()+"分:&nbsp;"+params.value[1];
            return message;
        }
    },
    xAxis: { type: 'time', splitLine: { show: false } },
    yAxis: { type: 'value', boundaryGap: [0, '100%'] },
    series: [{
        name: '模拟数据', type: 'line',smooth: true,showSymbol: false, hoverAnimation: false,
        data: data
    }]
};

/*setInterval(function () {
    data.shift();
    data.push(randomData());

    timeLineChart.setOption({
        series: [{ data: data }]
    });
}, 1000);*/
/*实时监控折线图(timelineOption)【结束】*/