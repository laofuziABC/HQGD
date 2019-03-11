$(document).ready(function(){




// 页面table选中状态的控制

$(".checkable tr").click(function(){
if(!$(this).find('td').eq(0).find("span").hasClass('trchecktrue')){
$(this).find('td').eq(0).find("span").addClass('trchecktrue');
$(this).addClass("checktr")}
else if($(this).find('td').eq(0).find("span").hasClass('trchecktrue')){
$(this).removeClass("checktr")
$(this).find('td').eq(0).find("span").removeClass('trchecktrue');}})







$(".tj_table thead tr td .checkspan").click(function(){
if($(this).hasClass("trchecktrue")){
$(this).removeClass("trchecktrue");
$(".tj_table tbody").find("tr").removeClass("checktr");
$(".tj_table tbody").find(".checkspan").removeClass("trchecktrue");
} else{
$(this).addClass("trchecktrue");
$(".tj_table tbody").find("tr").addClass("checktr");
$(".tj_table tbody").find(".checkspan").addClass("trchecktrue");}})



$(".checkable tr").find(".budian").find("span").bind("click",function(event){
$(this).html('ss');
event.stopPropagation();  
return false;});


// 页面table选中状态的控制









/*// 首页登录验证账号密码不能为空
$(".login_botton input").click(function(event){
if(!$(".login_user").find("input").val() ==''){
if(!$(".login_pass").find("input").val() ==''){
event.preventDefault(); 
top.location.href="index.html";
}else{alert("密码不能为空")}
}else{alert("用户名不能为空")}})
// 首页登录验证账号密码不能为空
*/

/*// 设置侧导航栏的侧导航样式兼容
$(".sidebar-toggle").click(function(){
if($("body").hasClass("sidebar-collapse")){
$(".sidebar-menu").css({"overflow":'hidden',"width":230})
$(".sidebar-menu").find(".active").removeClass("nei_active");
adjustjia()
}else{
$(".sidebar-menu").css({"overflow":'visible'})
$(".sidebar-menu").find(".active").addClass("nei_active")
adjustjian()}})
// 设置侧导航栏的侧导航样式兼容
*/

// 浮层开关
$(".lindianjia").click(function(){
$(".fuceng").css({"display":"block"})
})
$(".fuclose").click(function(){
$(".fuceng").css({"display":"none"})})
// 浮层开关


// 响应方案管理浮层管理控制
function furesize(){
pingHeight=document.documentElement.clientHeight;
 $(".fuceng").css({"height":pingHeight})
fucenggao = (pingHeight-$(".fuqu").height())/2;
$(".fuqu").css({"top":fucenggao,"left":(pingWidth-640)/2}) }
// 响应方案管理浮层管理控制

//更改说明：暂时隐藏fuquwei相关变量
// 浮层在窗口变化时候的样式变化
//function sy_fucengresize(fufu){
//fuquwei = fufu[0];
//if (fuquwei.offsetLeft > getInner().width - fuquwei.offsetWidth) {
//fuquwei.style.left = getInner().width - fuquwei.offsetWidth + 'px';}
//if (fuquwei.offsetTop > getInner().height - fuquwei.offsetHeight) {
//fuquwei.style.top = getInner().height - fuquwei.offsetHeight + 'px';}
//}
// 浮层在窗口变化时候的样式变化







//resizefun();
//sy_tabel();
//window.addEventListener('resize',function(){
//resizefun();
//sy_tabel();
////sy_fucengresize($(".fuqu"))
//})


//// 首页表格自适应
//function sy_tabel(){
//var sytgao = 120;
//if($(".sy_info_four p").height()>sytgao){
//sytgao = $(".sy_info_four p").height()+10;}
//if($(".sy_info_left").height()>sytgao){
//sytgao = $(".sy_info_left").height();}
//$(".sy_info_one").css({"height":sytgao})
//$(".sy_info_two").css({"height":sytgao})
//$(".sy_info_three").css({"height":sytgao})
//$(".sy_info_four").css({"height":sytgao})}
//// 首页表格自适应


function resizefun(){
pingHeight=document.documentElement.clientHeight;
pingWidth=document.documentElement.clientWidth;
add_lin();
//更改说明：更改 $(".qqq").css({"height":pingHeight-166}) 为 $(".qqq").css({"height":pingHeight-130})			laofuzi 2018.12.21
//$(".qqq").css({"height":pingHeight-166})
$(".qqq").css({"height":pingHeight-130})
$(".requtu").css({"maxHeight":pingHeight-298})
//更改说明：更改 $(".content-wrapper").css({"height":pingHeight-90}) 为 $(".content-wrapper").css({"height":pingHeight-120})			laofuzi 2018.12.20
//$(".content-wrapper").css({"height":pingHeight-90})
$(".content-wrapper").css({"height":pingHeight-120})
$(".sidebar-menu").css({"height":pingHeight-106})
$(".login").css({"left":(pingWidth-537)/2})
$(".login").css({"top":(pingHeight-458)/2})

var linyi = $(".xg_right").height();
var liner = $(".ssjc_right").height();
if (linyi<pingHeight-166){linyi = pingHeight-166}
//更改说明：更改 liner = pingHeight-166 为 liner = pingHeight-130			laofuzi 2018.12.20
//if (liner<pingHeight-166){liner = pingHeight-166}
if (liner<pingHeight-130){liner = pingHeight-130}
if(pingWidth>990){
$(".ssjc_left").css({"minHeight":liner})
}else{$(".ssjc_left").css({"minHeight":'auto'})}
if(pingWidth>1200){
$(".xj_left").css({"height":linyi})
}else{$(".xj_left").css({"height":'auto'})}
$(".sidebar-menu").css({"overflow":'hidden'})
$(".yxjc_info_right").css({"width":($(".yxjc").width()-145)})
}






// 为适应屏幕高度过高加入的兼容代码
//add_lin();
//function add_lin(){
//if(pingHeight<750){ 
//$(".gao").addClass("xian")
//$(".lin_kongxian").css({"display":"none"})
//$(".xtjc .row").css({"paddingTop":0})
//$(".yxjc img").css({"paddingTop":0})}
//if(pingHeight>750){ 
//$(".gao").removeClass("xian")
//$(".lin_kongxian").css({"display":"block"})
//$(".xtjc .row").css({"paddingTop":"7%"})
//$(".yxjc img").css({"paddingTop":"7%"})}}
// 为适应屏幕高度过高加入的兼容代码


// 表格样式控制
function table_change(){
var biaospansize = $(".table_head").find("span").length;
var biaospanwidth =($(".table_head").width()-biaospansize+1)/biaospansize;
var headliheight=30;
for(n=0;n<biaospansize;n++){
if($(".table_head").find("span").eq(n).find("i").height()>headliheight){
headliheight =$(".table_head").find("span").eq(n).find("i").height();
for(m=0;m<biaospansize;m++){
$(".table_head").find("span").eq(m).css({"height":headliheight})}}}
if(headliheight<40){
for(m=0;m<biaospansize;m++){
$(".table_head").find("span").eq(m).css({"height":headliheight})}}
$(".table_head").find("span").css({"width":biaospanwidth})
$(".table_head").find("span").eq(biaospansize-1).css({"borderRight":"none"})
$(".table_body").find("span").css({"width":biaospanwidth})
var biaolisize = $(".table_body").find("li").length;
for(i=0;i<biaolisize;i++){
$(".table_body").find("li").eq(i).find("span").eq(biaospansize-1).css({"borderRight":"none"});
var everyheight = 30;
for(j=0;j<$(".table_body").find("li").eq(i).find("span").length;j++){
if(everyheight < $(".table_body").find("li").eq(i).find("span").eq(j).find('i').height()){
everyheight = $(".table_body").find("li").eq(i).find("span").eq(j).find('i').height();
for(t=0;t<$(".table_body").find("li").eq(i).find("span").length;t++){
$(".table_body").find("li").eq(i).find("span").eq(t).css({"height":everyheight})}}}
if(everyheight < 40){
for(k=0;k<$(".table_body").find("li").eq(i).find("span").length;k++){
$(".table_body").find("li").eq(i).find("span").eq(k).css({"height":everyheight}) }}}}
// 表格样式控制

// 拖拽功能Start
tuozhuai($(".fuqutitle"),$(".fuqu"));
tuozhuai($(".fuqulogo"),$(".fuqu"));


function tuozhuai(dianzhuai , kuangti){
	if(dianzhuai[0]){
dianzhuai[0].onmousedown = function (e) {
preDef(e);
var e = getEvent(e);
var _this = kuangti[0];
var diffX = e.clientX - _this.offsetLeft;
var diffY = e.clientY - _this.offsetTop;
if (typeof _this.setCapture != 'undefined') {
_this.setCapture();} 
document.onmousemove = function (e) {
var e = getEvent(e);
var left = e.clientX - diffX;
var top = e.clientY - diffY;
if (left < 0) {
left = 0;
} else if (left > getInner().width - _this.offsetWidth) {
left = getInner().width - _this.offsetWidth;}
if (top < 0) {
top = 0;
} else if (top > getInner().height - _this.offsetHeight) {
top = getInner().height - _this.offsetHeight;}
_this.style.left = left + 'px';
_this.style.top = top + 'px';} 
document.onmouseup = function () {
this.onmousemove = null;
this.onmouseup = null;
if (typeof _this.releaseCapture != 'undefined') {
_this.releaseCapture();}}}}}
//阻止默认行为
function preDef(event) {
var e = getEvent(event);
if (typeof e.preventDefault != 'undefined') {//W3C
e.preventDefault();
} else {//IE
e.returnValue = false;}}
//获取Event对象
function getEvent(event) {
return event || window.event;}
//跨浏览器获取视口大小
function getInner() {
if (typeof window.innerWidth != 'undefined') {
return {
width : window.innerWidth,
height : window.innerHeight}
} else {
return {
width : document.documentElement.clientWidth,
height : document.documentElement.clientHeight}}}
// 拖拽功能End
})



