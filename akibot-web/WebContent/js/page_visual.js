if (!Detector.webgl)
	Detector.addGetWebGLMessage();

$(document).ready(init);

var container, stats;
var camera, controls, scene, renderer;
var cross;
var ws;
var gridHelper;
var axisHelper;

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
		console.log("WorldContentResponse:");
		addNodeRecursion(messageObject.worldNode);
		render();
	} else if (messageObject.className == "NodeTransformationMessage") {
		console.log("NodeTransformationResponse:");
		var object3d = scene.getObjectByName(messageObject.nodeName, true);
		applyTransformation(object3d, messageObject.transformation);
		render();
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

function getMaterial(material) {
	var mat = new THREE.MeshPhongMaterial(material);
	// mat.color = new THREE.Color(0x00ff00);
	
	return mat;
}

function addNode(node) {
	var geometry = new THREE.BoxGeometry(node.geometry.dimension.x, node.geometry.dimension.y, node.geometry.dimension.z);
	var mesh = new THREE.Mesh(geometry, getMaterial(node.material));
	mesh.name = node.name;
	mesh.castShadow = node.castShadow;
	mesh.receiveShadow = node.receiveShadow;

	applyTransformation(mesh, node.transformation);

	mesh.updateMatrix();
	scene.add(mesh);
}

function applyTransformation(object3d, transformation) {
	// console.log("applyTransformation: "+object3d+" = "+transformation)
	if (transformation != null) {
		// Translation (move):
		if (transformation.position != null) {
			object3d.position.set(transformation.position.x, transformation.position.y, transformation.position.z);
		}
		// Rotation:
		if (transformation.rotation != null) {
			object3d.rotation.x = transformation.rotation.x;
			object3d.rotation.y = transformation.rotation.y;
			object3d.rotation.z = transformation.rotation.z;
		}
		// Scale:
		if (transformation.scale != null) {
			object3d.scale.set(transformation.scale.x, transformation.scale.y, transformation.scale.z);
		}
	}
}

function drawScene() {	
	drawGridHelper();
	drawAxisHelper();
	// drawArrowHelper();
	// drawBoundingBoxHelper();
	
	loadCollada();
}

function loadCollada() {
	var loader = new THREE.ColladaLoader();
	loader.load('../js/loader/AkiBot.dae', function(collada) {
		dae = collada.scene;
		/*
		 * dae.traverse(function(child) { if (child instanceof
		 * THREE.SkinnedMesh) { var animation = new THREE.Animation(child,
		 * child.geometry.animation); animation.play(); } });
		 */
		dae.updateMatrix();
		dae.position.x = -20;
		dae.position.y = -20;
		dae.position.z = 5;	
		
		dae.scale.set(100,100,100);
		
		dae.castShadows=true;
		dae.reflectShadows=true;
		scene.add(dae);
	});
}

function loadJson() {
	var jsonLoader = new THREE.JSONLoader();
	jsonLoader.load('../js/loader/Sandbox_obj_mat.json', addModelToScene2);
}

function addModelToScene2(geometry, materials) {
	console.log('addModelToScene2')
	var material = new THREE.MeshFaceMaterial(materials);
	var jsonModel = new THREE.Mesh(geometry, material);
	// jsonModel.scale.set(1, 1, 1);
	scene.add(jsonModel);
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
	// directionalLight.shadowCameraVisible = true;
	scene.add(directionalLight);
}

function drawGridHelper() {
	// reference: https://www.script-tutorials.com/webgl-with-three-js-lesson-3
	gridHelper = new THREE.Object3D();// create an empty container

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

function initScene() {

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

	scene = new THREE.Scene();
	drawScene();
	drawLights();

	renderer = new THREE.WebGLRenderer({
		antialias : false
	});

	renderer.shadowMapEnabled = true;
	renderer.shadowMapType = THREE.PCFSoftShadowMap;
	renderer.shadowMapSoft = true;

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

function onKeyPress(event) {
	var char = getChar(event);
	console.log('onKeyPress: ' + char);
	if (char == "g") {
		gridHelper.visible = !gridHelper.visible;
		render();
	} else if (char == "a") {
		axisHelper.visible = !axisHelper.visible;
		render();
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

animate();
