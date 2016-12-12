var AkiNetwork = {
	webSocket : null,
	stompClient : null,

	initWs : function() {
		this.webSocket = new SockJS('/ws');
		this.stompClient = Stomp.over(this.webSocket);
		this.stompClient.connect({}, function(frame) {
			console.log('Connected: ' + frame);
			AkiNetwork.stompClient.subscribe('/topic/worldResponse', AkiEvents.onWorldResponse);
			AkiNetwork.stompClient.subscribe('/topic/worldResponse', AkiEvents.onWorldResponse);
			AkiNetwork.sendWorldRequest(new AkiNetwork.enrichWebSocketMessage("WorldContentRequest"));
		});
	},

	sendWorldRequest : function(worldRequest) {
		text = JSON.stringify(worldRequest);
		console.log("sendWorldRequest: " + text);
		AkiNetwork.stompClient.send("/app/worldRequest", {}, text);
	},

	enrichWebSocketMessage : function(className) {
		this.className = className;
	}

}
