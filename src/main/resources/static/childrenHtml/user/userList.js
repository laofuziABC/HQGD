$("#userfuceng").css({
	"display" : "none"
});
$(function() {
	$("#userForm").ajaxForm(function(data) {
		if (data.result == "false") {
			alert(data.message);
		} else {
			getUserList();
			$(".fuceng").css({
				"display" : "none"
			})
			alert("操作成功");
		}
	});
});
function check() {
	var password = $("#password").val();
	var confirm = $("#confirm").val();
	$("#add").val(add);
	if (password != confirm) {
		alert("两次密码不一致！");
		return false;
	}
}

jQuery(document).ready(function() {
	getUserList();
});
var globalResult;
var add = true;
// 获取用户列表
function getUserList() {
	$
			.ajax({
				url : "user/selectAll",
				type : "post",
				dataType : "json",
				success : function(result) {
					globalResult = result;
					var userlist = "";
					// 设置分页信息——当前为假分页
					var totalSize = result.length;
					var pageSize = 15;
					var totalNum = Math.ceil(totalSize / pageSize);
					var startIndex = (pageNum - 1) * pageSize;
					var endIndex = (pageNum * pageSize > totalSize) ? totalSize
							: pageNum * pageSize;
					for (let i = startIndex; i < endIndex; i++) {
						userlist += "<tr class=\"\">";
						userlist += "<td>" + (i + 1) + "</td>";
						userlist += "<td>" + result[i].userName + "</td>";
						userlist += "<td>" + result[i].tel + "</td>";
						userlist += "<td>" + result[i].comments + "</td>";
						userlist += "<td>" + result[i].createTime + "</td>";
						userlist += "<td>" + result[i].updateTime + "</td>";
						userlist += "<td>" + result[i].updater + "</td>";
						if (result[i].isdel == "N") {
							userlist += "<td>启用</td>";
						} else {
							userlist += "<td>禁用</td>";
						}
						userlist += "<td class=\"center\" >";
						userlist += "<a class=\" edit\" onclick=\"getUserEdit('"
								+ i + "');\">编辑</a>&nbsp;";
						userlist += "<a class=\"delete\" href=\"#\" onclick=\"getUserDelete('"
								+ result[i].id + "');\">删除</a>&nbsp;";
						userlist += "<a class=\"resetpsw\" href=\"#\" onclick=\"initUserPassword('"
								+ result[i].id + "');\">密码初始化</a>";
						userlist += "</td>";
						userlist += "</tr>";
					}
					$("#userList").html(userlist);
					// 加载页码
					$("#user-show-pager").html(
							loadPages(pageSize, totalSize, pageNum));
					$("#user-show-pager").setActivePage(2);
				}
			});
}
function close2() {
	$("#userfuceng").css({
		"display" : "none"
	});

}
function add1() {
	add = true;
	$("#userfuceng").css({
		"display" : "block"
	});
}
// 编辑用户
function getUserEdit(param) {
	add = false;
	$("#userfuceng").css({
		"display" : "block"
	});
	$("#id").val(globalResult[param].id);
	$("#username").val(globalResult[param].userName);
	$("#tel").val(globalResult[param].tel);
	$("#password").val(globalResult[param].password);
	$("#confirm").val(globalResult[param].password);
	document.getElementById('isdel').value = globalResult[param].isdel;
	$("#comments").val(globalResult[param].comments);
}
// 删除用户
function getUserDelete(param) {
	if (confirm("确定删除此用户？")) {
		$.ajax({
			url : "user/delete",
			type : "post",
			data : {
				"userId" : param
			},
			dataType : "json",
			success : function(result) {
				if (!result.success) {
					alert("删除失败！");
				}
				getUserList();
			}
		});
	}
}
// 用户密码初始化
function initUserPassword(param) {
	userId = param;
	if (confirm("确认将该用户的密码初始化？")) {
		$.ajax({
			url : "user/initUserPassword",
			type : "post",
			data : {
				"userId" : param
			},
			dataType : "json",
			success : function(result) {
				if (result.success) {
					alert("密码初始化成功！");
				} else {
					alert("密码初始化失败！");
				}
				getUserList();
			}
		});
	}
}
// 搜索用户
function searchResult() {
	var name = $("#searchByName").val().trim();
	if (name != "") {
		$
				.ajax({
					url : "user/selectByUserName",
					type : "post",
					data : {
						"userName" : name
					},
					dataType : "json",
					success : function(result) {
						// alert("数据到前台");
						var userlist = "";
						for (let i = 0; i < result.length; i++) {
							userlist += "<tr class=\"\">";
							userlist += "<td>" + (i + 1) + "</td>";
							userlist += "<td>" + result[i].userName + "</td>";
							userlist += "<td>" + result[i].tel + "</td>";
							userlist += "<td>" + result[i].comments + "</td>";
							userlist += "<td>" + result[i].createTime + "</td>";
							userlist += "<td>" + result[i].updateTime + "</td>";
							userlist += "<td>" + result[i].updater + "</td>";
							if (result[i].isdel == "N") {
								userlist += "<td>启用</td>";
							} else {
								userlist += "<td>禁用</td>";
							}
							userlist += "<td class=\"center\" >";
							userlist += "<a class=\" lindianjia\" >编辑</a>&nbsp;";
							userlist += "<a class=\"delete\" href=\"#\" onclick=\"getUserDelete('"
									+ result[i].id + "');\">删除</a>&nbsp;";
							userlist += "<a class=\"resetpsw\" href=\"#\" onclick=\"initUserPassword('"
									+ result[i].id + "');\">密码初始化</a>";
							userlist += "</td>";
							userlist += "</tr>";
						}
						$("#userList").html(userlist);
					}
				});
	}
}
// 修改用户密码
function updatePsw() {
	let title = "修改密码";
	let url = "formHtml/user/updatePass.html";
	var params = {
		"width" : 600,
		"height" : 250
	};
	parent.openModal(title, url, params);

}

function report() {
	var result = globalResult;
	var userlist = "<tr><td>序号</td><td>用户名</td><td>联系方式</td><td>用户属性</td><td>创建时间</td><td>更新时间</td>	<td>更新者</td><td>状态</td></tr>";
	for (let i = 0; i < result.length; i++) {
		userlist += "<tr class=\"\">";
		userlist += "<td>" + (i + 1) + "</td>";
		userlist += "<td>" + result[i].userName + "</td>";
		userlist += "<td>" + result[i].tel + "</td>";
		userlist += "<td>" + result[i].comments + "</td>";
		userlist += "<td>" + result[i].createTime + "</td>";
		userlist += "<td>" + result[i].updateTime + "</td>";
		userlist += "<td>" + result[i].updater + "</td>";
		if (result[i].isdel == "N") {
			userlist += "<td>启用</td>";
		} else {
			userlist += "<td>禁用</td>";
		}
		userlist += "</tr>";
	}
	$("#userListreport").html(userlist);

	$("#userListreport").table2excel({
		exclude : ".noExl",
		name : "Excel Document Name",
		filename : "用户信息",
		exclude_img : true,
		exclude_links : true,
		exclude_inputs : true
	});
}