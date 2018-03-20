var index=2;
function setheight(fid){
	$("#iframepage"+fid).css("height",window.screen.availHeight*0.9 +"px");
	//$("#iframepage"+fid).hide();
}
function changeUrl(){
	$("#iframepage"+index).fadeIn(500);
	if(index==1){
		$("#cimg").attr("src","http://cdn.wap.qizhihuitrade.cn/images/tjy.gif");
		index=2;
	}else {
		$("#cimg").attr("src","http://cdn.wap.qizhihuitrade.cn/images/tzb.gif");
		index=1;
	}
	setCookie("_iframe_show",index,10);
	$("#iframepage"+index).fadeOut(500);
}

var mouseX;
var mouseY;
var ismove=false;
var timeOutEvent=0; 
function show(event) {
	if(ismove){
		mouseX=event.changedTouches[0].clientX;
		mouseY=event.changedTouches[0].clientY; 
		//$("#changeDiv").css({"left":mouseX+"px","top":mouseY+"px"});
		//$("#a").html(mouseX+" , "+mouseY);
		$("#changeDiv").finish();
		validXY();
		$("#changeDiv").animate({left: (mouseX-30)+"px",top: (mouseY-30)+"px"}, "fast");
	}
	return false;
}
function gtouchstart(){  
	timeOutEvent = setTimeout("longPress()",100);
	stop();
	return false;  
};  
function gtouchend(){  
	clearTimeout(timeOutEvent);
	ismove=false;
	move();
	return false;  
};   
function longPress(){  
	ismove=true;
} 

//实现滚动条无法滚动
var mo=function(e){e.preventDefault();};

/***禁止滑动***/
function stop(){
	document.body.style.overflow='hidden';       
	document.addEventListener("touchmove",mo,false);//禁止页面滑动
}

/***取消滑动限制***/
function move(){
	document.body.style.overflow='';//出现滚动条
	document.removeEventListener("touchmove",mo,false);       
} 

function validXY(){
	if(mouseX<30){
		mouseX=30;
	}
	if(mouseY<30){
		mouseY=30;
	}
}

$(function(){
	var iframeheight=window.screen.availHeight*0.9 +"px";
	$("body").append("<iframe src=\"http://m.qizhihuitrade.cn/c/?proxyid=33&title=金汇投#/login\" id=\"iframepage2\" name=\"iframepage2\" frameBorder=0 width=\"100%\"  scrolling=\"auto\" height=\""+iframeheight+"\" ></iframe>");
	var pl=location+"";
	var iframe_index=getCookie("_iframe_show");
	if(iframe_index==1){
		$("#cimg").attr("src","http://cdn.wap.qizhihuitrade.cn/images/tzb.gif");
		$("#iframepage1").hide();
		index=1;
	}else if(iframe_index==2){
		$("#cimg").attr("src","http://cdn.wap.qizhihuitrade.cn/images/tjy.gif");
		$("#iframepage2").hide();
		index=2;
	}else{
		if(pl.indexOf("stat=jy")!=-1){
			$("#iframepage1").hide();
			$("#cimg").attr("src","http://cdn.wap.qizhihuitrade.cn/images/tzb.gif");
			index=1;
		}else{
			$("#iframepage2").hide();
		}
	}
	
});