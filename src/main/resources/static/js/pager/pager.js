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
				else if((CP == 0 && AP == (PrevP+1))||(CP == NextP && AP == (NextP-1))){currentPage=currentPage;}			//排除第一页和最后一页的上一页和下一页操作
				else{		//当前页
					if(currentPage>=3){
						currentPage=CP+currentPage-3;	
					}else{
						currentPage=CP
					}
				}
				//更改currentPage后，刷新表格
				alert(currentPage);
				searchResult(currentPage);
			});
		}
		
	})
	
	
	
	
	
});
	
