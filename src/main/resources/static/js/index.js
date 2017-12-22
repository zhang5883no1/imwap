$(function(){
	//判断是否登录成功
//	if(level==0){
//		$(".pop").show();
//		$(".login").show();
//		socket.disconnect();
//	}
//	//判断用户状态
//	if(status==0){
//		toout();
//	}
	//初始化信息
	//分析师观点
	getChats(0,1);
	//讨论区
	//getChats(0,2);
	
	//发送信息
	$(".sub_btn").click(function(){
		if(status==2){
			alert("被禁言");
			return;
		}
		var msg=$("#sendMsgInput").html();
		var tts="";
		if($("#fonts")){
			tts=$("#fonts").val();
		}
		if(0==msg.indexOf("@")){
			var ss=msg.split("@");
			msg="";
			for(var i=2;i<ss.length;i++){
				msg+=ss[i];
			}
		}
		$("#sendMsgInput").html("");
		sendMessage(msg,clientid,targetClientId,tts);
		if(tts!="top"&&tts!="scrol"){
			normalInfo(moment().format("HH:mm"),clientid,msg,level,tts,targetClientId);
		}
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
			$("#sendMsgInput").html("<img src='http://static.qizhihuitrade.cn/uploads/"+res+"' />");
		}).fail(function(res) {});
	});
	
	//私聊
	$(".person_name").off().on().click(function(){
		var target_name=$(this).text();
		$("#sendMsgInput").html("@"+target_name+"@");
		targetClientId=target_name;
	});
});
	
//管理员聊天信息
function createAdminInfo(headUrl,sourceName,targetName,type,content){
	var chat="<ul><li><div class=\"l_answer\"><div class=\"tx\">"+
    "<img src=\""+headUrl+"\"></img>"+
    "<img class=\"guan\" src=\"./images/guan.png\"></img>"+
    "<img class=\"tx-img\" src=\"./images/tx-img.png\" alt=\"\"></img></div>"+
    "<div class=\"content\"><div class=\"l_name\">"+
    "<a href=\"javascript:;\" th:text=\""+sourceName+"\"></a>"+
    "<font>讲师</font>";
    if(type==2){
    	chat+="<span>回答</span><i>"+targetName+"</i>";
    }
    chat+="</div><div class=\"speak\"><p>"+content+"</p>"+
    "</div></div></div><div class=\"clear\"></div></li></ul>";
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
	if(type=="vc"){
		$("."+datav).remove();
	}else if(type=="kick"){
		toout();
	}else if(type=="stop"){
		status=2;
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
			var date=0;
			for(var i=0;i<res.length;i++){
				var date1=res[i].times;
				if(date1-date>5*60*1000){
					chatshtml+=createTimeDiv(date1);
				}
				date=date1;
				chatshtml+=createAdminInfo(res[i].headUrl,res[i].sourceClientId,res[i].targetClientId,res[i].msgType,res[i].msgContent);
			}
			if(type==1){
				$("#adminTopMsg").after(chatshtml);
			}else if(type==2){
				$("#customerTopMsg").after(chatshtml);
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