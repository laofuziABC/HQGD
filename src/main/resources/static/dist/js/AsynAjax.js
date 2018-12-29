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
//异步获取设备/用户列表
function POSTAjaxForAllResultList(url,param){
	var allResultList={};
	if(url!=null && url!=""){
		$.ajax({
			url: url, type: "post", data: param, dataType: "json",
			success: function(result){allResultList=result; }
		});
	}
	return allResultList;
}
//封装数据监测页面的Ajax异步请求
var equiId;		//设备主键
var userParam=document.getElementById("userParam");
var addrParam=document.getElementById("addrParam");
var typeParam=document.getElementById("typeParam");
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
					equiId=data[0].equipmentId;
					for(let i=0; i<totalSize; i++){
						resultList+="<li onclick=\"getEquiData('"+data[i].equipmentId+"');\"><span><em>"+data[i].equipmentName+"</em></span></li>";
					}
				}
				$("#equipResultList").html(resultList);
			}
		});
	}
	userParam.style.display="none";
	addrParam.style.display="none";
	typeParam.style.display="none";
}
