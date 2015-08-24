$(document).ready(function() {
	init();
});

function init() {
	var socket = new WebSocket(wsurl("/../../actions"));
	socket.onmessage = onMessage;
}

function onMessage(message) {
	var jsonDataStr = JSON.stringify(message);
	console.log("onMessage: jsonDataStr: " + jsonDataStr);
	//var object = JSON.parse(message.data);
	$("<p>" + jsonDataStr + "</p>").insertBefore($("#incomingLogPanel p:first"));
}
