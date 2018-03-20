var innerHeight = window.innerHeight;
var chatstimer = null;
var ajax_getting = false;
var adminpagenum=1;
var cuspagenum=1;
$(function(){
	//判断是否登录成功
	if(level==0){
		alert("未登录");
		socket.disconnect();
	}
	//判断用户状态
	if(status==0){
		toout();
	}
	//初始化信息
	//分析师观点
	getChats(0,1);
	//讨论区
	getChats(0,2);
	
	var dds=getCookie("_default_show");
	if(dds==2||dds=="2"){
		$(".mui-control-item").toggleClass("mui-active");
		$(".mui-control-content").toggleClass("mui-active");
	}
	//发送信息
	$("#subbtn").click(function(){
		var tts=1;
		if(status==2){
			alert("被禁言");
			return;
		}
		var msg=$("#sendMsgInput").val();
		if($.trim(msg)==""){
			return;
		}
		if(0==msg.indexOf("@")){
			var ss=msg.split("@");
			msg="";
			for(var i=2;i<ss.length;i++){
				msg+=ss[i];
			}
			tts=2;
		}
		$("#sendMsgInput").val("");
		sendMessage(msg,clientid,targetClientId,tts);
		
		addCurrentInfo(headImgUrl,clientid,targetClientId,tts,msg)
		targetClientId="";
	});
	
	//上传图片
	$("#upload_pic").click(function(){
		$("#file").click();
	});
	$("#file").change(function(){
		$("#uploadBtn").click();
	});
	$("#uploadBtn").click(function(){
		var formData = new FormData();
		formData.append('file', $('#file')[0].files[0]);
		$.ajax({
		    url: '/file/uploadPic',
		    type: 'POST',
		    cache: false,
		    data: formData,
		    processData: false,
		    contentType: false
		}).done(function(res) {
			sendMessage("<img src='http://cdn.wap.qizhihuitrade.cn/uploads/"+res+"' />",clientid,"",1);
			addCurrentInfo(headImgUrl,clientid,"",1,"<img src='http://cdn.wap.qizhihuitrade.cn/uploads/"+res+"' />");
		}).fail(function(res) {});
	});
	
	//私聊
	$(".person_name").off().on().click(function(){
		var target_name=$(this).text();
		$("#sendMsgInput").html("@"+target_name+"@");
		targetClientId=target_name;
	});
	/**$("#sendMsgInput").focusout(function() {
		$("#subbtn").click();
	});*/
	$(document).on("click",".speak img", function(event) {
		$(".bigpic").show();
		$("#imgView").attr("src",$(this).attr("src"));
	});
	$(".bigpic").click(function(){
		$(this).hide();
	});
	$(".iocn").click(function(){
		var defaultshow=$(".mui-active").attr("data-type");
		setCookie("_default_show",defaultshow,10);
		//return false;
	});
	$(document).keydown(function(event){
		if(event.keyCode==13){
			$("#subbtn").click();
		}
	});
    $(window).scroll(function() {
       clearTimeout(chatstimer);
       chatstimer = setTimeout(function() {
         var scrollTop = $(document.body).scrollTop();　　
         var scrollHeight = $('body').height();　　
         var windowHeight = innerHeight;
         var scrollWhole = Math.max(scrollHeight - scrollTop - windowHeight);
         //$(".watch").html("scrollHeight:"+scrollHeight+" ,scrollTop:"+scrollTop+" ,windowHeight:"+windowHeight+" ,scrollWhole:"+scrollWhole);
         if (scrollTop < -100) {
           if (ajax_getting) {
             return false;
           } else {
             ajax_getting = true;
             getHistory();
           }
         }else if(scrollTop==0){
        	 ajax_getting=false;
         }
       });
    });
});


var uptime=setInterval(updateTime, 1000);
function updateTime(){
	var timehtml=totalclints+"人次<span>·</span>剩余时长<span>99:99:99</span>";
	$(".watch").html(timehtml);
}

