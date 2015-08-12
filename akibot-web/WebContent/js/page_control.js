$(document).ready(function() {
	init();
});

function init() {
	$('#testRequestButton').click(function() {
		doTestRequest(1)
	});

	var socket = new WebSocket(wsurl("/../../actions"));
	socket.onmessage = onMessage;

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

function onMessage(message) {
	var jsonDataStr = JSON.stringify(message);
	console.log("onMessage: jsonDataStr: " + jsonDataStr);
	//var object = JSON.parse(message.data);
	$("<p>" + jsonDataStr + "</p>").insertBefore($("#logPanel p:first"));
}

function wsurl(s) {
	var l = window.location;
	return ((l.protocol === "https:") ? "wss://" : "ws://") + l.hostname
			+ (((l.port != 80) && (l.port != 443)) ? ":" + l.port : "")
			+ l.pathname + s;
}
