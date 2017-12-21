$(function(){
	$("#sendMsgInput").html(default_info);
	if(level==0){
		$(".pop").show();
		$(".login").show();
		socket.disconnect();
	}else{
		$(".exit").show();
		$(".headLog").hide();
		$(".time2").html("当前登录："+clientid+"&nbsp;&nbsp;");
	}
	if(status==0){
		toout();
	}
	
	$("#upload_pic").click(function(){
		$("#file").click();
	});
	$("#file").change(function(){
		$("#uploadBtn").click();
	});
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
	$(".loginbtn").click(function(){
		var login_userName=$("#login_userName").val();
		var login_pwd=$("#login_pwd").val();
		$.ajax({
			url:"login?name="+encodeURI(login_userName)+"&pwd="+login_pwd,
			type:"get",
			dataType:"json",
			timeout : 1500,
			contentType:"application/x-www-form-urlencoded;charset=UTF-8",   //解决传中文乱码
			success:function(res){
				if(res=="1"){
					$("#loginForm").submit();
				}else if(res=="2"){
					alert("用户不存在");
				}else if(res=="3"){
					alert("密码错误");
				}else{
					alert("鬼知道什么错误");
				}
			},
			error:function(){
	 
			}
		});
	});
	$(".exit").click(function(){
		setCookie("im_clientID","");
		location.href="/";
	});
	$("#sendMsgInput").focusin(function(){
		if($.trim($(this).html())==default_info){
			$(this).html("");
		}
	});
	$("#sendMsgInput").focusout(function(){
		if($.trim($(this).html())==""){
			$(this).html(default_info);
		}
	});
	$(".person_name").off().on().click(function(){
		var target_name=$(this).text();
		$("#sendMsgInput").html("@"+target_name+"@");
		targetClientId=target_name;
	});
	$("#addplotbtn").click(function(){
		addplotbtn();
	});
	$("#saveplotbtn").click(function(){
		updatePlot();
	});
	$(".plotspan").click(function(){
		if($(this).hasClass("current")){
			return ;
		}
		$(".plotspan").toggleClass("current");
		$(".plot table").toggleClass("phide");
		getPlot($(".plotdiv>.current").attr("data-type"));
	});
	
	
});
	
function normalInfo(time,name,content,level,tts,tname){
	var info = "<div class=\"talk\"><span class=\"time\">"+time+"</span>" +
			"<span><img src=\"images/icon"+level+".png\" >" +
			"<div class=\"person_name\"><a href=\"javascript:;\" class=\"name\">"+name+"</a></div>";
			if(tname!=null&&tname!=""){
				info+="<i class=\"at\">对</i><div class=\"person_name\"><a href=\"javascript:;\" class=\"name\">"+tname+"</a></div>";
			}
			info+="</span><div class=\"talk_hua\"><p>"+content+"</p></div></div>";
	if(tts=="black"){
		info = "<div class=\"talk\"><span class=\"time\">"+time+"</span>" +
				"<span><img src=\"images/icon"+level+".png\" >" +
				"<div class=\"person_name\"><a href=\"javascript:;\" class=\"name\">"+name+"</a></div>";
		if(tname!=null&&tname!=""){
			info+="<i class=\"at\">对</i><div class=\"person_name\"><a href=\"javascript:;\" class=\"name\">"+tname+"</a></div>";
		}
		info+="</span><div class=\"talk_hua talk_hua1\"><p>"+content+"</p></div></div>";
	}else if(tts=="red"){
		info = "<div class=\"talk\"><span class=\"time\">"+time+"</span>" +
				"<span><img src=\"images/icon"+level+".png\" >" +
				"<div class=\"person_name\"><a href=\"javascript:;\" class=\"name\">"+name+"</a></div>";
		if(tname!=null&&tname!=""){
			info+="<i class=\"at\">对</i><div class=\"person_name\"><a href=\"javascript:;\" class=\"name\">"+tname+"</a></div>";
		}
		info+="</span><div class=\"talk_hua talk_hua2\"><p>"+content+"</p></div></div>";
	}
	$("#topicbox").append(info);
	$(".mCustomScrollBox").scrollTop(99999);
}