//管理员聊天信息
function createAdminInfo(headUrl,sourceName,targetName,type,content){
	var chat="<ul><li><div class=\"l_answer\"><div class=\"tx\" onclick=\"talkTo('"+sourceName+"')\">"+
    "<img src=\""+headUrl+"\"></img>"+
    "<img class=\"guan\" src=\"./images/guan.png\"></img>"+
    "<img class=\"tx-img\" src=\"./images/tx-img.png\" alt=\"\"></img></div>"+
    "<div class=\"content\"><div class=\"l_name\">"+
    "<a href=\"javascript:;\">"+sourceName+"</a>"+
    "<font>讲师</font>";
    if(type==2){
    	chat+="<span>回答</span><i>"+targetName+"</i>";
    }
    chat+="</div><div class=\"speak\"><p>"+content+"</p>"+
    "</div></div></div><div class=\"clear\"></div>" +
    "</li></ul>";
    return chat;
}
function createCustomerlInfo(headUrl,sourceName,targetName,type,content,level,cid){
    var chat="<ul class=\""+cid+"\"><li><div class=\"h_answer\">" +
    	"<div class=\"tx\" ontouchstart=\"gtouchstart('"+sourceName+"')\" ontouchend=\"gtouchend()\" onclick=\"talkTo('"+sourceName+"')\">"+
	    "<img src=\""+headUrl+"\"></div>"+
	    "<div class=\"content\"><div class=\"l_name\"><a href=\"\">"+sourceName+"</a>";
    if(type==2){
    	chat+="<i>对<a class=\"ls\">"+targetName+"</a>讲</i>";
    }
    chat+="</div>"+
	    "<div class=\"speak\" ><p>"+content+"</p></div>"+
	    "<div class=\"clear\"></div>"+
	    "</div></div><div class=\"clear\"></div>" +
	    "</li></ul>";
    return chat;
}

function createCurrentInfo(headUrl,sourceName,targetName,type,content){
	var chat="<ul><li><div class=\"m_answer\">"+
		"<div class=\"tx\"><img src=\""+headUrl+"\">"+
		"</div><div class=\"content\"><div class=\"l_name\">";
	if(type==2){
		chat+="<i>对<a class=\"ls\">"+targetName+"</a>讲</i>";
	}
	chat+="<a href=\"\">"+sourceName+"</a></div>"+
		"<div class=\"speak\"><p>"+content+"</p></div>"+
		"</div><div class=\"clear\"></div></div>"+
		"<div class=\"clear\"></div></li></ul>";
    return chat;
}

function createValidlInfo(headUrl,sourceName,targetName,type,content,level,cid,sourceClientOpenId){
    var chat="<ul class=\""+cid+"\"><li><div class=\"h_answer\"><div class=\"tx\">"+
	    "<img src=\""+headUrl+"\"></div>"+
	    "<div class=\"content\"><div class=\"l_name\">";
    if(type==2){
    	chat+="<i>对<a class=\"ls\">"+targetName+"</a>讲</i>";
    }
    chat+="<a href=\"\">"+sourceName+"</a></div>"+
	    "<div class=\"speak\" data-level=\""+level+"\" data-type=\""+type+"\" data-headUrl=\""+headUrl+"\" data-sourceName=\""+sourceName+"\"" +
	    		" data-targetName=\""+targetName+"\" data-cid=\""+cid+"\" data-scoid=\""+sourceClientOpenId+"\"><p>"+content+"</p>" +
	    				"<i class=\"iconfont icon-selected\" onclick=\"admincheckinfo(this,'pass')\"></i><i class=\"iconfont icon-close\" onclick=\"admincheckinfo(this,'unpass')\"></i></div>"+
	    "<div class=\"clear\"></div>"+
	    "</div></div><div class=\"clear\"></div>" +
	    "</li></ul>";
    return chat;
}

//时间信息
function createTimeDiv(times){
	var date = new Date();  
    date.setTime(times);  
	var timeDiv="<div class=\"time\">"+
    "<span>"+getMonth(date)+"-"+getDay(date)+"</span>"+
    "<span>"+getHours(date)+":"+getMinutes(date)+"</span></div>";
    return timeDiv;
}

//添加聊天信息
function createMsg(res){
	var chatshtml="";
	var date1=res.times;
	if(date1-date>5*60*1000){
		chatshtml+=createTimeDiv(date1);
	}
	date=date1;
	if(res.level==1||res.level==10){
		chatshtml+=createCustomerlInfo(res.headUrl,res.sourceClientId,res.targetClientId,res.msgType,res.msgContent);
		$("#customerButtomMsg").before(chatshtml);
	}else if(res.level==11){
		chatshtml+=createAdminInfo(res.headUrl,res.sourceClientId,res.targetClientId,res.msgType,res.msgContent);
		$("#adminButtomMsg").before(chatshtml);
		$("#customerButtomMsg").before(chatshtml);
	}
	bodytoend();
}

