/* 设置统计图图表相关配置属性 (老夫子 2018.11.12) */

/*历史温度折线图(line-chart)【开始】*/
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

/*实时监控折线图(timelineOption)【开始】*/
//设定时间——每分钟更新
function randomData(val) {
    now = new Date(+now + 60 * 1000);
    value = value + Math.random() * 21 - 10;
    var valueName = now.getFullYear() + '/' + (now.getMonth() + 1) + '/' + now.getDate() 
    	+ ' ' + (now.getHours() >= 10 ? now.getHours() : '0' + now.getHours()) 
    	+ ':' + (now.getMinutes() >= 10 ? now.getMinutes() : '0' + now.getMinutes());
    return {
        name: now.toString(),
        value: [valueName, Math.round(value)]
    }
}
//设定变量
var data = [];
// 给定某天的00:00
var now = +new Date('2018-07-02 00:00:00');
var n_now = new Date(now);
var n_day = n_now.getDate();
// 获取当前时间
var c_now = new Date();
var c_day = c_now.getDate();
var c_month = c_now.getMonth() + 1;
var c_year = c_now.getFullYear();
var value = Math.random() * 1000;
//给定日期与当天日期不符，使用当前时间
if (n_day !== c_day) {
    now = +new Date(c_year + '-' + c_month + '-' + c_day + ' ' + '00:00:00');
}
//初始化有多少个点
var num = Math.floor((+new Date() - now) / 60000) <= 1 ? 1 : Math.floor((+new Date() - now) / 60000);
for (var i = 0; i < num; i++) {
    data.push(randomData());
}
//开始设定图表配置项
timelineOption = {
    title: {text: '实时数据监测'},
    tooltip: {
        trigger: 'axis',
        formatter: function(params) {
            params = params[0];
            var date = new Date(params.name);
            var message = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+"&nbsp;"
            	+date.getHours() + '时' + date.getMinutes()+"分:&nbsp;"+params.value[1];
            return message;
        }
    },
    xAxis: { type: 'time', splitLine: { show: false } },
    yAxis: { type: 'value', boundaryGap: [0, '100%'], splitLine: { show: false } },
    grid: {x: 30, y: 30, x2: 30, y2: 20},
    series: [
        { name: '模拟数据', type: 'line', showSymbol: false, hoverAnimation: false, data: data }
    ]
};
//给定日期与当天日期不符,刷新页面从新的一天开始
var timer = setInterval(function() {
    if ((new Date(now)).getDate() !== c_day) { 
        window.history.go(0)
    }
    data.push(randomData());
    myChart.setOption({
        series: [
        	{data: data }
        ]
    });
}, 60000);
/*实时监控折线图(timelineOption)【结束】*/