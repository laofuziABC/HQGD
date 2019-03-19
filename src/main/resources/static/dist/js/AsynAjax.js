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
var equiId;			//设备主键
var userParam=document.getElementById("userParam");
function AjaxPostForEquiSubList(url,param){
	var kgg_list = "";			//开关柜
	var gsbyq_list = "";		//干式变压器
	var byqrz_list = "";		//变压器绕阻
	var hwg_list = "";			//环网柜
	if(url != null && url != ""){
		$.ajax({
			url: url,
			type: "post",
			data: param,
			dataType: "json",
			async:false,
			success: function(result){
				var data = result.data;
				var totalSize = data.length;
				//根据设备列表，初始化第一台设备的展示信息
				if(totalSize>0){
					equiId=data[0].equipmentId;
					var activeStr="<li class=\"checked\" onclick=\"getEquiData('"+equiId+"');\"><span><em>"+data[0].equipmentName+"</em></span></li>";
					switch(data[0].type){
						case "1": kgg_list+=activeStr; $("#kgglist").removeClass("hide"); break;
						case "2": gsbyq_list+=activeStr; $("#gsbyqlist").removeClass("hide"); break;
						case "3": byqrz_list+=activeStr; $("#byqrzlist").removeClass("hide"); break;
						case "4": hwg_list+=activeStr; $("#hwglist").removeClass("hide"); break;
					}
					for(let i=1; i<totalSize; i++){
						switch(data[i].type){
							case "1": kgg_list+="<li class=\"\" onclick=\"getEquiData('"+data[i].equipmentId+"');\"><span><em>"+data[i].equipmentName+"</em></span></li>"; break;
							case "2": gsbyq_list+="<li class=\"\" onclick=\"getEquiData('"+data[i].equipmentId+"');\"><span><em>"+data[i].equipmentName+"</em></span></li>"; break;
							case "3": byqrz_list+="<li class=\"\" onclick=\"getEquiData('"+data[i].equipmentId+"');\"><span><em>"+data[i].equipmentName+"</em></span></li>"; break;
							case "4": hwg_list+="<li class=\"\" onclick=\"getEquiData('"+data[i].equipmentId+"');\"><span><em>"+data[i].equipmentName+"</em></span></li>"; break;
						}
					}
					kgg_list=(kgg_list.length==0)?("<li class=\"disabled\"><span><em style=\"color: red;\">无相关设备</em></span></li>"):(kgg_list);
					gsbyq_list=(gsbyq_list.length==0)?("<li class=\"disabled\"><span><em style=\"color: red;\">无相关设备</em></span></li>"):(gsbyq_list);
					byqrz_list=(byqrz_list.length==0)?("<li class=\"disabled\"><span><em style=\"color: red;\">无相关设备</em></span></li>"):(byqrz_list);
					hwg_list=(hwg_list.length==0)?("<li class=\"disabled\"><span><em style=\"color: red;\">无相关设备</em></span></li>"):(hwg_list);
				}else{
					kgg_list+="<li class=\"disabled\"><span><em style=\"color: red;\">无相关设备</em></span></li>";
					gsbyq_list+="<li class=\"disabled\"><span><em style=\"color: red;\">无相关设备</em></span></li>";
					byqrz_list+="<li class=\"disabled\"><span><em style=\"color: red;\">无相关设备</em></span></li>";
					hwg_list+="<li class=\"disabled\"><span><em style=\"color: red;\">无相关设备</em></span></li>";
				}
				$("#kgglist").html(kgg_list);
				$("#gsbyqlist").html(gsbyq_list);
				$("#byqrzlist").html(byqrz_list);
				$("#hwglist").html(hwg_list);
			}
		});
	}
}
//监听活动设备
function changeCheckEqui(){
	$(".equipResultList>li").click(function(){
		if(!$(this).hasClass("disabled")){
			$(this).parent().parent().parent().find("li").removeClass("checked");
			$(this).addClass("checked");
		}
	});
}
//切换设备分组菜单
$(function(){
	$("#equipResultList li>p").click(function(){
		$(this).next().toggleClass("hide");
	});
});