//添加审核信息
function createcheckInfo(res){
	var chatshtml="";
	var date1=res.times;
	if(date1-date>5*60*1000){
		chatshtml+=createTimeDiv(date1);
	}
	date=date1;
	chatshtml+=createValidlInfo(res.headUrl,res.sourceClientId,res.targetClientId,res.msgType,res.msgContent,res.level,res.id,res.sourceClientOpenId);
	$("#customerButtomMsg").before(chatshtml);
	bodytoend();
}
function addCurrentInfo(headImgUrl,clientid,targetClientId,tts,msg){
	var chatshtml="";
	var date1=new Date().getTime();
	if(date1-date>5*60*1000){
		chatshtml+=createTimeDiv(date1);
	}
	date=date1;
	chatshtml+= createCurrentInfo(headImgUrl,clientid,targetClientId,tts,msg);
	$("#customerButtomMsg").before(chatshtml);
	if(level==11){
		$("#adminButtomMsg").before(chatshtml);
	}
	bodytoend();
}
function checkMsg(btn){
	if($(btn).val()=="通过"){
		var cid=$(btn).attr("data-name");
		var msg=$(btn).attr("data-content");
		sendMessage(msg,cid,targetClientId,"");
	}else if($(btn).val()=="不通过"){
		
	}
	sendcc("vc",$(btn).parent().parent().attr("data-uid"));
	$("#"+$(btn).attr("data-rd")).remove();
}
function stop(btn){
	if(confirm("是否禁言用户："+$(btn).attr("data-name"))){
		sendcc("stop",$(btn).attr("data-name"));
	}
}
function kick(btn){
	if(confirm("是否踢出用户："+$(btn).attr("data-name"))){
		sendcc("kick",$(btn).attr("data-name"));
	}
}

function toout(){
	socket.disconnect();
	setCookie("im_clientID","");
	alert("被禁止登录");
	location.href="/";
}

function ccinfo(type, datav){
	if(type=="pass"||type=="unpass"){
		$("."+datav).remove();
	}else if(type=="kick"){
		toout();
	}else if(type=="stop"){
		status=2;
	}
}

function admincheckinfo(btn,ctype){
	var cid=$(btn).parent().attr("data-cid");
	var level=$(btn).parent().attr("data-level");
	var headUrl=$(btn).parent().attr("data-headUrl");
	var sourceName=$(btn).parent().attr("data-sourceName");
	var targetName=$(btn).parent().attr("data-targetName");
	if(targetName==null||targetName==""){
		targetName="meiyoumingzide2b";
	}
	var scoid=$(btn).parent().attr("data-scoid");
	var content=$(btn).parent().find("p").html();
	//alert("cid:"+cid+",level:"+level+",headUrl:"+headUrl+",sourceName:"+sourceName+",targetName:"
	//		+targetName+",scoid:"+scoid+",content:"+content);
	var jsonObject ={
		id:cid,
		level:level,
		msgType:ctype,
		headUrl:headUrl,
		sourceClientId:sourceName,
		targetClientId:targetName,
		msgContent:content,
		sourceClientOpenId:scoid
	};
	if(ctype=="pass"){
		$("."+cid).find(".icon-selected").remove();
		$("."+cid).find(".icon-close").remove();
	}else{
		$("."+cid).remove();
	}
	shenhe(jsonObject);
}

function talkTo(name){
	$("#sendMsgInput").val("@"+name+"@");
	targetClientId=name;
}

function getHistory(){
	var maid=$(".mui-active").attr("data-type");
	if(maid=="1"){
		getChats(adminpagenum++,1);
	}else if(maid=="2"){
		getChats(cuspagenum++,2);
	}
}

var timeOutEvent=0;//定时器  
function gtouchstart(name){  
    timeOutEvent = setTimeout("longPress('"+name+"')",500);//这里设置定时器，定义长按500毫秒触发长按事件，时间可以自己改，个人感觉500毫秒非常合适  
    return false;  
};  
//手释放，如果在500毫秒内就释放，则取消长按事件，此时可以执行onclick应该执行的事件  
function gtouchend(){  
    clearTimeout(timeOutEvent);//清除定时器  
    if(timeOutEvent!=0){  
        //这里写要执行的内容（尤如onclick事件）  
        //alert("你这是点击，不是长按");  
    }  
    return false;  
};  
//如果手指有移动，则取消所有事件，此时说明用户只是要移动而不是长按  
function gtouchmove(){  
    clearTimeout(timeOutEvent);//清除定时器  
    timeOutEvent = 0;  
};  
  
