;
/**
*设备监测页面的功能JS
*此脚本运行主要用于“设备监测”模块各功能的实现
*laofuzi 2019.04.24
*/
$.fn.extend({
	//获取并回显当前用户名
	fetchUserInformations: function(){
		$.ajax({
			url: "user/session",
			type: "post",
			dataType: "json",
			success: function(result){
				if(!result.length>0) return;
				var inner=(result.length>1)?"<option value='' selected='selected'>选择用户</option>":"";
				for(var i=0; i<result.length; i++){
					inner+="<option value='"+result[i].userName+"'>"+result[i].userName+"</option>";
				}
				$("#user-select").html(inner);
				if(result.length==1){
					$("#user-select").val(result[0].userName);
				}
			}
		})
	},
	//检查串口通信是否连接
	fetchEquipmentsInfos: function(){},
	//提交用户表单，查询符合条件的设备
	getAllEquiDataInfo: function(){
		var formdata=$("#queryform-sbjc").serializeObject();
		$.ajax({
			url: "monitors/equipDataInfos",		//测试的url
//			url: "serialPort/equipmentInfos",	//串口通信的url
			type: "post",
			data: formdata,
			dataType: "json",
			success: function(result){
//				console.log(result);
				var len=result.length;		//设备数量
				var warningMax;				//设备最高警示温度
				var warningMin;				//设备最低警示温度
				if(!len>0){
					$("#monitors").html("<div class='moni-none'>无相关监测设备</div>");
					return;
				}
				var monitorHtml="";
				for(var i=0; i<len; i++){
					var equipment=result[i].equipment;
					var dataList=result[i].dateList;
					var channelNum=dataList.length;
					monitorHtml+="<div class='moni-block' id='moni-for-"+equipment.equipmentId+"'>";
					monitorHtml+="<label>"+equipment.equipmentName+"</label><p>";
					monitorHtml+="<div class='moni-block-title block-red'>高温报警<br/>"+simplifyData(equipment.maxTem)+"</div>";
					monitorHtml+="<div class='moni-block-title block-blue'>低温报警<br/>"+simplifyData(equipment.minTem)+"</div>";
					monitorHtml+="<div class='moni-block-title block-green'>设备预警<br/>"+checkEquipmentRunningState(dataList)+"</div>";
					monitorHtml+="<div class='moni-block-title block-yellow'>环境温度<br/>"+simplifyData(equipment.ambientTem)+"</div>";
					monitorHtml+="</p><table><tr><td>位置</td><td class='edit-text'>A相</td><td class='edit-text'>B相</td><td class='edit-text'>C相</td></tr>";
					var rownum = Math.ceil(channelNum/3);
					if(!rownum>0){
						monitorHtml+="<tr><td>/</td><td>(无数据)</td><td>(无数据)</td><td>(无数据)</td></tr>";
					}else{
						for(var m=0; m<rownum; m++){
							switch(m){
								case 0: monitorHtml+="<tr><td class='edit-text'>上触头</td>"; break;
								case 1: monitorHtml+="<tr><td class='edit-text'>下触头</td>"; break;
								case 2: monitorHtml+="<tr><td class='edit-text'>上触头</td>"; break;
								case 3: monitorHtml+="<tr><td class='edit-text'>下触头</td>"; break;
								default: monitorHtml+="<tr><td class='edit-text'></td>"; break;
							}
							var si=m*3,ei=(m+1)*3;
							for(var n=si; n<ei; n++){
								monitorHtml+="<td>"+checkFiberRunningState(dataList[n])+"</td>";
							}
							monitorHtml+="</tr>";
						}
					}
					monitorHtml+="</table></div>";
				}
				$("#monitors").html(monitorHtml);
				function simplifyData(data){
					if(!data) return "(无)";
					switch(data){
			    		case "2999": return "<span class='block-green'>系统调整中</span>"; break;
			    		case "3000": return "<span class='block-red'>光纤故障</span>"; break;
			    		case "-437": return "<span class='block-red'>测温仪故障</span>"; break;
			    		default: return data; break;
		    		}
			    };
				function checkEquipmentRunningState(arr){
					if(!arr.length>0) return "(无)";
					var codes=[];
					for(var i=0; i<arr.length; i++){
						codes.push(arr[i].state);
					}
					if($.inArray(1,codes)>-1){return "<small class='error'>系统调整中</small>"; }
					else if($.inArray(2,codes)>-1){return "<small class='error'>通信故障</small>"; }
					else if($.inArray(3,codes)>-1){return "<small class='error'>光纤故障</small>"; }
					else if($.inArray(4,codes)>-1){return "<small class='error'>测温仪故障</small>"; }
					else if($.inArray(9,codes)>-1){return "<small class='error'>通道高温</small>"; }
					else if($.inArray(10,codes)>-1){return "<small class='error'>通道低温</small>"; }
					else{return "正常"; }
				};
				function checkFiberRunningState(obj){
					var result;
					if(obj.temperature>obj.maxTem){ result="<b class='block-red'>"; }
					else if(obj.temperature<obj.minTem){ result="<b class='block-blue'>"; }
					else{ result="<b>"; }
					result+=simplifyData(obj.temperature)+"</b>";
					return result;
				}
				
				
			}
		})
	},
	serializeObject: function() {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function() {
            if (o[this.name]) {
                if (!o[this.name].push) {
                    o[this.name] = [ o[this.name] ];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    }
    
});

/*
 * 在页面上修改文本（主要内容是光纤位置）
 * 暂时可以实现在页面直接修改，但修改效果不持久
 * 后续计划与数据库交互，将修改前后的位置信息存储在数据库中
 * 文本存储到数据库中的具体业务流程待定			——2019.05.06
 */
document.addEventListener("click", function(event){
	if(!event.target.classList.contains("edit-text")) return;
	var equipmentId = getIdByNode(event.target);
	var origintext = event.target.innerHTML;
	if(confirm("修改文本？")){
		var pro=prompt("请输入新内容：",origintext);
		if(!pro) return;
		if(pro.trim() !== origintext){
			event.target.innerHTML=pro;
			if(confirm("保存新文本？")){
				updateLocation(equipmentId, origintext, pro);
			}
		}
	}
});
//获取DOM中，指定节点所属设备的设备主键
var equipmentId;
function getIdByNode(node){
	if(!node.classList.contains("moni-block")){
		if(!node.parentNode) return;
		getIdByNode(node.parentNode);
	}else{
		equipmentId=node.id.substring(9,node.id.length);
	}
	return equipmentId;
}
//保存修改后的文本到数据库中（暂且保留此功能，具体业务实现方法待后续根据数据库做决定）
function updateLocation(equipmentId, origintext, newText){
	console.log("equipmentId="+equipmentId+"；origintext="+origintext+"；newText="+newText);
	
}
