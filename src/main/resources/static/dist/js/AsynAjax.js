/**
 * 封装Ajax异步请求方法
 * @param url				请求的地址，String类型
 * @param param		请求参数，Object类型
 * @param message		返回操作信息，String类型
 * @param mark			是否分页，Boolean类型
 * laofuzi 2018.12.11
 */
//页面弹框提示操作是否成功
function POSTAjaxForAlert(url,param,message){
	if(url!=null && url!=""){
		$.ajax({
			url: url,
			type: "post",
			data: param,
			dataType: "json",
			success: function(result){
				if(result.success){ alert(message+"操作成功！"); }
				else{ alert(message+"操作失败！"); }
			}
		});
	}
}
//异步获取用户列表
function POSTAjaxForUserList(url,param,mark){
	if(url!=null && url!=""){
		$.ajax({
			url: url,
			type: "post",
			data: param,
			dataType: "json",
			success: function(result){
				var userlist = "";
				//设置分页信息——当前为假分页
				var totalSize = result.length;
				var pageSize = 5;
				var totalNum = Math.ceil(totalSize/pageSize);
				var startIndex = (pageNum-1)*pageSize;
				var endIndex = (pageNum*pageSize>totalSize)?totalSize:pageNum*pageSize;
				for(let i=startIndex; i<endIndex; i++){
					userlist += "<tr class=\"\">";
					userlist += "<td>"+(i+1)+"</td>";
					userlist += "<td>"+result[i].userName+"</td>";
					userlist += "<td>"+result[i].comments+"</td>";
					userlist += "<td>"+parent.formatDateToString(new Date(result[i].createTime))+"</td>";
					userlist += "<td>"+parent.formatDateToString(new Date(result[i].updateTime))+"</td>";
					userlist += "<td>"+result[i].updater+"</td>";
					if(result[i].isdel == "N"){userlist += "<td>启用</td>"; }
					else{userlist += "<td>已删除</td>";}
					userlist += "<td class=\"center\">";
					userlist += "<a class=\"edit\" href=\"#\" onclick=\"getUserEdit('"+result[i].id+"');\">编辑</a>&nbsp;";
					userlist += "<a class=\"delete\" href=\"#\" onclick=\"getUserDelete('"+result[i].id+"');\">删除</a>&nbsp;";
					userlist += "<a class=\"resetpsw\" href=\"#\" onclick=\"initUserPassword('"+result[i].id+"');\">密码初始化</a>";
					userlist += "</td>";
					userlist += "</tr>";
				}
				$("#userList").html(userlist);
				if(mark){
					//加载页码
					$("#user-list-pager").html(loadPages(pageSize,totalSize,pageNum));
					$("#user-list-pager").setActivePage(2);
				}
			}
		});
	}
}
//异步获取设备列表
function POSTAjaxForEquiList(url,param,mark){
	if(url!=null && url!=""){
		$.ajax({
			url: url,
			type: "post",
			data: param,
			dataType: "json",
			success: function(result){
				var data = result.data;
				//设置分页信息——当前为假分页
				var equipList = "";
				var totalSize = data.length;
				var pageSize = 5;
				var totalNum = Math.ceil(totalSize/pageSize);
				var startIndex = (pageNum-1)*pageSize;
				var endIndex = (pageNum*pageSize>totalSize)?totalSize:pageNum*pageSize;
				for(let i=startIndex; i<endIndex; i++){
					equipList+="<tr>";
					equipList+="<td>"+(i+1)+"</td>";
					equipList+="<td>"+data[i].equipmentName+"</td>";
					equipList+="<td>"+data[i].address+"</td>";
					equipList+="<td>"+data[i].adcode+"</td>";
					equipList+="<td>"+data[i].lngLat+"</td>";
					equipList+="<td>"+data[i].frameStru+"</td>";
					equipList+="<td>"+data[i].userName+"</td>";
					equipList+="<td>"+parent.formatDateToString(new Date(data[i].createTime))+"</td>";
					equipList+="<td>"+parent.formatDateToString(new Date(data[i].updateTime))+"</td>";
					equipList+="<td>"+data[i].updater+"</td>";
					equipList+="<td class=\"center\">";
					equipList+="<a class=\"edit\" href=\"#\" onclick=\"getEquiEdit('"+data[i].equipmentId+"');\">编辑</a>&nbsp;";
					equipList+="<a class=\"delete\" href=\"#\"onclick=\"getEquiDelete('"+data[i].equipmentId+"');\">删除</a>&nbsp;";
					equipList+="</td>";
					equipList+="</tr>";
				}
				$("#equipmentList").html(equipList);
				if(mark){
					//加载页码
					$("#equi-list-pager").html(loadPages(pageSize,totalSize,pageNum));
					$("#equi-list-pager").setActivePage(3);
				}
			}
		});
	}
}
//封装数据监测页面的Ajax异步请求
var equiId;		//设备主键
function AjaxPostForEquiSubList(url,param){
	var resultList = "";
	if(url != null && url != ""){
		$.ajax({
			url: url,
			type: "post",
			data: param,
			dataType: "json",
			success: function(result){
				var data = result.data;
				var totalSize = data.length;
				//根据设备列表，初始化第一台设备的展示信息
				if(totalSize>0){
//					var pageSize = 5;
//					var totalNum = Math.ceil(totalSize/pageSize);
//					var startIndex = (pageNum-1)*pageSize;
//					var endIndex = (pageNum*pageSize>totalSize)?totalSize:pageNum*pageSize;
//					equiId=data[startIndex].equipmentId;
//					for(let i=startIndex; i<endIndex; i++){
					equiId=data[0].equipmentId;
					for(let i=0; i<totalSize; i++){
						resultList+="<li onclick=\"getEquiData('"+data[i].equipmentId+"');\"><span><em>"+data[i].equipmentName+"</em></span></li>";
					}
					$("#equipResultList").html(resultList);
					getEquiData(equiId);
//					//加载页码
//					$("#sub-list-pager").html(loadSubListPages(pageSize,totalSize,pageNum));
//					$("#sub-list-pager").setSubListPage();
				}else{
					$("#equipResultList").html(resultList);
					getEquiData(null);
				}
			}
		});
	}
}
