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
		doMotionRequest("STOP");
	});

	$('#leftButton').click(function() {
		doMotionRequest("LEFT");
	});

	$('#rightButton').click(function() {
		doMotionRequest("RIGHT");
	});

	$('#forwardButton').click(function() {
		doMotionRequest("FORWARD");
	});

	$('#backwardButton').click(function() {
		doMotionRequest("BACKWARD");
	});

	$('#distanceButton').click(function() {
		doSimpleRequest("distanceRequest");
	});

	$('#scoutDistanceAroundButton').click(function() {
		doSimpleRequest("scoutDistanceAroundRequest");
	});
	
	
	$('#gyroscopeValueButton').click(function() {
		doSimpleRequest("gyroscopeValueRequest");
	});

	$('#servoCenterButton').click(function() {
		doSimpleRequest("servoCenterRequest");
	});

	$('#speechSynthesisButton').click(
			function() {
				doSpeechSynthesisRequest($('#speechSynthesisTextInput').val(),
						$('#speechSynthesisVoiceInput').val());
			});

	$('#gyroscopeCalibrationButton').click(function() {
		doSimpleRequest("gyroscopeCalibrationRequest");
	});

	
}


function doSpeechSynthesisRequest(speechText, voice) {
	var speechSynthesisRequest = new Object();
	speechSynthesisRequest.speechText = speechText;
	speechSynthesisRequest.voice = voice;

	callService("speechSynthesisRequest", JSON
			.stringify(speechSynthesisRequest));
}


function doMotionRequest(direction, milliseconds) {
	var timedMotionMillisecondsInput = $('#timedMotionMillisecondsInput').val();
	if (timedMotionMillisecondsInput.length == 0) {
		doStickMotionRequest(direction)
	} else {
		doTimedMotionRequest(direction, timedMotionMillisecondsInput);
	}
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
