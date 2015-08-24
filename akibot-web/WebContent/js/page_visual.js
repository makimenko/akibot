$.getScript('../js/page_visual_classes.js');

if (!Detector.webgl)
	Detector.addGetWebGLMessage();

var container, stats;
var camera, controls, scene, renderer;
var cross;
var ws;

$(document).ready(function() {
	initWs();
	init();
});

function initWs() {
	ws = new WebSocket(wsurl("/../../actions"));
	ws.onopen = onWsOpen;
	ws.onmessage = onWsMessage;
}

function onWsMessage(message) {
	var jsonDataStr = JSON.stringify(message);
	console.log("onMessage: jsonDataStr: " + jsonDataStr);
	var object = JSON.parse(message.data);
	// $("<p>" + jsonDataStr + "</p>").insertBefore($("#incomingLogPanel
	// p:first"));

	if (object.className == "WorldContentResponse") {
		console.log("WorldContentResponse:");
		addNodeRecursion(object.worldNode);
		render();
	} else if (object.className == "NodeTransformationMessage") {
		console.log("NodeTransformationResponse:");
		
		var object = this.scene.getObjectByName("robotMesh", true);
		
	}
	
	
	
}

function addNodeRecursion(node) {
	console.log("addNodeRecursion: " + node.name)
	addNode(node);
	if (node.childs != null) {
		for (i = 0; i < node.childs.length; i++) {
			addNodeRecursion(node.childs[i]);
		}
	}
}

function addNode(node) {
	var matDummy = new THREE.MeshLambertMaterial({
		color : 0x0f0f0f,
		shading : THREE.FlatShading,
		opacity : 0.5,
		transparent : true
	});

	var geometry = new THREE.BoxGeometry(node.geometry.dimension.x, node.geometry.dimension.y, node.geometry.dimension.z);
	var mesh = new THREE.Mesh(geometry, matDummy);
	applyTransformation(mesh, node.transformation);

	mesh.name = node.name;
	mesh.updateMatrix();
	scene.add(mesh);
}

function applyTransformation(object3d, transformation) {
	if (transformation != null) {
		// Translation (move):
		if (transformation.translationChanged == true) {
			object3d.translateX(transformation.translation.x);
			object3d.translateY(transformation.translation.y);
			object3d.translateZ(transformation.translation.z);
		}
		// Rotation:
		if (transformation.rotationChanged == true) {
			object3d.rotation.x = transformation.rotation.x;
			object3d.rotation.y = transformation.rotation.y;
			object3d.rotation.z = transformation.rotation.z;
		}
		// Scale:
		if (transformation.scaleChanged == true) {
			object3d.scale.set(transformation.scale.x, transformation.scale.y, transformation.scale.z);
		}
	}
}


function drawScene() {
	drawGridHelper();
	drawAxisHelper();
	// drawArrowHelper();
	// drawBoundingBoxHelper();
}

function drawGridHelper() {
	// reference: https://www.script-tutorials.com/webgl-with-three-js-lesson-3
	var gridHelper = new THREE.GridHelper(300, 100);
	gridHelper.position = new THREE.Vector3(0, 0, 0);
	gridHelper.rotation = new THREE.Euler(0, 0, 0);
	this.scene.add(gridHelper);

	var gridHelper2 = gridHelper.clone();
	gridHelper2.rotation.set(Math.PI / 2, 0, 0);
	this.scene.add(gridHelper2);

	var gridHelper3 = gridHelper.clone();
	gridHelper3.rotation.set(Math.PI / 2, 0, Math.PI / 2);
	this.scene.add(gridHelper3);
}

function drawAxisHelper() {
	var axisHelper = new THREE.AxisHelper(800); // 500 is size
	this.scene.add(axisHelper);
}

function drawArrowHelper() {
	var directionV3 = new THREE.Vector3(1, 0, 0);
	var originV3 = new THREE.Vector3(0, 0, 0);
	var arrowHelper = new THREE.ArrowHelper(directionV3, originV3, 50, 0xff0000, 10, 5);
	this.scene.getObjectByName("robotMesh", true).add(arrowHelper);
}

function drawBoundingBoxHelper() {
	bboxHelper = new THREE.BoundingBoxHelper(this.scene.getObjectByName("robotMesh", true), 0x999999);
	this.scene.add(bboxHelper);
}

function init() {

	camera = new THREE.PerspectiveCamera(60, window.innerWidth / window.innerHeight, 1, 1000);
	camera.position.y = -200;
	camera.position.z = 200;
	camera.lookAt(new THREE.Vector3(0, 0, 0));

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

	// world
	scene = new THREE.Scene();

	drawScene();

	// lights
	light = new THREE.DirectionalLight(0xffffff);
	light.position.set(1, 1, 1);
	scene.add(light);

	light = new THREE.DirectionalLight(0x002288);
	light.position.set(-1, -1, -1);
	scene.add(light);

	light = new THREE.AmbientLight(0x222222);
	scene.add(light);

	// renderer
	renderer = new THREE.WebGLRenderer({
		antialias : false
	});
	renderer.setClearColor(0xffffff, 1);
	renderer.setSize(window.innerWidth, window.innerHeight);

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
	// onSceneReady();
}

function onWindowResize() {
	width = $('#container').width();
	height = window.innerHeight * 0.85;
	camera.aspect = width / height;
	camera.updateProjectionMatrix();
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
	renderer.render(scene, camera);
	stats.update();
}

function onWsOpen() {
	sendWsRequest(new WebSocketMessage("WorldContentRequest"));
}

function sendWsRequest(requestObject) {
	ws.send(JSON.stringify(requestObject));
}

animate();
