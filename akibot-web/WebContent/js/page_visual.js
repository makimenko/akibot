if (!Detector.webgl)
	Detector.addGetWebGLMessage();

$(document).ready(init);

var container, stats;
var camera, cameraTop, cameraFront;
var controls, scene, renderer;
var cross;
var ws;
var gridHelper;
var axisHelper;
var currentCamera = "1";
var gridGroup, grid, cellObjectTemplate, cellSize;
var matEmpty, matOccupied;
var gridInitialVisibility = false;
var axisInitialVisibility = false;
var labelInitialVisibility = false;

function init() {
	initWs();
	initScene();
	window.addEventListener('keypress', onKeyPress, false);
}

function initWs() {
	ws = new WebSocket(wsurl("/../../actions"));
	ws.onopen = onWsOpen;
	ws.onmessage = onWsMessage;
}

function onWsMessage(message) {
	var jsonDataStr = JSON.stringify(message);
	console.log("onMessage: jsonDataStr: " + jsonDataStr);
	var messageObject = JSON.parse(message.data);

	if (messageObject.className == "WorldContentResponse") {
		addNodeRecursion(messageObject.worldNode);
		render();
	} else if (messageObject.className == "NodeTransformationMessage") {
		var object3d = scene.getObjectByName(messageObject.nodeName, true);
		applyTransformation(object3d, messageObject.transformation);
		render();
	}
}

function addNodeRecursion(node) {
	addNode(node);
	if (node.childs != null) {
		for (i = 0; i < node.childs.length; i++) {
			addNodeRecursion(node.childs[i]);
		}
	}
}

function getMaterial(material) {
	var mat = new THREE.MeshPhongMaterial(material);
	return mat;
}

function addNode(node) {
	if (node.geometry == null) {
		emptyObject = new THREE.Object3D();
		addNodeFinalisation(emptyObject, node);
	} else if (node.geometry.className == "AkiBoxGeometry") {
		var geometry = new THREE.BoxGeometry(node.geometry.dimension.x,
				node.geometry.dimension.y, node.geometry.dimension.z);
		object3d = new THREE.Mesh(geometry, getMaterial(node.geometry.material));
		addNodeFinalisation(object3d, node);
	} else if (node.geometry.className == "AkiColladaGeometry") {
		var loader = new THREE.ColladaLoader();
		loader.load(node.geometry.fileName, function(collada) {
			object3d = collada.scene;
			addNodeFinalisation(object3d, node);
		});
	} else if (node.geometry.className == "AkiGridGeometry") {
		console.log("AkiGridGeometry");
		addLocationAreaGrid(node);
	}
}

function addLocationAreaGrid(node) {
	gridGroup = new THREE.Object3D();
	gridGroup.name = node.name;
	applyTransformation(gridGroup, node.transformation);

	grid = node.geometry.grid;

	matEmpty = new THREE.MeshPhongMaterial({
		color : 0x00ff00,
		// specular : 0x009900,
		// shininess : 30,
		// shading : THREE.FlatShading,
		opacity : 0.2,
		transparent : true
	});

	matOccupied = new THREE.MeshPhongMaterial({
		color : 0xff0000,
		// specular : 0x009900,
		// shininess : 30,
		// shading : THREE.FlatShading,
		opacity : 1
	// transparent : true
	});

	cellSize = node.geometry.akiGridConfiguration.cellSize;
	var geometry = new THREE.BoxGeometry(cellSize - 0.1, cellSize - 0.1, 0.5);
	cellObjectTemplate = new THREE.Mesh(geometry, matEmpty);

	for (x = 0; x < grid.length; x++) {
		for (y = 0; y < grid[0].length; y++) {
			if (grid[x][y] >= 0) {
				addGridCell(node, x, y, grid[x][y]);
			}
		}
	}

	scene.add(gridGroup);
	render();
}

function addGridCell(node, x, y, value) {

	cellObject = cellObjectTemplate.clone();
	cellObject.name = node.name + "_" + x + "_" + y;
	cellObject.position.x = x * cellSize + (cellSize / 2);
	cellObject.position.y = y * cellSize + (cellSize / 2);

	if (value == 0) {
		cellObject.material = matEmpty;
	} else if (value > 0) {
		cellObject.scale.z = grid[x][y] * 3;
		cellObject.material = matOccupied;
	}
	gridGroup.add(cellObject);
}

