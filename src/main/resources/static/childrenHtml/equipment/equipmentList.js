jQuery(document).ready(function() {
	var roleId = parent.getParam("roleId");
	var userName = parent.getParam("userName");
	if (roleId == "1") {
		getEquipmentList("1", null);
	} else {
		getEquipmentList("0", userName);
	}

});
var globalData;
var add;
var parami;
// 获取所有设备信息列表
function getEquipmentList(p1, userName) {
	if (p1 == "1") {
		path = "selectAll";
	} else {
		path = "searchEquiByParam";
	}
	$
			.ajax({
				url : "equipment/" + path,
				type : "post",
				dataType : "json",
				data : {
					"userName" : userName
				},
				success : function(result) {
					globalData = result.data;
					var data = result.data;
					var equipList = "";
					// 设置分页信息——当前为假分页
					var totalSize = data.length;
					var pageSize = 15;
					var totalNum = Math.ceil(totalSize / pageSize);
					var startIndex = (pageNum - 1) * pageSize;
					var endIndex = (pageNum * pageSize > totalSize) ? totalSize
							: pageNum * pageSize;
					equipList = getequipList(startIndex, endIndex, data,
							equipList);
					$("#equipmentList").html(equipList);
					// 加载页码
					$("#equi-show-pager").html(
							loadPages(pageSize, totalSize, pageNum));
					$("#equi-show-pager").setActivePage(3);
				}
			});
}
function getequipList(startIndex, endIndex, data, equipList) {
	for (let i = startIndex; i < endIndex; i++) {
		equipList += "<tr>";
		equipList += "<td>" + (i + 1) + "</td>";
		equipList += "<td>" + data[i].equipmentId + "</td>";
		equipList += "<td>" + data[i].equipmentName + "</td>";
		if (data[i].type == '1') {
			equipList += "<td>开关柜</td>";
		} else if (data[i].type == '2') {
			equipList += "<td>干式变压器</td>";
		} else if (data[i].type == '3') {
			equipList += "<td>变压器绕组</td>";
		} else if (data[i].type == '3') {
			equipList += "<td>环网柜</td>";
		}
		equipList += "<td colspan=\"1\" class=\"text_al_rig\"><a href=\"#\" class=\"lindianjia\" onclick=\"getChan('"
				+ i + "')\">详情</a></td>";
		equipList += "<td>" + data[i].numOfCh + "</td>";
		equipList += "<td>" + data[i].address + "</td>";
		equipList += "<td>" + data[i].adcode + "</td>";
		equipList += "<td>" + data[i].lngLat + "</td>";
		equipList += "<td>" + data[i].frameStru + "</td>";
		equipList += "<td>" + data[i].userName + "</td>";
		equipList += "<td>" + data[i].heartbeatId + "</td>";
		equipList += "<td>" + data[i].updateTime + "</td>";
		equipList += "<td>" + data[i].updater + "</td>";
		equipList += "<td class=\"center\">";
		equipList += "<a class=\" edit\" onclick=\"getEquiEdit('" + i
				+ "');\">编辑</a>&nbsp;";
		equipList += "<a class=\"delete\" href=\"#\"onclick=\"getEquiDelete('"
				+ data[i].equipmentId + "&" + data[i].type
				+ "');\">删除</a>&nbsp;";
		equipList += "</td>";
		equipList += "</tr>";
	}
	return equipList;
};

// 温度值域
function getChan(param) {
	$("#equipmentfuceng").css({
		"display" : "block"
	});
	if (globalData[param].channelTem != undefined) {
		var v = JSON.parse(globalData[param].channelTem);
		var channelTemList = "";
		for (let i = 0; i < v.length; i++) {
			channelTemList += "<tr>";
			channelTemList += "<td>" + v[i][0] + "</td>";
			channelTemList += "<td>" + v[i][1] + "</td>";
			channelTemList += "<td>" + v[i][2] + "</td>";
			channelTemList += "<td>" + v[i][3] + "</td>";
			channelTemList += "</tr>"
		}
		$("#channelTemList").html(channelTemList);
	} else {
		var channelTemList = "";
		channelTemList += "<tr>";
		channelTemList += "<td></td>";
		channelTemList += "<td></td>";
		channelTemList += "<td></td>";
		channelTemList += "<td></td>";
		channelTemList += "</tr>"
		$("#channelTemList").html(channelTemList);
	}
}
function addEquip() {
	add = true;
	$("#equip-content-wrapper").load(
			"childrenHtml/equipment/equipmentForm.html");
}
function getEquiEdit(param) {
	add = false;
	parami = param;
	$("#equip-content-wrapper").load(
			"childrenHtml/equipment/equipmentForm.html");
}

// 删除一条设备信息
function getEquiDelete(param) {
	if (confirm("确定删除此设备？")) {
		$.ajax({
			url : "equipment/delete",
			type : "post",
			data : {
				"param" : param
			},
			dataType : "json",
			success : function(result) {
				if (!result.success) {
					alert("设备删除失败！");
				}
				getEquipmentList();
			}
		});
	}
}

// 通过设备名称查询设备
function searchResult() {
	var name = $("#searchByName").val().trim();
	var type = $("#searchtype option:selected").val();

	$.ajax({
		url : "equipment/searchEquiByParam",
		type : "post",
		data : {
			"equipmentName" : name,
			"type" : type
		},
		dataType : "json",
		success : function(result) {
			globalData = result.data;
			var data = result.data;
			var equipList = "";
			equipList = getequipList(0, data.length, data, equipList);
			$("#equipmentList").html(equipList);
		}
	});

}
// 导出
function report() {
	var equipList = "<tr><td>设备ID</td><td>设备名称</td><td>设备类型</td><td>通道温度</td><td>通道数</td><td>安装地址</td><td>地区编码</td><td>经纬度</td><td>帧结构</td>"
			+ "<td>所属用户</td><td>心跳包ID</td><td>心跳包名称</td><td>更新时间</td><td>更新者</td></tr>";
	var data = globalData;
	for (let i = 0; i < data.length; i++) {
		equipList += "<tr>";
		equipList += "<td>" + data[i].equipmentId + "</td>";
		equipList += "<td>" + data[i].equipmentName + "</td>";
		equipList += "<td>" + data[i].type + "</td>";
		equipList += "<td>" + data[i].channelTem + "</td>";
		equipList += "<td>" + data[i].numOfCh + "</td>";
		equipList += "<td>" + data[i].address + "</td>";
		equipList += "<td>" + data[i].adcode + "</td>";
		equipList += "<td>" + data[i].lngLat + "</td>";
		equipList += "<td>" + data[i].frameStru + "</td>";
		equipList += "<td>" + data[i].userName + "</td>";
		equipList += "<td>" + data[i].heartbeatId + "</td>";
		equipList += "<td>" + data[i].heartbeatName + "</td>";
		equipList += "<td>" + data[i].heartbeatId + "</td>";
		equipList += "<td>" + data[i].heartbeatName + "</td>";
		equipList += "<td>" + data[i].updateTime + "</td>";
		equipList += "<td>" + data[i].updater + "</td>";
		equipList += "</tr>";
	}
	$("#equipmentreport").html(equipList);
	$("#equipmentreport").table2excel({
		exclude : ".noExl",
		name : "Excel Document Name",
		filename : "设备信息",
		exclude_img : true,
		exclude_links : true,
		exclude_inputs : true
	});
}