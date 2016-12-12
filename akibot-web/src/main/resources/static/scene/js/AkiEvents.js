var AkiEvents = {
	worldUpdateInProgress : false,

	mouseLastDownTime : null,
	mouseDoubleClickSpeed : 300,
	lastLocation : null,
	locationTolerance : 10,

	onDocumentReady : function() {
		AkiScene.initScene();
		window.addEventListener('keypress', AkiEvents.onKeyPress, false);
		window.addEventListener('mousemove', AkiEvents.onMouseMove, false);
		AkiScene.container.addEventListener('mousedown', AkiEvents.onMouseDown, false);
		AkiScene.container.addEventListener('mouseup', AkiEvents.onMouseUp, false);
		AkiNetwork.initWs();
	},

	onKeyPress : function(event) {
		var char = AkiUtils.getChar(event);
		if (char == "g") {
			AkiUtils.toggleVisibility(AkiScene.scene, AkiUtils.GRID_HELPER_NAME);
			AkiScene.render();
		} else if (char == "a") {
			AkiUtils.toggleVisibility(AkiScene.scene, AkiUtils.AXIS_HELPER_NAME);
			AkiScene.render();
		} else if (char == "f") {
			if (isTHREExFullScreen) {
				THREEx.FullScreen.cancel();
			} else {
				THREEx.FullScreen.request(AkiScene.container);
			}
			AkiScene.render();
		}
	},

	onTouchStart : function(event) {
		if (event.touches.length === 1) {
			event.preventDefault();
			AkiEvents.onAnyClick({
				x : event.touches[0].pageX,
				y : event.touches[0].pageY
			});
		}
	},

	onMouseMove : function(event) {
		if (this.mouseLastDownTime != null) {
			this.mouseLastDownTime = null;
		}
	},

	onMouseDown : function(event) {
		AkiEvents.onAnyClick({
			x : event.clientX,
			y : event.clientY
		});
	},

	onAnyClick : function(location) {
		var timeNow = Date.now();
		var diff = timeNow - this.mouseLastDownTime;
		if (diff < AkiEvents.mouseDoubleClickSpeed && this.lastLocation != null
				&& Math.abs(location.x - this.lastLocation.x) < this.locationTolerance
				&& Math.abs(location.y - this.lastLocation.y) < this.locationTolerance) {
			this.mouseLastDownTime = null;
			AkiEvents.onCommand(location);
		}
		this.mouseLastDownTime = timeNow;
		this.lastLocation = location;
	},

	onMouseUp : function(event) {
		// console.log("onMouseUp");
	},

	onCommand : function(location) {
		// console.log("onMouseDblClick");

		var mouseVector = new THREE.Vector3();
		mouseVector.x = 2 * (location.x / AkiScene.container.offsetWidth) - 1;
		mouseVector.y = 1 - 2 * (location.y / AkiScene.container.offsetHeight);

		var raycaster = new THREE.Raycaster();
		raycaster.setFromCamera(mouseVector, AkiScene.camera);

		// TODO: restrict only to grid + remove IF/comparison below
		var parentNode = AkiScene.sceneContent;// .getObjectByName("world");
		var intersects = raycaster.intersectObjects(parentNode.children, true);

		for (var i = 0; i < intersects.length; i++) {
			var intersection = intersects[i];
			var obj = intersection.object;

			if (obj == AkiScene.gridDetail.gridGroup) {
				var point = intersection.point;
				// console.log("*** i=" + i + ": " + obj.name + "/ " +
				// point.x +
				// ", " + point.y + ", " + point.z);

				AkiCursor.startCursor(AkiScene.sceneContent, point);

				// Send request:
				var lookAtRequest = new AkiNetwork.enrichWebSocketMessage("LookAtOrientationRequest");
				lookAtRequest.lookAt = new Object();
				lookAtRequest.lookAt.x = point.x;
				lookAtRequest.lookAt.y = point.y;
				lookAtRequest.lookAt.z = point.z;
				AkiNetwork.sendWsRequest(lookAtRequest);

			}
		}
	},

	onWindowResize : function() {
		width = $('#' + AkiScene.CONTAINER_DIV_NAME).width();
		height = $('#' + AkiScene.CONTAINER_DIV_NAME).height();

		AkiScene.camera.aspect = width / height;
		AkiScene.camera.updateProjectionMatrix();

		AkiScene.renderer.setSize(width, height);
		// AkiScene.controls.handleResize();
		AkiScene.render();
	},

	onDocumentError : function(errorMsg, url, lineNumber) {
		alert('Document Error: ' + errorMsg + ' Script: ' + url + ' Line: ' + lineNumber);
	},

	onWsError : function(event) {
		alert('WebSocket Error: ' + event);
	},

	onWsClose : function(event) {
		var reason;
		// See http://tools.ietf.org/html/rfc6455#section-7.4.1
		if (event.code == 1000)
			reason = "Normal closure, meaning that the purpose for which the connection was established has been fulfilled.";
		else if (event.code == 1001)
			reason = "An endpoint is \"going away\", such as a server going down or a browser having navigated away from a page.";
		else if (event.code == 1002)
			reason = "An endpoint is terminating the connection due to a protocol error";
		else if (event.code == 1003)
			reason = "An endpoint is terminating the connection because it has received a type of data it cannot accept (e.g., an endpoint that understands only text data MAY send this if it receives a binary message).";
		else if (event.code == 1004)
			reason = "Reserved. The specific meaning might be defined in the future.";
		else if (event.code == 1005)
			reason = "No status code was actually present.";
		else if (event.code == 1006)
			reason = "The connection was closed abnormally, e.g., without sending or receiving a Close control frame";
		else if (event.code == 1007)
			reason = "An endpoint is terminating the connection because it has received data within a message that was not consistent with the type of the message (e.g., non-UTF-8 [http://tools.ietf.org/html/rfc3629] data within a text message).";
		else if (event.code == 1008)
			reason = "An endpoint is terminating the connection because it has received a message that \"violates its policy\". This reason is given either if there is no other sutible reason, or if there is a need to hide specific details about the policy.";
		else if (event.code == 1009)
			reason = "An endpoint is terminating the connection because it has received a message that is too big for it to process.";
		else if (event.code == 1010) // Note that this status code is not
			// used by the server, because it can
			// fail the WebSocket handshake instead.
			reason = "An endpoint (client) is terminating the connection because it has expected the server to negotiate one or more extension, but the server didn't return them in the response message of the WebSocket handshake. <br /> Specifically, the extensions that are needed are: "
					+ event.reason;
		else if (event.code == 1011)
			reason = "A server is terminating the connection because it encountered an unexpected condition that prevented it from fulfilling the request.";
		else if (event.code == 1015)
			reason = "The connection was closed due to a failure to perform a TLS handshake (e.g., the server certificate can't be verified).";
		else
			reason = "Unknown reason";

		alert('WebSocket Closed: ' + event.code + ": " + reason);
	},
	onWsOpen : function() {
		AkiScene.sendWorldContentRequest();
	},

	onWorldResponse : function(message) {
		console.log("onWorldResponse");
		var messageObject = JSON.parse(message.body);
		if (messageObject.className == "WorldContentResponse") {
			AkiEvents.onWorldContentResponse(messageObject);
		} else if (messageObject.className == "WorldNodeTransformationEvent") {
			AkiEvents.onWorldNodeTransformationEvent(messageObject);
		} else if (messageObject.className == "GridGeometryFullTransformationEvent") {
			AkiEvents.onGridGeometryFullTransformationEvent(messageObject);
		} else if (messageObject.className == "GridGeometryPartialTransformationEvent") {
			AkiEvents.onGridGeometryPartialTransformationEvent(messageObject);
		} else {
			console.log("Unsupported className received: " + messageObject.className);
		}
	},

	onWorldContentResponse : function(message) {
		console.log("onWorldContentResponse");
		AkiScene.reloadWorldContentResponse(message);
	},

	onWorldNodeTransformationEvent : function(worldNodeTransformationEvent) {
		console.log("onWorldNodeTransformationEvent (for " + worldNodeTransformationEvent.nodeName + ")");
		var object3d = AkiScene.scene.getObjectByName(worldNodeTransformationEvent.nodeName, true);

		if (object3d == null) {
			console.log("WorldNodeTransformationEvent can't find object to be transformed, reloading WorldContent")
			AkiScene.sendWorldContentRequest();
		} else {
			// console.log("onWorldNodeTransformationEvent: object found");
			AkiScene.applyTransformation(object3d, worldNodeTransformationEvent.transformation);
			AkiScene.render();
		}
	},

	onGridGeometryFullTransformationEvent : function(event) {
		// console.log("onGridGeometryFullTransformationEvent (" +
		// event.gridNodeName + ")");
		var gridGroup = AkiScene.scene.getObjectByName(event.gridNodeName, true);
		if (gridGroup == null) {
			// console.log("onGridGeometryFullTransformationEvent can't find
			// object to be transformed, reloading WorldContent")
			AkiScene.sendWorldContentRequest();
		} else {
			AkiScene.applyGridGeometryFullTransformation(gridGroup, event.gridGeometry.grid);
			AkiScene.render();
		}
	},

	onGridGeometryPartialTransformationEvent : function(event) {
		// console.trace("onGridGeometryFullTransformationEvent (" +
		// event.gridNodeName + ")");
		var gridGroup = AkiScene.scene.getObjectByName(event.gridNodeName, true);
		if (gridGroup == null) {
			// console.log("GridGeometryPartialTransformationEvent can't
			// find
			// object to be transformed, reloading WorldContent")
			AkiScene.sendWorldContentRequest();
		} else {
			// console.log("onGridGeometryPartialTransformationEvent: object
			// found");
			AkiScene.applyGridGeometryPartialTransformation(gridGroup, event);
			AkiScene.render();
		}
	},

	onControlChange : function() {
		AkiScene.render();
	}
}
