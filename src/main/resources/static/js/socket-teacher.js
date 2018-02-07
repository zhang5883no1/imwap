var socket = io.connect(serverUrl+"?ssid=" + ssid);
socket.on("connect",function() {
	
});
socket.on("messageevent", function(data) {
	if(data.sourceClientId!=clientid){
		createMsg(data);
	}
});
socket.on("ccevent", function(data) {
	ccinfo(data.type,data.ddInfo);
});
socket.on("disconnect",	function() {
	
});
socket.on("serverinfo",function(data){
	totalclints=data.totalvisiter;
})

function sendDisconnect() {
	socket.disconnect();
}

function sendMessage(message,cid,tcid,tts) {
	if(level==0){
		return;
	}
	if($.trim(message)==""){
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

_heart=setInterval(heartthread, 60*1000); 
function heartthread(){
	var jsonObject ={
		type:"heart",
		ddInfo:ssid
	};
	socket.emit("ccevent", jsonObject);
}
