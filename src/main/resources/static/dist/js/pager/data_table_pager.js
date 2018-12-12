/**
 * 整合分页插件
 * laofuzi 2018.12.09
 */
//设置公共常量【开始】
var initPageNum=1;
var initPageSize=10;
var pageNum=1;
var initTotalSize=100;
var totalNum;
//设置公共常量【结束】
//	载入页码区域
function loadPages(pageSize, totalSize, pageNum){
	pageSize=(pageSize==null)?initPageSize:pageSize;
	totalSize=(totalSize==null)?initTotalSize:totalSize;
	//缓存总数和总页数
	initPageSize=(pageSize==null)?initPageSize:pageSize;
	initTotalSize=(totalSize==null)?initTotalSize:totalSize;
	//计算总页码
	totalNum=Math.ceil(totalSize/pageSize);
	var pageInfo="<a class='pre'>上一页</a>";
	pageInfo+="<a class='pre'>首页</a>";
	if(pageNum>2){ pageInfo+="<a>"+(pageNum-2)+"</a>"; }
	if(pageNum>1){ pageInfo+="<a>"+(pageNum-1)+"</a>"; }
	pageInfo+="<a class=\"active\">"+pageNum+"</a>";
	if(pageNum<totalNum){ pageInfo+="<a>"+(pageNum+1)+"</a>"; }
	if(pageNum<totalNum-1){ pageInfo+="<a>"+(pageNum+2)+"</a>"; }
	pageInfo+="<a class='next'>尾页</a>";
	pageInfo+="<a class='next'>下一页</a>";
	pageInfo+="<span class='goto-text'>共"+totalNum+"页，到</span>";
	pageInfo+="<span class='goto-input'><input type='text' id='enterNum' /></span>";
	pageInfo+="<span class='goto-text'>页</span>";
	pageInfo+="<a class='goto-btn'>GO</a>";
	return pageInfo;
}
//变更当前活动页
$.fn.extend({
	setActivePage: function(fParam){
		var CP = 1;		//点击的页面排序值ClickPage
		var AP = $(".list-page-tail > .active").index();								//当前活动的页面排序值ActivePage
		var FP = $(".list-page-tail > a:contains('首页')").index();				//“首页”的排序值FirstPage
		var LP = $(".list-page-tail > a:contains('尾页')").index();				//“尾页”的排序值LastPage
		var PrevP = $(".list-page-tail > a:contains('上一页')").index();		//“上一页”的排序值
		var NextP = $(".list-page-tail > a:contains('下一页')").index();		//“下一页”的排序值
		var Useless = $(".list-page-tail > a:contains('GO')").index();		//“GO”的排序值
		
		$(".list-page-tail > a").click(this, function(){
			CP = $(this).index();
			if(CP!=Useless){
				if(CP==FP){pageNum=1}
				else if(CP==LP){pageNum=totalNum}
				else if(CP == PrevP && AP > (PrevP+2)){ pageNum--; }		//上一页
				else if(CP == NextP && AP < (NextP-2)){ pageNum++; }		//下一页
				else if((CP == PrevP && AP == (PrevP+2))||(CP == NextP && AP == (NextP-2))){pageNum=pageNum;}			//排除第一页和最后一页的上一页和下一页操作
				else{		//当前页
					if(pageNum>=3){ pageNum=CP+pageNum-4;	 }
					else{ pageNum=CP-1; }
				}
			}else{
				var enterNum = parseInt($("#enterNum").val());
				if(enterNum>0 && enterNum<=totalNum){ pageNum=enterNum;}
				else{alert("请输入页码。"); }
			}
			//更改pageNum后，局部刷新结果和分页插件
			if(fParam==1){searchResult(pageNum);}
			else if(fParam==2){getUserList();}
			else if(fParam==3){getEquipmentList();}
			else if(fParam==4){getEquiResultList();}
			loadPages(null, null, pageNum);
		});
	}
})