function addNodeFinalisation(object3d, node) {
	object3d.name = node.name;
	object3d.castShadow = node.castShadow;
	object3d.receiveShadow = node.receiveShadow;

	applyTransformation(object3d, node.transformation);
	object3d.updateMatrix();
	drawLabel(object3d, node.name);
	scene.add(object3d);
	render();
}

function applyTransformation(object3d, transformation) {
	if (transformation != null) {
		// Translation (move):
		if (transformation.position != null) {
			object3d.position.set(transformation.position.x,
					transformation.position.y, transformation.position.z);
		}
		// Rotation:
		if (transformation.rotation != null) {
			object3d.rotation.x = transformation.rotation.x;
			object3d.rotation.y = transformation.rotation.y;
			object3d.rotation.z = transformation.rotation.z;
		}
		// Scale:
		if (transformation.scale != null) {
			object3d.scale.set(transformation.scale.x, transformation.scale.y,
					transformation.scale.z);
		}
	}
}

function drawScene() {
	drawGridHelper();
	drawAxisHelper();
	// drawArrowHelper();
	// drawBoundingBoxHelper();
}

function drawLabel(node, text) {
	var spritey = makeTextSprite(text, {
		fontsize : 12,
		borderThickness : 1,
		fontColor : {
			r : 0,
			g : 100,
			b : 0,
			a : 0.8
		},
		borderColor : {
			r : 0,
			g : 100,
			b : 0,
			a : 0.2
		},
		backgroundColor : {
			r : 0,
			g : 100,
			b : 0,
			a : 0.05
		}
	});
	// spritey.position.set(15, 0, 0);
	spritey.visible = labelInitialVisibility;
	node.add(spritey);
}

function drawLights() {
	var directionalLight = new THREE.DirectionalLight(0xffffff);
	directionalLight.position.set(-500, -500, 1000);
	directionalLight.shadowCameraNear = 50;
	directionalLight.shadowCameraFar = 5000;
	directionalLight.shadowCameraFov = 50;
	directionalLight.intensity = 0.9;
	directionalLight.castShadow = true;
	directionalLight.shadowDarkness = 0.4;

	directionalLight.shadowMapWidth = 1024; // default is 512
	directionalLight.shadowMapHeight = 1024; // default is 512

	// directionalLight.shadowCameraVisible = true;
	scene.add(directionalLight);
}

function drawGridHelper() {
	// reference: https://www.script-tutorials.com/webgl-with-three-js-lesson-3
	gridHelper = new THREE.Object3D();// create an empty container
	gridHelper.visible = gridInitialVisibility;

	var gridHelper1 = new THREE.GridHelper(300, 100);
	gridHelper1.position = new THREE.Vector3(0, 0, 0);
	gridHelper1.rotation = new THREE.Euler(0, 0, 0);
	gridHelper.add(gridHelper1);

	var gridHelper2 = gridHelper1.clone();
	gridHelper2.rotation.set(Math.PI / 2, 0, 0);
	gridHelper.add(gridHelper2);

	var gridHelper3 = gridHelper1.clone();
	gridHelper3.rotation.set(Math.PI / 2, 0, Math.PI / 2);
	gridHelper.add(gridHelper3);

	this.scene.add(gridHelper);
}

function drawAxisHelper() {
	axisHelper = new THREE.AxisHelper(800); // 500 is size
	axisHelper.renderOrder = 999;
	axisHelper.visible = axisInitialVisibility;
	this.scene.add(axisHelper);
}

function drawArrowHelper() {
	var directionV3 = new THREE.Vector3(1, 0, 0);
	var originV3 = new THREE.Vector3(0, 0, 0);
	var arrowHelper = new THREE.ArrowHelper(directionV3, originV3, 50,
			0xff0000, 10, 5);
	this.scene.getObjectByName("robotMesh", true).add(arrowHelper);
}

function drawBoundingBoxHelper() {
	bboxHelper = new THREE.BoundingBoxHelper(this.scene.getObjectByName(
			"robotMesh", true), 0x999999);
	this.scene.add(bboxHelper);
}

