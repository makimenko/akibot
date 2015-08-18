if (!Detector.webgl)
	Detector.addGetWebGLMessage();

var container, stats;
var camera, controls, scene, renderer;
var cross;

$(document).ready(function() {
	init();
});

animate();

function drawScene() {
	var material1 = new THREE.MeshLambertMaterial({
		color : 0xffffff,
		shading : THREE.FlatShading
	});

	var material2 = new THREE.MeshLambertMaterial({
		color : 0x0f0f0f,
		shading : THREE.FlatShading
	});

	var material3 = new THREE.MeshLambertMaterial({
		color : 0xff0000,
		shading : THREE.FlatShading
	});

	var worldMesh = new THREE.Mesh(new THREE.BoxGeometry(1000, 1000, 1),
			material2);
	worldMesh.updateMatrix();
	worldMesh.matrixAutoUpdate = false;
	scene.add(worldMesh);

	var carMesh = new THREE.Mesh(new THREE.BoxGeometry(50, 30, 1), material1);
	carMesh.position.x = 70;
	carMesh.position.y = 50;
	carMesh.position.z = 1;
	carMesh.rotation.z = 0.1;
	carMesh.updateMatrix();
	carMesh.matrixAutoUpdate = false;
	worldMesh.add(carMesh);

	var homeMesh = new THREE.Mesh(new THREE.BoxGeometry(100, 100, 1), material1);
	homeMesh.position.x = -50;
	homeMesh.position.y = 0;
	homeMesh.position.z = 1;
	homeMesh.updateMatrix();
	homeMesh.matrixAutoUpdate = false;
	worldMesh.add(homeMesh);

	var robotMesh = new THREE.Mesh(new THREE.BoxGeometry(10, 10, 10), material3);
	robotMesh.position.x = 0;
	robotMesh.position.y = 0;
	robotMesh.position.z = 5;
	robotMesh.updateMatrix();
	robotMesh.matrixAutoUpdate = false;
	homeMesh.add(robotMesh);

}

function init() {

	camera = new THREE.PerspectiveCamera(60, window.innerWidth
			/ window.innerHeight, 1, 1000);
	camera.position.z = 500;

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
