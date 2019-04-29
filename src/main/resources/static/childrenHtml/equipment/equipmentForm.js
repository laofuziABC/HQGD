$('#channelTem').SetEditable({
	$addButton : $('#addBtn')
});
var globalUserList = [];
var bu = "<td name=\"buttons\"><div class=\"btn-group pull-right\"><button id=\"bEdit\" type=\"button\" class=\"btn btn-sm btn-default\" onclick=\"rowEdit(this);\"><span class=\"glyphicon glyphicon-pencil\"> </span></button><button id=\"bElim\" type=\"button\" class=\"btn btn-sm btn-default\" onclick=\"rowElim(this);\"><span class=\"glyphicon glyphicon-trash\"> </span></button><button id=\"bAcep\" type=\"button\" class=\"btn btn-sm btn-default\" style=\"display:none;\" onclick=\"rowAcep(this);\"><span class=\"glyphicon glyphicon-ok\"> </span></button><button id=\"bCanc\" type=\"button\" class=\"btn btn-sm btn-default\" style=\"display:none;\" onclick=\"rowCancel(this);\"><span class=\"glyphicon glyphicon-remove\"> </span></button></div></td>"
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
			channelTemList += "<td>" + v[i][0] + "</td>";
			channelTemList += "<td>" + v[i][1] + "</td>";
			channelTemList += "<td>" + v[i][2] + "</td>";
			channelTemList += "<td>" + v[i][3] + "</td>";
			channelTemList += bu;
			channelTemList += "</tr>"
		}
		$("#channelTemList1").html(channelTemList);
	} else {
		var channelTemList = "";
		channelTemList += "<tr>";
		channelTemList += "<td></td>";
		channelTemList += "<td></td>";
		channelTemList += "<td></td>";
		channelTemList += "<td></td>";
		channelTemList += bu;
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
	var channelTemList = "";
	channelTemList += "<tr>";
	channelTemList += "<td>CH1</td>";
	channelTemList += "<td></td>";
	channelTemList += "<td>80</td>";
	channelTemList += "<td>20</td>";
	channelTemList += bu;
	channelTemList += "</tr>";
	$("#channelTemList1").html(channelTemList);
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
	var mytable = document.getElementById("channelTem");
	var channelInfos = [];
	for (var i = 1, rows = mytable.rows.length; i < rows; i++) {
		for (var j = 0, cells = mytable.rows[i].cells.length; j < cells - 1; j++) {
			if (!channelInfos[i]) {
				channelInfos[i] = new Array();
			}
			channelInfos[i][j] = mytable.rows[i].cells[j].innerHTML;
		}
	}
	channelInfos.splice(0, 1);
	var res = JSON.stringify(channelInfos);
	$("#channelTem11").val(res);
	var userId = $("#userId").val();
	$("#userId").val(userId);
	var userName = $("#userId option:selected").text();
	$("#userName").val(userName);
	return true;
}

/*
 * Bootstable @description Javascript library to make HMTL tables editable,
 * using Bootstrap
 * 
 * @version 1.1 @autor Tito Hinostroza
 */
"use strict";
// Global variables
var params = null; // Parameters
var colsEdi = null;
var newColHtml = '<div class="btn-group pull-right">'
		+ '<button id="bEdit" type="button" class="btn btn-sm btn-default" onclick="rowEdit(this);">'
		+ '<span class="glyphicon glyphicon-pencil" > </span>'
		+ '</button>'
		+ '<button id="bElim" type="button" class="btn btn-sm btn-default" onclick="rowElim(this);">'
		+ '<span class="glyphicon glyphicon-trash" > </span>'
		+ '</button>'
		+ '<button id="bAcep" type="button" class="btn btn-sm btn-default" style="display:none;" onclick="rowAcep(this);">'
		+ '<span class="glyphicon glyphicon-ok" > </span>'
		+ '</button>'
		+ '<button id="bCanc" type="button" class="btn btn-sm btn-default" style="display:none;" onclick="rowCancel(this);">'
		+ '<span class="glyphicon glyphicon-remove" > </span>' + '</button>'
		+ '</div>';
var colEdicHtml = '<td name="buttons">' + newColHtml + '</td>';