function resetCameraPosition() {
	camera.position.x = -50;
	camera.position.y = -200;
	camera.position.z = 200;
	camera.up = new THREE.Vector3(0, 0, 1);
	camera.lookAt(new THREE.Vector3(0, 0, 0));

	if (scene != null && this.scene.getObjectByName("robotNode", true) != null) {
		// only is scene is initialized
		var robot = this.scene.getObjectByName("robotNode", true);
		var relativeCameraOffset = new THREE.Vector3(0, 0, 500);
		var cameraOffset = relativeCameraOffset.applyMatrix4(robot.matrixWorld);
		cameraTop.position.x = cameraOffset.x;
		cameraTop.position.y = cameraOffset.y;
		cameraTop.position.z = cameraOffset.z;
		cameraTop.up = new THREE.Vector3(0, 1, 0);
		cameraTop.lookAt(robot.position);
	} else {
		cameraTop.position.x = 0;
		cameraTop.position.y = 0;
		cameraTop.position.z = 500;
		cameraTop.up = new THREE.Vector3(0, 1, 0);
		cameraTop.lookAt(new THREE.Vector3(0, 0, 0));
	}

	cameraFront.position.x = 50;
	cameraFront.position.y = 200;
	cameraFront.position.z = 200;
	cameraFront.up = new THREE.Vector3(0, 0, 1);
	cameraFront.lookAt(new THREE.Vector3(0, 0, 0));
}

function initScene() {

	camera = new THREE.PerspectiveCamera(60, window.innerWidth
			/ window.innerHeight, 1, 1000);
	cameraTop = new THREE.PerspectiveCamera(60, window.innerWidth
			/ window.innerHeight, 1, 1000);
	cameraFront = new THREE.PerspectiveCamera(60, window.innerWidth
			/ window.innerHeight, 1, 1000);
	resetCameraPosition();

	controls = new THREE.TrackballControls(camera);
	controls.rotateSpeed = 1.0;
	controls.zoomSpeed = 1.2;
	controls.panSpeed = 0.8;
	controls.noZoom = false;
	controls.noPan = false;
	controls.staticMoving = true;
	controls.dynamicDampingFactor = 0.3;
	controls.keys = [ 65, 83, 68 ];
	controls.addEventListener('change', render);

	scene = new THREE.Scene();
	drawScene();
	drawLights();

	renderer = new THREE.WebGLRenderer({
		antialias : true
	});

	renderer.shadowMapEnabled = true;
	renderer.shadowMapType = THREE.PCFSoftShadowMap;
	renderer.shadowMapSoft = true;

	renderer.setClearColor(0xffffff, 1);
	renderer.setSize(window.innerWidth, window.innerHeight);
	renderer.autoClear = true;

	container = document.getElementById('container');
	container.appendChild(renderer.domElement);

	stats = new Stats();
	stats.domElement.style.position = 'absolute';
	stats.domElement.style.top = '0px';
	stats.domElement.style.zIndex = 100;
	container.appendChild(stats.domElement);

	window.addEventListener('resize', onWindowResize, false);

	render();
	onWindowResize();
}

function onWindowResize() {
	width = $('#container').width();
	height = window.innerHeight * 0.90;
	camera.aspect = width / height;
	camera.updateProjectionMatrix();

	cameraTop.aspect = width / height;
	cameraTop.updateProjectionMatrix();

	renderer.setSize(width, height);
	controls.handleResize();
	render();
}

function animate() {
	requestAnimationFrame(animate);
	if (controls != null) {
		controls.update();
	}
}

function render() {
	if (currentCamera == '2') {
		renderer.render(scene, cameraTop);
	} else if (currentCamera == '3') {
		renderer.render(scene, cameraFront);
	} else {
		renderer.render(scene, camera);
	}
	stats.update();
}

function onWsOpen() {
	sendWsRequest(new WebSocketMessage("WorldContentRequest"));
}

function sendWsRequest(requestObject) {
	ws.send(JSON.stringify(requestObject));
}

function onKeyPress(event) {
	var char = getChar(event);
	if (char == "g") {
		gridHelper.visible = !gridHelper.visible;
		render();
	} else if (char == "a") {
		axisHelper.visible = !axisHelper.visible;
		render();
	} else if (char == "1") {
		currentCamera = "1";
		controls.object = camera;
		resetCameraPosition();
		render();
	} else if (char == "2") {
		currentCamera = "2";
		controls.object = cameraTop;
		resetCameraPosition();
		render();
	} else if (char == "3") {
		currentCamera = "3";
		controls.object = cameraFront;
		resetCameraPosition();
		render();
	} else if (char == "f") {
		if (isTHREExFullScreen) {
			THREEx.FullScreen.cancel();
		} else {
			THREEx.FullScreen.request(container);
		}
		render();
	} else if (char == "l") {
		toggleLabels();
	}
}