//真正长按后应该执行的内容  
function longPress(name){  
    timeOutEvent = 0;
    if(confirm("确认将会员："+name+"踢出直播间？")){
    	sendcc("kick",name);
    }
} 

//type=1 获取管理员信息，type=2 获取普通信息，pageNum默认0
function getChats(pageNum,type){
	$.ajax({
		url:"service/getChats?type="+type+"&page="+pageNum,
		type:"get",
		dataType:"json",
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",   //解决传中文乱码
		success:function(res){
			var chatshtml="";
			for(var i=0;i<res.length;i++){
				var date1=res[i].times;
				if(date1-date>5*60*1000){
					chatshtml+=createTimeDiv(date1);
				}
				date=date1;
				if(ssid==res[i].sourceClientOpenId){
					chatshtml+=createCurrentInfo(res[i].headUrl,res[i].sourceClientId,res[i].targetClientId,res[i].msgType,res[i].msgContent);
				}else{
					if(res[i].level==1||res[i].level==10){
						chatshtml+=createCustomerlInfo(res[i].headUrl,res[i].sourceClientId,res[i].targetClientId,res[i].msgType,res[i].msgContent);
					}else if(res[i].level==11){
						chatshtml+=createAdminInfo(res[i].headUrl,res[i].sourceClientId,res[i].targetClientId,res[i].msgType,res[i].msgContent);
					}
				}
			}
			if(type==1){
				if(pageNum>0){
					$("#adminhistory").after(chatshtml);
				}else{
					$("#adminTopMsg").after(chatshtml);
					bodytoend();
				}
			}else if(type==2){
				if(pageNum>0){
					$("#customerhistory").after(chatshtml);
				}else{
					$("#customerTopMsg").after(chatshtml);
					bodytoend();
				}
			}
			
		},
		error:function(){
 
		},
		complete : function(XMLHttpRequest,status){ //请求完成后最终执行参数
			
		}
	});
}

function setCookie(c_name,value,expireMinutes){
	var exdate=new Date();
	exdate.setMinutes(exdate.getMinutes()+expireMinutes);
	document.cookie=c_name+ "=" +escape(value)+	((expireMinutes==null) ? "" : ";expires="+exdate.toGMTString());
}
function getCookie(c_name){
	if (document.cookie.length>0){
	  c_start=document.cookie.indexOf(c_name + "=");
	  if (c_start!=-1){ 
		c_start=c_start + c_name.length+1;
		c_end=document.cookie.indexOf(";",c_start);
		if (c_end==-1){
			c_end=document.cookie.length;
		}
		return unescape(document.cookie.substring(c_start,c_end));
	  }
	}
	return "";
}

function datetimeFormat(longTypeDate){  
    var datetimeType = "";  
    var date = new Date();  
    date.setTime(longTypeDate);  
    datetimeType+= date.getFullYear();   //年  
    datetimeType+= "-" + getMonth(date); //月   
    datetimeType += "-" + getDay(date);   //日  
    datetimeType+= "&nbsp;&nbsp;" + getHours(date);   //时  
    datetimeType+= ":" + getMinutes(date);      //分
    datetimeType+= ":" + getSeconds(date);      //分
    return datetimeType;
} 
//返回 01-12 的月份值   
function getMonth(date){  
    var month = "";  
    month = date.getMonth() + 1; //getMonth()得到的月份是0-11  
    if(month<10){  
        month = "0" + month;  
    }  
    return month;  
}  
//返回01-30的日期  
function getDay(date){  
    var day = "";  
    day = date.getDate();  
    if(day<10){  
        day = "0" + day;  
    }  
    return day;  
}
//返回小时
function getHours(date){
    var hours = "";
    hours = date.getHours();
    if(hours<10){  
        hours = "0" + hours;  
    }  
    return hours;  
}
//返回分
function getMinutes(date){
    var minute = "";
    minute = date.getMinutes();
    if(minute<10){  
        minute = "0" + minute;  
    }  
    return minute;  
}
//返回秒
function getSeconds(date){
    var second = "";
    second = date.getSeconds();
    if(second<10){  
        second = "0" + second;  
    }  
    return second;  
}
function bodytoend(){
	var hbody = document.getElementsByTagName("body")[0];
	hbody.scrollTop = hbody.scrollHeight;
}