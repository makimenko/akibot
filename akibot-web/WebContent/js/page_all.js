$(function(){
	$("#navigationDiv").load("include/navigation.html", function() {
		var pathname = window.location.pathname;
		var index = pathname.lastIndexOf("/") + 1;
		var filename = pathname.substr(index);
		
		$("#xxx a[href='"+filename+"']").addClass("active");
	});

});

