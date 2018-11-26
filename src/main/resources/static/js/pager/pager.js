/**
 * 分页插件
 * laofuzi 2018.11.20
 */
var currentPage = 1;
var CP = 1;				//点击的页面排序值ClickPage
var AP = 1;				//当前活动的页面排序值ActivePage
var PrevP = 0;		//“上一页”的排序值
var NextP = 1;		//“下一页”的排序值
/*$(".list-page-tail > a").click(this, function(){*/
$("#pager > a").click(this, function(){
	alert("123");
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
	searchResult();
});
	
