var CONTROL_URL = "../webapi/services/control";
var CONTENT_TYPE = "application/json";

$(document).ready(function() {
	init();
});

function init() {
	$('#testRequestButton').click(function() {
		doTestRequest(1)
	});

	$('#orientationButton').click(
			function() {
				doOrientationRequest($('#northDegreesXYInput').val(), $(
						'#precissionDegreesInput').val(), $(
						'#timeoutMillisInput').val());
			});

	$('#stopButton').click(function() {
		// timedMotionMillisecondsInput
		var timedMotionMillisecondsInput = doTimedMotionRequest("STOP", 123);
	});

	$('#leftButton').click(function() {
		doStickMotionRequest("LEFT");
	});

	$('#rightButton').click(function() {
		doStickMotionRequest("RIGHT");
	});

	$('#forwardButton').click(function() {
		doStickMotionRequest("FORWARD");
	});

	$('#backwardButton').click(function() {
		doStickMotionRequest("BACKWARD");
	});
}

function doStickMotionRequest(direction) {
	var stickMotionRequest = new Object();
	stickMotionRequest.directionType = direction;

	callService("stickMotionRequest", JSON.stringify(stickMotionRequest));
}

function doTimedMotionRequest(direction, milliseconds) {
	var timedMotionRequest = new Object();
	timedMotionRequest.directionType = direction;
	timedMotionRequest.milliseconds = milliseconds;

	callService("timedMotionRequest", JSON.stringify(timedMotionRequest));
}

function doOrientationRequest(northDegreesXY, precissionDegrees, timeoutMillis) {
	var orientationRequest = new Object();
	orientationRequest.northDegreesXY = northDegreesXY;
	orientationRequest.precissionDegrees = precissionDegrees;
	orientationRequest.timeoutMillis = timeoutMillis;

	callService("orientationRequest", JSON.stringify(orientationRequest));
}

function doTestRequest(x) {
	var testRequest = new Object();
	testRequest.x = x;

	callService("testRequest", JSON.stringify(testRequest));
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
