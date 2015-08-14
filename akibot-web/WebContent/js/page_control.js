$(document).ready(function() {
	init();
});

function init() {
	$('#testRequestButton').click(function() {
		doTestRequest(1)
	});
}

function doTestRequest(x) {
	var testRequest = new Object();
	testRequest.x = x;

	var jsonDataStr = JSON.stringify(testRequest);
	console.log('doTestRequest: jsonDataStr' + jsonDataStr);

	var myURL = "../webapi/services/control/testRequest";
	$.ajax({
		type : "PUT",
		url : myURL,
		contentType : "application/json",
		data : jsonDataStr,
		success : function(result) {
			//console.log('SUCCESS');
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log("jqXHR statusCode" + jqXHR.statusCode());
			console.log("textStatus " + textStatus);
			console.log("errorThrown " + errorThrown);
		}
	});
}
