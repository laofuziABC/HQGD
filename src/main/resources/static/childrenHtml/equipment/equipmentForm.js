
var globalUserList = [];
if (add) {
	addEquip1();
} else {
	getEquiEdit1(parami);
}
function getUserList() {
	$.ajax({
		url : "user/selectAll",
		type : "post",
		dataType : "json",
		async : false,
		success : function(result) {
			globalUserList = result;

		}
	});
}
function getEquiEdit1(param) {
	if (globalData[param].channelTem != undefined) {
		var v = JSON.parse(globalData[param].channelTem);
		var channelTemList = "";
		for (let i = 0; i < v.length; i++) {
			channelTemList += "<tr>";
			channelTemList += "<td contenteditable='true'>" + v[i][0] + "</td>";
			channelTemList += "<td contenteditable='true'>" + v[i][1] + "</td>";
			channelTemList += "<td contenteditable='true'>" + v[i][2] + "</td>";
			channelTemList += "<td contenteditable='true'>" + v[i][3] + "</td>";
			channelTemList += "</tr>"
		}
		$("#channelTemList1").html(channelTemList);
	} else {
		var channelTemList = "";
		channelTemList += "<tr>";
		channelTemList += "<td contenteditable='true'></td>";
		channelTemList += "<td contenteditable='true'></td>";
		channelTemList += "<td contenteditable='true'></td>";
		channelTemList += "<td contenteditable='true'></td>";
		channelTemList += "</tr>"
		$("#channelTemList1").html(channelTemList);
	}
	if (globalUserList == "") {
		getUserList();
	}
	var userHtml = "<option value='" + globalData[param].userId + "' checked>"
			+ globalData[param].userName + "</option>";
	for (let i = 0; i < globalUserList.length; i++) {
		userHtml += "<option  value='" + globalUserList[i].id + "'>"
				+ globalUserList[i].userName + "</option>";
	}
	document.getElementById('type').value = globalData[param].type;
	$("#userId").html(userHtml);
	$("#equipmentId").val(globalData[param].equipmentId);
	$("#equipmentName").val(globalData[param].equipmentName);
	$("#address").val(globalData[param].address);
	$("#adcode").val(globalData[param].adcode);
	$("#lnglat").val(globalData[param].lngLat);
	$("#frameStru").val(globalData[param].frameStru);
	$("#numOfCh").val(globalData[param].numOfCh);
	$("#heartbeatId").val(globalData[param].heartbeatId);
	$("#heartbeatName").val(globalData[param].heartbeatName);
	$("#equipmentId").attr("readonly", "readonly");
}
function addEquip1() {
	add = true;

	if (globalUserList == "") {
		getUserList();
	}
	var userHtml = "<option value=\"\" checked>—请选择用户—</option>";
	for (let i = 0; i < globalUserList.length; i++) {
		userHtml += "<option value='" + globalUserList[i].id + "'>"
				+ globalUserList[i].userName + "</option>";
	}
	$("#userId").html(userHtml);

}
function backequlist() {
	$("#content-wrapper").load("childrenHtml/equipment/equipmentList.html");
}
// 提交设备信息表单
$(function() {
	$("#equipmentForm").ajaxForm(function(data) {
		if (data.result == "false") {
			alert(data.message);
		} else {
			backequlist();
		}
	});
});
function toVaild() {
	$("#add").val(add);
	var mytable = document.getElementById("channelTemList1");
	var channelInfos = [];
	for (var i = 0, rows = mytable.rows.length; i < rows; i++) {
		for (var j = 0, cells = mytable.rows[i].cells.length; j < cells; j++) {
			if (!channelInfos[i]) {
				channelInfos[i] = new Array();
			}
			channelInfos[i][j] = mytable.rows[i].cells[j].innerHTML;
		}
	}
	var res = JSON.stringify(channelInfos);
	$("#channelTem11").val(res);
	var userId = $("#userId").val();
	$("#userId").val(userId);
	var userName = $("#userId option:selected").text();
	$("#userName").val(userName);
	return true;
}
function generateForm() {
	var num = $("#numOfCh").val();
	var thtml = "";
	for (let i = 0; i < num; i++) {
		thtml += "<tr>";
		thtml += "<td contenteditable='true' >CH" + (i + 1) + "</td>";
		thtml += "<td contenteditable='true' ></td>";
		thtml += "<td contenteditable='true' >120</td>";
		thtml += "<td contenteditable='true' >-20</td>";
		thtml += "</tr>";
	}
	$("#channelTemList1").html(thtml);
}

function TableToCSV(tabId, separator) { // Convierte tabla a CSV
	var datFil = '';
	var tmp = '';
	var $tab_en_edic = $("#" + tabId); // Table source
	$tab_en_edic.find('tbody tr').each(function() {
		// Termina la edición si es que existe
		if (ModoEdicion($(this))) {
			$(this).find('#bAcep').click(); // acepta edición
		}
		var $cols = $(this).find('td'); // lee campos
		datFil = '';
		$cols.each(function() {
			if ($(this).attr('name') == 'buttons') {
				// Es columna de botones
			} else {
				datFil = datFil + $(this).html() + separator;
			}
		});
		if (datFil != '') {
			datFil = datFil.substr(0, datFil.length - separator.length);
		}
		tmp = tmp + datFil + '\n';
	});
	return tmp;
}