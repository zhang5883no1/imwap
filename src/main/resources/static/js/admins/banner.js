$(function(){
	var bannername="";
	$(".btn").click(function() {
		bannername=$(this).attr("data-name");
		$("#file").click();
	});
	$("#file").change(function() {
		$("#uploadBtn").click();
	});
	$("#uploadBtn").click(function() {
		var name=bannername;
		var formData = new FormData();
		formData.append('file', $('#file')[0].files[0]);
		formData.append('filename',name);
		$.ajax({
			url : '/admin/banner/update',
			type : 'POST',
			cache : false,
			data : formData,
			processData : false,
			contentType : false
		}).done(
				function(res) {
					alert(name+"更新成功");
					$("#"+ name).attr("src","http://static.qizhihuitrade.cn/banner/"+name+".jpg");
				}).fail(function(res) {
		});
	});
});
