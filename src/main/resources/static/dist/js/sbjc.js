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
				var inner=(result.length>1)?"<option value='0' selected='selected'>选择用户</option>":"";
				if(result.length>0){
					for(var i=0; i<result.length; i++){
						inner+="<option value='"+result[i].userName+"'>"+result[i].userName+"</option>";
					}
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
		console.log(formdata);
		$.ajax({
			url: "monitors/equipDataInfos",
//			url: "serialPort/equipmentInfos",
			type: "post",
			data: formdata,
			dataType: "json",
			success: function(result){
				console.log(result);
				var len=result.length;		//设备数量
				var warningMax;				//设备最高警示温度
				var warningMin;				//设备最低警示温度
				if(len<=0) return;
				var monitorHtml="";
				for(var i=0; i<len; i++){
					var equipment=result[i].equipment;
					var dataList=result[i].dateList;
					var channlNum=dataList.length;
					monitorHtml+="<div class='moni-block' id='moni-for'"+equipment.equipmentId+"'>";
					monitorHtml+="<label>"+equipment.equipmentName+"</label><p>";
					monitorHtml+="<div class='moni-block-title block-red'>高温报警<br/>"+simplifyData(dataList[0].maxTem)+"</div>";
					monitorHtml+="<div class='moni-block-title block-blue'>低温报警<br/>"+simplifyData(dataList[0].minTem)+"</div>";
					monitorHtml+="<div class='moni-block-title block-green'>设备预警<br/>正常</div>";
					monitorHtml+="<div class='moni-block-title block-yellow'>环境温度<br/>N/A</div>";
					monitorHtml+="</p><table><tr><td>位置</td><td class='edit-text'>A相</td><td class='edit-text'>B相</td><td class='edit-text'>C相</td></tr>";
					var rownum = (channlNum%3===0)?(channlNum/3):(channlNum/3+1);
					for(var m=0; m<rownum; m++){
						var si=m*3,ei=(m+1)*3;
						monitorHtml+="<tr><td class='edit-text'>触头位置</td>";
						for(var n=si; n<ei; n++){
							monitorHtml+="<td>"+simplifyData(dataList[n].temperature)+"</td>";
						}
						monitorHtml+="</tr>";
					}
					monitorHtml+="</table></div>";
				}
				$("#monitors").html(monitorHtml);
				function simplifyData(data){
			    	if(data){return data;}
			    	else{return "(无)";}
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

document.addEventListener("click", function(event){
	if(!event.target.classList.contains("edit-text")) return;
	var origintext = event.target.innerHTML;
	if(confirm("修改文本？")){
		var pro=prompt("请输入新内容：");
		if(pro.trim() !== "" && pro!==null){
			event.target.innerHTML=pro;
		}
	}
});