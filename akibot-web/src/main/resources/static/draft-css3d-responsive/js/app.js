// R73, Responsive CSS3D Transparent
var camera;
var scene, renderer;
var sceneCss, rendererCss;
var controls;

init();
animate();

function init() {
  camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, 1, 1000);
  camera.position.set(200, 200, 200);

  controls = new THREE.TrackballControls(camera);

  scene = new THREE.Scene();
  sceneCss = new THREE.Scene();

  var element = document.createElement('iframe');
  element.className = "element"
	  element.src = "sample.html";
  

  


  var object = new THREE.CSS3DObject(element);
  sceneCss.add(object);

  // Add 2 Cubes (front and back):
  var matCube = new THREE.MeshNormalMaterial();
  matCube.transparent = true;
  matCube.opacity = 0.5;

  cube = new THREE.Mesh(new THREE.CubeGeometry(50, 50, 50), matCube);
  cube.position.z = 50;
  scene.add(cube);

  cube2 = cube.clone();
  cube2.position.z = -50;
  scene.add(cube2);

  // Renderer:
  renderer = new THREE.WebGLRenderer();
  renderer.setPixelRatio(window.devicePixelRatio);
  renderer.setSize(window.innerWidth, window.innerHeight);
  document.body.appendChild(renderer.domElement);

  rendererCss = new THREE.CSS3DRenderer();
  rendererCss.setSize(window.innerWidth, window.innerHeight);
  rendererCss.domElement.style.position = 'absolute';
  rendererCss.domElement.style.top = 0;
  document.body.appendChild(rendererCss.domElement);

}

function animate() {
  requestAnimationFrame(animate);
  controls.update();
  renderer.render(scene, camera);
  rendererCss.render(sceneCss, camera);
}