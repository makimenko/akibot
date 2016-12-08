var AkiNetwork = {
	webSocket : null,

	getWsUrl : function(s) {
		var l = window.location;
		return ((l.protocol === "https:") ? "wss://" : "ws://") + l.hostname + (((l.port != 80) && (l.port != 443)) ? ":" + l.port : "") + l.pathname
				+ s;
	},

	initWs : function() {
		this.webSocket = new WebSocket(this.getWsUrl("/../../actions"));
		this.webSocket.onopen = AkiEvents.onWsOpen;
		this.webSocket.onmessage = AkiEvents.onWsMessage;
		this.webSocket.onerror = AkiEvents.onWsError;
		this.webSocket.onclose = AkiEvents.onWsClose;
	},

	sendWsRequest : function(requestObject) {
		if (this.webSocket.readyState == this.webSocket.OPEN) {
			text = JSON.stringify(requestObject);
			console.log("sendWsRequest: " + text);
			this.webSocket.send(text);

		} else {
			alert("The Socket is Closed! Can not send request!");
		}
	},

	enrichWebSocketMessage : function(className) {
		this.className = className;
	}

}
