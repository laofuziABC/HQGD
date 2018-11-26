/**
 * 分页插件
 * laofuzi 2018.11.20
 */
$(function(){
	currentPage = 1;
	$.fn.extend({
		setActivePage: function(){
			var CP = 1;				//点击的页面排序值ClickPage
			var AP = 1;				//当前活动的页面排序值ActivePage
			var PrevP = 0;		//“上一页”的排序值
			var NextP = 1;		//“下一页”的排序值
			//设定样式，默认第一页(currentPage=1)处于活动状态，随着页码值变化，改变活动样式
			/*$("#pager > a:eq("+currentPage+")").addClass("active");*/
			$("#pager > a").click(this, function(){
				CP = $(this).index();
				AP = $("#pager > .active").index();	
				NextP = $("#pager > a:contains('下一页')").index();
				if(CP == 0 && AP > (PrevP+1)){ currentPage--; }		//上一页
				else if(CP == NextP && AP < (NextP-1)){ currentPage++; }		//下一页
				else if((CP == 0 && AP == (PrevP+1))||(CP == NextP && AP == (NextP-1))){currentPage=currentPage;}
				else{ currentPage=CP;	}		//当前页
				/*if(CP == 0 && AP > (PrevP+1)){ currentPage=AP-1; }		//上一页
				else if(CP == NextP && AP < (NextP-1)){ currentPage=AP+1; }		//下一页
				else{ currentPage=CP;	}		//当前页
*/				
				//更改currentPage后，刷新表格
				alert(currentPage);
				searchResult(currentPage);
			});
		}
		
	})
	/*//设定样式，改变页码值
	$("#pager > a").click(this, function(){
		CP = $(this).index();
		AP = $("#pager > .active").index();	
		NextP = $("#pager > a:contains('下一页')").index();
		//去掉活动页的class='active'属性
		$("#pager > a").removeClass("active");
		if(CP == 0 && AP > (PrevP+1)){
			//为前一页添加class='active'属性
			$("#pager > a:eq("+(AP-1)+")").addClass("active");
			currentPage=AP-1;
		}else if(CP == NextP && AP < (NextP-1)){
			//为后一页添加class='active'属性
			$("#pager > a:eq("+(AP+1)+")").addClass("active");
			currentPage=AP+1;
		}else{
			//为点击页添加class='active'属性
			$("#pager > a:eq("+CP+")").addClass("active");
			currentPage=CP;
		}
		//更改currentPage后，刷新表格
		alert("当前页码是："+currentPage);
		searchResult(currentPage);
	});*/
	
});
	
