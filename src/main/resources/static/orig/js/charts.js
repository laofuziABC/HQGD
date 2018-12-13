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
function randomData(val) {
    now = new Date(+now + 60 * 1000);
    value = Math.random()*10 + val;
    var valueName = now.getFullYear() + '/' + (now.getMonth() + 1) + '/' + now.getDate() 
    	+ ' ' + (now.getHours() >= 10 ? now.getHours() : '0' + now.getHours()) 
    	+ ':' + (now.getMinutes() >= 10 ? now.getMinutes() : '0' + now.getMinutes());
    return { name: now.toString(), value: [valueName, Math.round(value)] }
}
//设定常量
var data = [];
var data1=[],data2=[],data3=[],data4=[],data5=[],data6=[];
// 获取当前时间
var c_now = new Date();
var c_year = c_now.getFullYear();
var c_month = c_now.getMonth() + 1;
var c_day = c_now.getDate();
var c_hour = c_now.getHours();
var c_minute = c_now.getMinutes();
var c_second = c_now.getSeconds();
//给定某天的00:00，使用当前时间
//var now = +new Date(c_year + '-' + c_month + '-' + (c_day-1) + ' ' + '00:00:00');
var now = +new Date();
var value = Math.random() * 1000;
//初始化有多少个点
//var num = Math.floor((+new Date() - now) / 60000) <= 1 ? 1 : Math.floor((+new Date() - now) / 60000);
for (var i = 0; i < 240; i++) {
	data.push(randomData(20));
    data1.push(randomData(22));
    data2.push(randomData(30));
    data3.push(randomData(25));
    data4.push(randomData(15));
    data5.push(randomData(18));
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
    legend: {top: 30, data:['CH1','CH2','CH3','CH4','CH5','CH6'] },
    color: ["#4977FE", "#40D0FF", "#87AF10", "#FFA200", "#26e4a8", "#125622"],
    grid: {x: 30, y: 30, x2: 50, y2: 50},
    series: [
    	{name: 'CH1', type: 'line', itemStyle: {normal: {lineStyle: {width:1.5 }}}, showSymbol: false, hoverAnimation: false,data: [] },
    	{name: 'CH2', type: 'line', itemStyle: {normal: {lineStyle: {width:1.5 }}}, showSymbol: false, hoverAnimation: false,data: [] },
    	{name: 'CH3', type: 'line', itemStyle: {normal: {lineStyle: {width:1.5 }}}, showSymbol: false, hoverAnimation: false,data: [] },
    	{name: 'CH4', type: 'line', itemStyle: {normal: {lineStyle: {width:1.5 }}}, showSymbol: false, hoverAnimation: false,data: [] },
    	{name: 'CH5', type: 'line', itemStyle: {normal: {lineStyle: {width:1.5 }}}, showSymbol: false, hoverAnimation: false,data: [] },
    	{name: 'CH6', type: 'line', itemStyle: {normal: {lineStyle: {width:1.5 }}}, showSymbol: false, hoverAnimation: false,data: [] }
    ]
};

/*setInterval(function () {
    data.shift();
    data.push(randomData());

    timeLineChart.setOption({
        series: [{ data: data }]
    });
}, 1000);*/
/*实时监控折线图(timelineOption)【结束】*/