//每分钟获取所有设备的实时数据，判断有没有异常
function allEquipRealtime() {
	var timing = setInterval(function() {
		myTimer()
	}, 30000);
	// 清除页面多余的定时任务
	/*var start = (timing - 5000 > 0) ? (timing - 5000) : 0;
	for (var i = start; i < timing; i++) {
		clearInterval(i);
	}*/

}
function myTimer() {
	var userName = parent.getParam("userName");
	var roleId = parent.getParam("roleId");
	//debugger;
	$.ajax({
		url : "dataAcquisition/allEquipRealtime",
		type : "post",
		data : {
			"userName" : userName,
			"roleId" : roleId
		},
		dataType : "json",
		success : function(result) {
			var data = result.data;
			if (data.length > 0) {
				floatWindow();
			}
		}
	});

}

function floatWindow() {
	//debugger;
	$(".conent").css({
		"display" : "block"
	});
	var timer = null;/* 定时器 */
	var _left = 0;/* 默认left距离 */
	var _top = 20;/* 默认top距离 */
	var top_folg = false;/* 控制高度-锁 */
	var left_folg = true;/* 控制宽度-锁 */
	var win_width = $(window).width() - $(".conent").width();/* 获取并计算浏览器初始宽度 */
	var win_height = $(window).height() - $(".conent").height();/* 获取并计算浏览器初始高度 */
	$("#liulan").html(win_height + "px");
	$("#sumwid").html(win_width + "px");
	;
	action();/* 执行走动 */
	$(".conent").mouseover(function() {
		clearInterval(timer);
		$(this).find(".c_adver").css({
			"background" : "url('vince/images/fudong/no.gif')",
			"bakcground-repeat" : "no-repeat"
		});
		$(this).find(".txt").text("放开我!!!");

	}).mouseout(function() {
		action();
		$(this).find(".txt").text("1号测温仪发生温度超标异常，请及时检查");
		$(this).find(".c_adver").css({
			"background" : "url('vince/images/fudong/back.gif')",
			"bakcground-repeat" : "no-repeat"
		});
	});

	$(window).resize(function() {
		conobj = $(".conent");
		win_width = $(window).width() - conobj.width();
		win_height = $(window).height() - conobj.height();
		$("#liulan").html(win_height + "px");
		$("#sumwid").html(win_width + "px");
		;
	});

	function action() {
		timer = setInterval(function() {
			if (!top_folg) {
				_top++;
				if (_top >= win_height) {
					top_folg = true;
				}
				;
			} else {
				_top--;
				if (_top <= 0) {
					top_folg = false;
				}
				;
			}
			;
			if (left_folg) {
				_left++;
				if (_left >= win_width) {
					left_folg = false;
				}
				;
			} else {
				_left--;
				if (_left <= 0) {
					left_folg = true;
				}
				;
			}
			;
			$("#textone").html(_top + "px");
			$("#timewid").html(_left + "px");
			$(".conent").animate({
				left : _left,
				top : _top
			}, 3);
		}, 15);
	}
	;

	$(".conent .c_adver").dblclick(function() {
		$(this).parents(".conent").slideUp(500, function() {
			$(this).remove();
			clearInterval(timer);
		});
	});

	var state;/* 拖动锁 */
	$(".c_header").mousedown(function(event) {
		state = false;
		var x = event.clientX;
		var y = event.clientY;
		var obj = $(this).parents(".conent");
		var c_left = obj.offset().left;
		var c_top = obj.offset().top;
		$(document).mousemove(function(e) {
			if (!state) {
				var x1 = e.clientX;
				var y1 = e.clientY;
				var action_left = x1 - x + c_left;
				var action_top = y1 - y + c_top;
				if (action_left <= 0) {
					action_left = 0;
				}
				;
				if (action_top <= 0) {
					action_top = 0;
				}
				if (action_left >= win_width) {
					action_left = win_width;
				}
				;
				if (action_top >= win_height) {
					action_top = win_height;
				}
				;
				obj.css({
					top : action_top,
					left : action_left
				});
				_left = action_left;
				_top = action_top;
				$("#text").html(_top + "px");
				$("#dywid").html(_left + "px");
			}
			;
		}).mouseup(function() {
			state = true;
		});
	});
}