function checkInfo(time,name,content,uid,level){
	var rd=new Date().getTime();
	var info = "<div class=\"talk "+uid+"\" data-uid=\""+uid+"\" id=\""+rd+"\"><span class=\"time\">"+time+"</span>" +
			"<span><img src=\"images/icon"+level+".png\" title=\"游客\"><div class=\"person_name\">" +
			"<a href=\"javascript:;\" class=\"name\">"+name+"</a></div></span><div class=\"talk_hua\"><p>"+content+"</p>" +
			"<input type=\"button\" value=\"通过\" onclick=\"checkMsg(this)\" class=\"checkbtn\" data-uid=\""+uid+"\" data-name=\""+name+"\" data-content=\""+encodeURI(content)+"\" data-rd=\""+rd+"\"/>" +
			"<input type=\"button\" value=\"不通过\" onclick=\"checkMsg(this)\" class=\"checkbtn\" data-uid=\""+uid+"\" data-rd=\""+rd+"\"/>" +
			"<input type=\"button\" value=\"禁言\" onclick=\"stop(this)\" class=\"checkbtn\" data-name=\""+name+"\" data-rd=\"qa"+rd+"\"/>" +
			"<input type=\"button\" value=\"踢出\" onclick=\"kick(this)\" class=\"checkbtn\" data-name=\""+name+"\" data-rd=\"qa"+rd+"\"/>" +
			"</div></div>";
	$("#topicbox").append(info);
	$(".mCustomScrollBox").scrollTop(99999);
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
function sendCaitiao(type){
	if(type=="pt掌声"){
		$("#sendMsgInput").html("<img width='28' height='28'  src='images/autoImg/shandian/1.gif'>"+
			     "<img width='28' height='28'  src='images/autoImg/shandian/2.gif'>"+
			     "<img width='28' height='28'  src='images/autoImg/shandian/3.gif'>"+
			     "<img width='28' height='28'  src='images/autoImg/shandian/4.gif'>"+
			     "<img width='28' height='28'  src='images/autoImg/shandian/5.gif'>"+
			     "<img width='28' height='28'  src='images/autoImg/shandian/6.gif'>"+
			     "<img width='28' height='28'  src='images/autoImg/shandian/7.gif'>");
	}else if(type=="pt送鲜花"){
		  $("#sendMsgInput").html("<img width='28' height='28'  src='images/autoImg/meigui/1.gif'>"+
				     "<img width='28' height='28'  src='images/autoImg/meigui/2.jpg'>"+
				     "<img width='28' height='28'  src='images/autoImg/meigui/3.gif'>"+
				     "<img width='28' height='28'  src='images/autoImg/meigui/4.gif'>"+
				     "<img width='28' height='28'  src='images/autoImg/meigui/5.gif'>"+
				     "<img width='28' height='28'  src='images/autoImg/meigui/6.jpg'>"+
				     "<img width='28' height='28'  src='images/autoImg/meigui/7.gif'>");
	}else if(type=="pt顶一个"){
		 $("#sendMsgInput").html("<img width='28' height='28'  src='images/autoImg/ding/1.gif'>"+
			     "<img width='28' height='28'  src='images/autoImg/ding/2.gif'>"+
			     "<img width='28' height='28'  src='images/autoImg/ding/3.gif'>"+
			     "<img width='28' height='28'  src='images/autoImg/ding/4.gif'>"+
			     "<img width='28' height='28'  src='images/autoImg/ding/5.gif'>"+
			     "<img width='28' height='28'  src='images/autoImg/ding/6.gif'>"+
			     "<img width='28' height='28'  src='images/autoImg/ding/7.gif'>");
	}else if(type=="pt赞一个"){
		  $("#sendMsgInput").html("<img width='28' height='28'  src='images/autoImg/zan/1.gif'>"+
				     "<img width='28' height='28'  src='images/autoImg/zan/2.gif'>"+
				     "<img width='28' height='28'  src='images/autoImg/zan/3.gif'>"+
				     "<img width='28' height='28'  src='images/autoImg/zan/4.gif'>"+
				     "<img width='28' height='28'  src='images/autoImg/zan/5.gif'>"+
				     "<img width='28' height='28'  src='images/autoImg/zan/6.gif'>"+
				     "<img width='28' height='28'  src='images/autoImg/zan/7.gif'>");
	}else if(type=="pt给力"){
		 $("#sendMsgInput").html("<img width='28' height='28'  src='images/autoImg/geili/1.gif'>"+
			     "<img width='28' height='28'  src='images/autoImg/geili/2.gif'>"+
			     "<img width='28' height='28'  src='images/autoImg/geili/3.gif'>"+
			     "<img width='28' height='28'  src='images/autoImg/geili/4.gif'>"+
			     "<img width='28' height='28'  src='images/autoImg/geili/5.gif'>"+
			     "<img width='28' height='28'  src='images/autoImg/geili/6.gif'>"+
			     "<img width='28' height='28'  src='images/autoImg/geili/7.gif'>");
	}else if(type=="pt棒！"){
		$("#sendMsgInput").html("<img width='28' height='28'  src='images/autoImg/bang/1.gif'>"+
			     "<img width='28' height='28'  src='images/autoImg/bang/2.gif'>"+
			     "<img width='28' height='28'  src='images/autoImg/bang/3.gif'>"+
			     "<img width='28' height='28'  src='images/autoImg/bang/4.gif'>"+
			     "<img width='28' height='28'  src='images/autoImg/bang/5.gif'>"+
			     "<img width='28' height='28'  src='images/autoImg/bang/6.gif'>"+
			     "<img width='28' height='28'  src='images/autoImg/bang/7.gif'>");
	}
//	po_Last_Div();
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

function getPlot(type){
	var tp=type+"plot";
	$.ajax({
		url:"api/?type="+type,
		type:"get",
		dataType:"json",
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",   //解决传中文乱码
		success:function(res){
			var tb=$("#"+tp).find("tbody");
			tb.empty();
			for(var i=0;i<res.length;i++){
				var orderno=res[i].orderno;
				var opentime=res[i].opentime;
				var type=res[i].type;
				var product=res[i].product;
				var openprice=res[i].openprice;
				var downprice=res[i].downprice;
				var upprice=res[i].upprice;
				var closetime=res[i].closetime;
				var closeprice=res[i].closeprice;
				var gainprice=res[i].gainprice;
				var tname=res[i].tname;
				var s="<tr>";
				s+="<td>"+orderno+"</td>";
				s+="<td>"+opentime+"</td>";
				s+="<td>"+type+"</td>";
				s+="<td>"+product+"</td>";
				s+="<td>"+openprice+"</td>";
				s+="<td>"+downprice+"</td>";
				s+="<td>"+upprice+"</td>";
				s+="<td>"+closetime+"</td>";
				s+="<td>"+closeprice+"</td>";
				s+="<td>"+gainprice+"</td>";
				s+="<td>"+tname+"</td>";
				s+="</tr>"
				tb.append(s);
			}
		},
		error:function(){
 
		},
		complete : function(XMLHttpRequest,status){ //请求完成后最终执行参数
			
		}
	});
}
function addplotbtn(){
	if($(".plot").find("input:text").length>5){
		return;
	}
	var tp=$(".plotdiv>.current").attr("data-type")+"plot";
//	$("#saveplotbtn").show();
	var tb=$("#"+tp).find("tbody");
	var s="<tr>";
	s+="<td><input type=\"text\" class=\"adplotbtn\"></input></td>";
	s+="<td><input type=\"text\" class=\"adplotbtn\"></input></td>";
	s+="<td><input type=\"text\" class=\"adplotbtn\"></input></td>";
	s+="<td><input type=\"text\" class=\"adplotbtn\"></input></td>";
	s+="<td><input type=\"text\" class=\"adplotbtn\"></input></td>";
	s+="<td><input type=\"text\" class=\"adplotbtn\"></input></td>";
	s+="<td><input type=\"text\" class=\"adplotbtn\"></input></td>";
	s+="<td><input type=\"text\" class=\"adplotbtn\"></input></td>";
	s+="<td><input type=\"text\" class=\"adplotbtn\"></input></td>";
	s+="<td><input type=\"text\" class=\"adplotbtn\"></input></td>";
	s+="<td><input type=\"text\" class=\"adplotbtn\"></input></td>";
	s+="</tr>"
	tb.prepend(s);
}
function updatePlot(){
	$.ajax({
		url:"api/update",
		type:"post",
		data:{"orderno":$(".adplotbtn:eq(0)").val(),"opentime":$(".adplotbtn:eq(1)").val(),"type":$(".adplotbtn:eq(2)").val()
			,"product":$(".adplotbtn:eq(3)").val(),"openprice":$(".adplotbtn:eq(4)").val()
			,"downprice":$(".adplotbtn:eq(5)").val(),"upprice":$(".adplotbtn:eq(6)").val(),"closetime":$(".adplotbtn:eq(7)").val()
			,"closeprice":$(".adplotbtn:eq(8)").val(),"gainprice":$(".adplotbtn:eq(9)").val(),"tname":$(".adplotbtn:eq(10)").val()
		},
		dataType:"json",
		contentType:"application/x-www-form-urlencoded;charset=UTF-8",   //解决传中文乱码
		success:function(){
			
		},
		error:function(){
 
		},
		complete : function(XMLHttpRequest,status){ //请求完成后最终执行参数
			getPlot($(".plotdiv>.current").attr("data-type"));
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