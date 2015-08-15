var CONTROL_URL = "../webapi/services/control";
var CONTENT_TYPE = "application/json; charset=utf-8";

$(function() {
	$("#navigationDiv").load("include/navigation.html", highlightNavigation);
});

function highlightNavigation() {
	var pathname = window.location.pathname;
	var index = pathname.lastIndexOf("/") + 1;
	var filename = pathname.substr(index);

	$("#xxx a[href='" + filename + "']").addClass("active");
}

function logError(jqXHR, textStatus, errorThrown) {
	console.log("jqXHR statusCode" + jqXHR.statusCode());
	console.log("textStatus " + textStatus);
	console.log("errorThrown " + errorThrown);
}

function logSuccess(result) {
	// console.log('SUCCESS');
}

function callService(subUrl, jsonDataStr) {
	console.log('callService: ' + subUrl + ': ' + jsonDataStr);
	$.ajax({
		type : "PUT",
		url : CONTROL_URL + "/" + subUrl,
		contentType : CONTENT_TYPE,
		data : jsonDataStr,
		success : logSuccess,
		error : logError
	});
}

function callService2(subUrl) {
	console.log('callService: ' + subUrl);
	$.ajax({
		type : "PUT",
		url : CONTROL_URL + "/" + subUrl,
		contentType : CONTENT_TYPE,
		success : logSuccess,
		error : logError
	});
}
