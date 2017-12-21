var socket = io.connect(serverUrl+"?clientid=" + clientid);
socket.on("connect",function() {
	//output("<span>Client has connected to the server!</span>");
	//normalInfo(moment().format("HH:mm"),clientid,"欢迎"+clientid+"进入直播间",level,"","");
});
socket.on("messageevent", function(data) {
	//output("<span>" + data.sourceClientId + ":</span> " + data.msgContent);
	if(data.sourceClientId!=clientid){
		normalInfo(moment().format("HH:mm"),data.sourceClientId,data.msgContent,data.level,data.msgType,data.targetClientId);
	}
});
socket.on("checkevent", function(data) {
	//output("<span>" + data.sourceClientId + ":</span> " + data.msgContent);
	checkInfo(moment().format("HH:mm"),data.sourceClientId,data.msgContent,data.id,data.level);
});
socket.on("ccevent", function(data) {
	//output("<span>" + data.sourceClientId + ":</span> " + data.msgContent);
	ccinfo(data.type,data.ddInfo);
});
socket.on("topevent", function(data) {
	$("#marq ul li").html(data.msgContent);
});
socket.on("scrolevent", function(data) {
	$(".fly_word").html(data.msgContent);
	$(".fly b").animate({left:'-3000px'},90000,'linear',function(){
		$(".fly b").css({left:'100%'})
    })
});
socket.on("disconnect",	function() {
//	output("<span>The client has disconnected!</span>");
	//normalInfo(moment().format("HH:mm"),clientid,clientid+"离开直播间",level,"","");
});

function sendDisconnect() {
	socket.disconnect();
}

function sendMessage(message,cid,tcid,tts) {
	if(level==0){
		return;
	}
	if(message==default_info){
		return;
	}
	var jsonObject = {
		sourceClientId : cid,
		targetClientId : tcid,
		msgType : tts,
		msgContent : decodeURI(message)
	};
	socket.emit("messageevent", jsonObject);
}

function sendcc(type,dd){
	if(level==0){
		return ;
	}
	var jsonObject ={
		type:type,
		ddInfo:dd
	};
	socket.emit("ccevent", jsonObject);
}