$.fn.SetEditable = function(options) {
	var defaults = {
		columnsEd : null, // Index to editable columns. If null all td
		// editables. Ex.: "1,2,3,4,5"
		$addButton : null, // Jquery object of "Add" button
		onEdit : function() {
		}, // Called after edition
		onBeforeDelete : function() {
		}, // Called before deletion
		onDelete : function() {
		}, // Called after deletion
		onAdd : function() {
		} // Called when added a new row
	};
	params = $.extend(defaults, options);
	this
			.find('thead tr')
			.append(
					'<th name="buttons" value="操作" style="background: #399ad7;color: #f6f6f6;">操作</th>'); // encabezado
	// vacío
	this.find('tbody tr').append(colEdicHtml);
	var $tabedi = this; // Read reference to the current table, to resolve
	// "this" here.
	// Process "addButton" parameter
	if (params.$addButton != null) {
		// Se proporcionó parámetro
		params.$addButton.click(function() {
			rowAddNew($tabedi.attr("id"));
		});
	}
	// Process "columnsEd" parameter
	if (params.columnsEd != null) {
		// Extract felds
		colsEdi = params.columnsEd.split(',');
	}
};
function IterarCamposEdit($cols, tarea) {
	// Itera por los campos editables de una fila
	var n = 0;
	$cols.each(function() {
		n++;
		if ($(this).attr('name') == 'buttons')
			return; // excluye columna de botones
		if (!EsEditable(n - 1))
			return; // noe s campo editable
		tarea($(this));
	});

	function EsEditable(idx) {
		// Indica si la columna pasada está configurada para ser editable
		if (colsEdi == null) { // no se definió
			return true; // todas son editable
		} else { // hay filtro de campos
			// alert('verificando: ' + idx);
			for (var i = 0; i < colsEdi.length; i++) {
				if (idx == colsEdi[i])
					return true;
			}
			return false; // no se encontró
		}
	}
}
function FijModoNormal(but) {
	$(but).parent().find('#bAcep').hide();
	$(but).parent().find('#bCanc').hide();
	$(but).parent().find('#bEdit').show();
	$(but).parent().find('#bElim').show();
	var $row = $(but).parents('tr'); // accede a la fila
	$row.attr('id', ''); // quita marca
}
function FijModoEdit(but) {
	$(but).parent().find('#bAcep').show();
	$(but).parent().find('#bCanc').show();
	$(but).parent().find('#bEdit').hide();
	$(but).parent().find('#bElim').hide();
	var $row = $(but).parents('tr'); // accede a la fila
	$row.attr('id', 'editing'); // indica que está en edición
}
function ModoEdicion($row) {
	if ($row.attr('id') == 'editing') {
		return true;
	} else {
		return false;
	}
}
function rowAcep(but) {
	// Acepta los cambios de la edición
	var $row = $(but).parents('tr'); // accede a la fila
	var $cols = $row.find('td'); // lee campos
	if (!ModoEdicion($row))
		return; // Ya está en edición
	// Está en edición. Hay que finalizar la edición
	IterarCamposEdit($cols, function($td) { // itera por la columnas
		var cont = $td.find('input').val(); // lee contenido del input
		$td.html(cont); // fija contenido y elimina controles
	});
	FijModoNormal(but);
	/* params.onEdit($row); */
}
function rowCancel(but) {
	// Rechaza los cambios de la edición
	var $row = $(but).parents('tr'); // accede a la fila
	var $cols = $row.find('td'); // lee campos
	if (!ModoEdicion($row))
		return; // Ya está en edición
	// Está en edición. Hay que finalizar la edición
	IterarCamposEdit($cols, function($td) { // itera por la columnas
		var cont = $td.find('div').html(); // lee contenido del div
		$td.html(cont); // fija contenido y elimina controles
	});
	FijModoNormal(but);
}
function rowEdit(but) { // Inicia la edición de una fila
	var $row = $(but).parents('tr'); // accede a la fila
	var $cols = $row.find('td'); // lee campos
	if (ModoEdicion($row))
		return; // Ya está en edición
	// Pone en modo de edición
	IterarCamposEdit($cols, function($td) { // itera por la columnas
		var cont = $td.html(); // lee contenido
		var div = '<div style="display: none;">' + cont + '</div>'; // guarda
		// contenido
		var input = '<input class="form-control input-sm"  value="' + cont
				+ '">';
		$td.html(div + input); // fija contenido
	});
	FijModoEdit(but);
}
function rowElim(but) { // Elimina la fila actual
	var $row = $(but).parents('tr'); // accede a la fila
	/* params.onBeforeDelete($row); */
	$row.remove();
	/* params.onDelete(); */
}
function rowAddNew(tabId) { // Agrega fila a la tabla indicada.
	var $tab_en_edic = $("#" + tabId); // Table to edit
	var $filas = $tab_en_edic.find('tbody tr');
	if ($filas.length == 0) {
		// No hay filas de datos. Hay que crearlas completas
		var $row = $tab_en_edic.find('thead tr'); // encabezado
		var $cols = $row.find('th'); // lee campos
		// construye html
		var htmlDat = '';
		$cols.each(function() {
			if ($(this).attr('name') == 'buttons') {
				// Es columna de botones
				htmlDat = htmlDat + colEdicHtml; // agrega botones
			} else {
				htmlDat = htmlDat + '<td></td>';
			}
		});
		$tab_en_edic.find('tbody').append('<tr>' + htmlDat + '</tr>');
	} else {
		// Hay otras filas, podemos clonar la última fila, para copiar los
		// botones
		var $ultFila = $tab_en_edic.find('tr:last');
		$ultFila.clone().appendTo($ultFila.parent());
		$ultFila = $tab_en_edic.find('tr:last');
		var $cols = $ultFila.find('td'); // lee campos
		$cols.each(function() {
			if ($(this).attr('name') == 'buttons') {
				// Es columna de botones
			} else {
				$(this).html(''); // limpia contenido
			}
		});
	}
	params.onAdd();
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