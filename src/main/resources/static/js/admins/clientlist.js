$(function() {
	$(".updateBtn").click(function() {
		location.href = $(this).attr("data") + "/";
	});
	$(".subbtn").click(function() {
		location.href = "add/";
	});
	$(".querybtn").click(function() {
		var sname=$("#sname").val();
		location.href = "list?name="+sname;
	});
	var prev, next, pagelist="";
	if (pagenum == 0) {
		prev = "<li class=\"paginate_button previous disabled\" aria-controls=\"dataTables-example\" tabindex=\"0\" id=\"dataTables-example_previous\"><a href=\"#\">Previous</a></li>";
	} else {
		prev = "<li class=\"paginate_button previous\" aria-controls=\"dataTables-example\" tabindex=\"0\" id=\"dataTables-example_previous\"><a href=\"javascript:toprev();\">Previous</a></li>";
	}
	if (pagenum + 1 == lastpage) {
		next = "<li class=\"paginate_button next disabled\" aria-controls=\"dataTables-example\" tabindex=\"0\" id=\"dataTables-example_next\"><a href=\"#\">Next</a></li>";
	} else {
		next = "<li class=\"paginate_button next\" aria-controls=\"dataTables-example\" tabindex=\"0\" id=\"dataTables-example_next\"><a href=\"javascript:tonext();\">Next</a></li>";
	}
	for (var i = 2; i > 0; i--) {
		if (pagenum - i > -1) {
			pagelist += "<li class=\"paginate_button\" aria-controls=\"dataTables-example\" tabindex=\"0\"><a href=\"javascript:topage("
					+ (pagenum + 1 - i)
					+ ");\">"
					+ (pagenum + 1 - i)
					+ "</a></li>";
		}
	}
	pagelist += "<li class=\"paginate_button active\" aria-controls=\"dataTables-example\" tabindex=\"0\"><a href=\"#\">"
			+ (pagenum + 1) + "</a></li>";
	for (var i = 1; i < 3; i++) {
		if (pagenum + 1 + i <= lastpage) {
			pagelist += "<li class=\"paginate_button\" aria-controls=\"dataTables-example\" tabindex=\"0\"><a href=\"javascript:topage("
					+ (pagenum + 1 + i)
					+ ");\">"
					+ (pagenum + 1 + i)
					+ "</a></li>";
		}
	}
	$(".pagination").append(prev + pagelist + next);
	
});

function topage(num){
	location.href="list?page="+num;
}
function tonext(){
	topage(pagenum+2);
}
function toprev(){
	topage(pagenum);
}