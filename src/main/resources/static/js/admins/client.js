$(function() {
	var ls=location+"";
	var name=$("#clientId").val();
	$("#subbtn").click(function() {
		if ($.trim($("#clientId").val()) == "") {
			alert("用户名为空你是想干嘛？");
			return;
		}
		if ($.trim($("#password").val()) == "") {
			alert("密码为空你是想干嘛？");
			return;
		}
		if(ls.indexOf("add")==-1){
			//修改
			if(name==$("#clientId").val()){
				$("#formbody").submit();
				return;
			}
		}
		$.ajax({
			url : "/login?name=" + encodeURI($("#clientId").val()) + "&pwd=",
			type : "get",
			dataType : "json",
			timeout : 1500,
			contentType : "application/x-www-form-urlencoded;charset=UTF-8", // 解决传中文乱码
			success : function(res) {
				if (res == "2") {
					$("#formbody").submit();
				} else {
					alert("用户名已存在");
				}
			},
			error : function() {

			}
		});

	});
});