function toggleLabels() {
	console.log("toggleLabels");
	var searchResult = [];
	findObjectArray(searchResult, scene, "label");
	for (var i = 0; i < searchResult.length; i++) {
		searchResult[i].visible = !searchResult[i].visible;
	}
	render();
}

function findObjectArray(result, startNode, pattern) {
	// console.log(startNode.id+": findObjectArray: " + startNode.name);
	if (startNode.name.match(pattern)) {
		result.push(startNode);
	}
	for (var i = 0; i < startNode.children.length; i++) {
		findObjectArray(result, startNode.children[i], pattern);
	}
}

function getChar(event) {
	if (event.which == null) {
		return String.fromCharCode(event.keyCode) // IE
	} else if (event.which != 0 && event.charCode != 0) {
		return String.fromCharCode(event.which) // the rest
	} else {
		return null // special key
	}
}

function WebSocketMessage(messageClass) {
	this.messageClass = messageClass;
};

function makeTextSprite(message, parameters) {
	if (parameters === undefined)
		parameters = {};

	var fontface = parameters.hasOwnProperty("fontface") ? parameters["fontface"]
			: "Arial";

	var fontsize = parameters.hasOwnProperty("fontsize") ? parameters["fontsize"]
			: 18;
	var fontColor = parameters.hasOwnProperty("fontColor") ? parameters["fontColor"]
			: {
				r : 0,
				g : 0,
				b : 0,
				a : 1.0
			};

	var borderThickness = parameters.hasOwnProperty("borderThickness") ? parameters["borderThickness"]
			: 4;

	var borderColor = parameters.hasOwnProperty("borderColor") ? parameters["borderColor"]
			: {
				r : 0,
				g : 0,
				b : 0,
				a : 1.0
			};

	var backgroundColor = parameters.hasOwnProperty("backgroundColor") ? parameters["backgroundColor"]
			: {
				r : 255,
				g : 255,
				b : 255,
				a : 1.0
			};

	// var spriteAlignment = THREE.SpriteAlignment.topLeft;

	var canvas = document.createElement('canvas');
	var context = canvas.getContext('2d');
	context.font = "Bold " + fontsize + "px " + fontface;

	// get size data (height depends only on font size)
	var metrics = context.measureText(message);
	var textWidth = metrics.width;

	// background color
	context.fillStyle = "rgba(" + backgroundColor.r + "," + backgroundColor.g
			+ "," + backgroundColor.b + "," + backgroundColor.a + ")";
	// border color
	context.strokeStyle = "rgba(" + borderColor.r + "," + borderColor.g + ","
			+ borderColor.b + "," + borderColor.a + ")";

	context.lineWidth = borderThickness;
	roundRect(context, borderThickness / 2, borderThickness / 2, textWidth
			+ borderThickness, fontsize * 1.4 + borderThickness, 6);
	// 1.4 is extra height factor for text below baseline: g,j,p,q.

	// text color
	context.fillStyle = "rgba(" + fontColor.r + "," + fontColor.g + ","
			+ fontColor.b + "," + fontColor.a + ")";

	context.fillText(message, borderThickness, fontsize + borderThickness);

	// canvas contents will be used for a texture
	var texture = new THREE.Texture(canvas)
	texture.needsUpdate = true;
	texture.minFilter = THREE.NearestFilter;

	var spriteMaterial = new THREE.SpriteMaterial({
		map : texture,
		useScreenCoordinates : false
	// ,alignment : spriteAlignment
	});
	var sprite = new THREE.Sprite(spriteMaterial);
	sprite.scale.set(100, 50, 1);
	sprite.name = "label(" + message + ")";
	return sprite;
}

// function for drawing rounded rectangles
function roundRect(ctx, x, y, w, h, r) {
	ctx.beginPath();
	ctx.moveTo(x + r, y);
	ctx.lineTo(x + w - r, y);
	ctx.quadraticCurveTo(x + w, y, x + w, y + r);
	ctx.lineTo(x + w, y + h - r);
	ctx.quadraticCurveTo(x + w, y + h, x + w - r, y + h);
	ctx.lineTo(x + r, y + h);
	ctx.quadraticCurveTo(x, y + h, x, y + h - r);
	ctx.lineTo(x, y + r);
	ctx.quadraticCurveTo(x, y, x + r, y);
	ctx.closePath();
	ctx.fill();
	ctx.stroke();
}

animate();
