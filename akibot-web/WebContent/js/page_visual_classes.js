function Animal(name) {
	this.speed = 0;
	this.name = name;

	this.run = function(speed) {
		this.speed += speed;
	};

	this.stop = function() {
		this.speed = 0;
	};
};

function WebSocketMessage(messageClass) {
	this.messageClass = messageClass;
};