//获取选中的checkbox的value值
function quvalue(){
var treeinfo = [];
  for(i=0;i<$(".tree_menu input:checkbox:checked").size();i++){
      var jige = $(".tree_menu input:checkbox:checked").eq(i).val();
       treeinfo.push(jige);}
  alert(treeinfo);
return treeinfo;

}
//获取选中的checkbox的value值



// 返回选中行
function chauxanji(){
var jige = $(".checkable").find("tr").size();
var fanhuixuanzhong = [];
for(var i=0;i<jige;i++){
if($(".checkable").find("tr").eq(i).find("span").hasClass('trchecktrue')){
fanhuixuanzhong.push($(".checkable").find("tr").eq(i).find("span").html())}}
alert(fanhuixuanzhong)
return fanhuixuanzhong;
}// 返回选中行

var passform=document.querySelector("#passform");passform.setAttribute("novalidate",true);passform.addEventListener("blur",function(event){var error=haveError(event.target);if(error){showError(event.target,error);return}removeError(event.target)},true);var haveError=function(field){if(field.type=="button")return;if(field.id==="confirm"){var pass1=document.querySelector("#newPassword");var pass2=document.querySelector("#confirm");if(pass1.value!=pass2.value){if(pass1.value==""||pass1.value==null)return"请先输入新密码！";if(pass2.value==""||pass2.value==null)return"请确认新密码！";return"两次输入不一致！"}};var result=field.validity;if(result.valid)return;if(result.valueMissing&&field.id=="oldpassword")return"请输入原密码！";if(field.id=="newPassword"){if(result.valueMissing)return"请输入新密码！";if(result.tooShort)return"密码最小长度为"+field.getAttribute("minLength")+"，当前输入长度为"+field.value.length+"!"}return"请仔细检查！"};var showError=function(field,error){field.classList.add("error");var mark=field.id||field.name;if(!mark)return;var message=document.querySelector("#error-for-"+mark);field.setAttribute("aria-describe-by","error-for-"+mark);message.innerHTML=error;message.style.visibility="visible"};var removeError=function(field){field.classList.remove("error");field.removeAttribute("aria-describe-by");var mark=field.id||field.name;if(!mark)return;var message=document.querySelector("#error-for-"+mark);message.innerHTML="";message.style.visibility="hidden"};
