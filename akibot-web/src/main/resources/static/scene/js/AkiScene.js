var AkiScene = {
	CONTAINER_DIV_NAME : "threejs_container",

	renderer : null,
	container : null,
	// stats : null,
	camera : null,
	scene : null,
	controls : null,
	sceneContent : null,
	labelInitialVisibility : true,
	isWorldContentRequest : false,
	lazyLoadCount : 0,

	gridDetail : {
		gridGeometry : null,
		gridGroup : null,
		grid : null,
		// TODO: Grid cell color is incorrect
		// http://stackoverflow.com/questions/37588370/three-js-bufferedgeometry-vertexcolors
		obstacleColor : new THREE.Color(200 / 255, 0 / 255, 0 / 255),
		emptyColor : new THREE.Color(0 / 255, 200 / 255, 0 / 255),
		unknownColor : new THREE.Color(250 / 255, 250 / 255, 250 / 255),
		// Number of coordinates: X, Y, Z
		xyzCoordinatesCount : 3,
		// each cell (square) has 2 triangles, each triansgle has 3 coordinates
		cellPointCount : 2 * 3,
		cellSizeMm : null,
		cellObjectTemplate : null,
		cellScale : 0.1,
		material : new THREE.MeshBasicMaterial({
			side : THREE.DoubleSide,
			vertexColors : THREE.VertexColors,
			transparent : true,
			opacity : 0.5,
			shading : THREE.FlatShading
		})
	},

	render : function() {
		AkiScene.renderer.render(AkiScene.scene, AkiScene.camera);
		// AkiScene.stats.update();
	},

	initScene : function() {
		this.scene = new THREE.Scene();

		this.camera = new THREE.PerspectiveCamera(60, window.innerWidth
				/ window.innerHeight, 1, 700);
		this.camera.position.x = -50;
		this.camera.position.y = -200;
		this.camera.position.z = 200;
		this.camera.up = new THREE.Vector3(0, 0, 1);
		this.camera.lookAt(new THREE.Vector3(0, 0, 0));

		this.renderer = new THREE.WebGLRenderer({
			antialias : true
		});

		this.renderer.shadowMap.enabled = true;
		this.renderer.shadowMap.type = THREE.PCFSoftShadowMap;
		this.renderer.shadowMapSoft = true;
		this.renderer.setClearColor(0xffffff, 1);
		this.renderer.setSize(window.innerWidth, window.innerHeight);
		this.renderer.autoClear = true;

		// this.renderer.gammaInput = true;
		// this.renderer.gammaOutput = true;

		this.container = document.getElementById(this.CONTAINER_DIV_NAME);
		this.container.appendChild(this.renderer.domElement);

		// this.stats = new Stats();
		// this.stats.domElement.style.position = 'absolute';
		// this.stats.domElement.style.top = '0px';
		// this.stats.domElement.style.zIndex = 100;
		// this.container.appendChild(this.stats.domElement);

		window.addEventListener('resize', AkiEvents.onWindowResize, false);

		this.controls = new THREE.OrbitControls(this.camera);
		this.controls.rotateSpeed = 1.0;
		this.controls.zoomSpeed = 1.2;
		this.controls.panSpeed = 0.8;
		//this.controls.enableZoom = false;
		// this.controls.enablePan = true;
		// this.controls.noRoll = true; // ?
		// this.controls.noRotate = true; // ?
		// this.controls.staticMoving = true;
		// this.controls.dynamicDampingFactor = 0.3;
		// this.controls.keys = [ 65, 83, 68 ];
		this.controls.addEventListener('change', AkiEvents.onControlChange);

		this.drawScene();
		AkiEvents.onWindowResize();
		animate();
	},

	appendLights : function(parentNode) {

		var light = new THREE.PointLight(0xffffff, 1, 1000);
		light.position.set(0, 0, 100);
		parentNode.add(light);

		var light = new THREE.PointLight(0xffffff, 1, 1000);
		light.position.set(0, 0, -100);
		parentNode.add(light);

		/*
		 * var directionalLight = new THREE.DirectionalLight(0xffffff);
		 * directionalLight.position.set(-500, -500, 1000);
		 * directionalLight.shadow.camera.near = 50;
		 * directionalLight.shadow.camera.far = 5000;
		 * directionalLight.shadow.camera.fov = 50; directionalLight.intensity =
		 * 0.9; directionalLight.castShadow = true;
		 * directionalLight.shadow.mapSize.width = 512; // default is 512
		 * directionalLight.shadow.mapSize.height = 512; // default is 512
		 * parentNode.add(directionalLight);
		 * 
		 * var directionalLight2 = new THREE.DirectionalLight(0xffffff);
		 * directionalLight2.position.set(500, 500, -1000);
		 * directionalLight2.shadow.camera.near = 50;
		 * directionalLight2.shadow.camera.far = 5000;
		 * directionalLight2.shadow.camera.fov = 50; directionalLight2.intensity =
		 * 0.1; directionalLight2.castShadow = true;
		 * directionalLight2.shadow.mapSize.width = 512; // default is 512
		 * directionalLight2.shadow.mapSize.height = 512; // default is 512
		 * parentNode.add(directionalLight2);
		 */
	},

	drawScene : function() {
		AkiUtils.appendGridHelper(this.scene, 300, 100, false);
		AkiUtils.appendAxisHelper(this.scene, 800, false);
		this.appendLights(this.scene);
	},

	sendWorldContentRequest : function() {
		if (!AkiScene.isWorldContentRequest && AkiScene.lazyLoadCount == 0) {
			AkiScene.isWorldContentRequest = true;
			console.log("sendWorldContentRequest");
			AkiNetwork.sendWsRequest(new AkiNetwork.enrichWebSocketMessage(
					"WorldContentRequest"));
		}
	},

	cleanupChildRecursion : function(startNode) {
		console.log("cleanupChildRecursion:");
		if (startNode != null && startNode.children != null) {
			for (var i = 0; i < startNode.children.length; i++) {
				node = startNode.children[i];
				this.cleanupChildRecursion(node);
				this.scene.remove(node);
			}
		}
	},

	addNodeRecursion : function(node, parentSceneNode) {
		AkiScene.addNode(node, parentSceneNode);
		if (node.childs != null) {
			for (i = 0; i < node.childs.length; i++) {
				AkiScene.addNodeRecursion(node.childs[i], parentSceneNode);
			}
		}
	},

	getMaterial : function(material) {
		var mat = new THREE.MeshPhongMaterial(material);
		return mat;
	},

	addNode : function(node, parentSceneNode) {
		console.log("addNode " + node.name);
		if (node.geometry == null) {
			emptyObject = new THREE.Object3D();
			AkiScene.addNodeFinalisation(emptyObject, node, parentSceneNode);
		} else if (node.geometry.className == "BoxGeometry") {
			var geometry = new THREE.BoxGeometry(node.geometry.dimension.x,
					node.geometry.dimension.y, node.geometry.dimension.z);
			object3d = new THREE.Mesh(geometry,
					getMaterial(node.geometry.material));
			AkiScene.addNodeFinalisation(object3d, node, parentSceneNode);
		} else if (node.geometry.className == "ColladaGeometry") {
			this.lazyLoadCount++;
			var loader = new THREE.ColladaLoader();
			loader.load(node.geometry.fileName, function colladaReady(collada,
					colladaRootObject) {
				object3d = collada.scene;
				AkiScene.addNodeFinalisation(object3d, node, parentSceneNode);
				this.lazyLoadCount--;
			});
		} else if (node.geometry.className == "GridGeometry") {
			AkiScene.addLocationAreaGrid(node, parentSceneNode);
		} else {
			console.log("Unsupported node class");
		}
	},

	addLocationAreaGrid : function(node, parentSceneNode) {
		console.log("addLocationAreaGrid");

		this.gridDetail.grid = node.geometry.grid;

		this.gridDetail.cellSizeMm = node.geometry.gridConfiguration.cellSizeMm;
		cellsCount = this.gridDetail.grid.length
				* this.gridDetail.grid[0].length;

		var gap = 0.0; // distance between cells
		this.gridDetail.gridGeometry = new THREE.BufferGeometry();
		var arraySize = cellsCount * this.gridDetail.xyzCoordinatesCount
				* this.gridDetail.cellPointCount;
		var positions = new Float32Array(arraySize);
		var colors = new Float32Array(arraySize);
		var color;

		var ip = 0;
		var ic = 0;
		var z = 0;
		var r, g, b;

		for (var x = 0; x < this.gridDetail.grid.length; x++) {
			for (var y = 0; y < this.gridDetail.grid[x].length; y++) {
				// Square has 2 triangles:
				// Triangle 1:
				positions[ip++] = x * this.gridDetail.cellSizeMm
						* this.gridDetail.cellScale + gap;
				positions[ip++] = y * this.gridDetail.cellSizeMm
						* this.gridDetail.cellScale + gap;
				positions[ip++] = z;

				positions[ip++] = x * this.gridDetail.cellSizeMm
						* this.gridDetail.cellScale + gap;
				positions[ip++] = y
						* this.gridDetail.cellSizeMm
						* this.gridDetail.cellScale
						+ (this.gridDetail.cellSizeMm * this.gridDetail.cellScale)
						- gap;
				positions[ip++] = z;

				positions[ip++] = x
						* this.gridDetail.cellSizeMm
						* this.gridDetail.cellScale
						+ (this.gridDetail.cellSizeMm * this.gridDetail.cellScale)
						- gap;
				positions[ip++] = y
						* this.gridDetail.cellSizeMm
						* this.gridDetail.cellScale
						+ (this.gridDetail.cellSizeMm * this.gridDetail.cellScale)
						- gap;
				positions[ip++] = z;

				// Triangle 2:
				positions[ip++] = x
						* this.gridDetail.cellSizeMm
						* this.gridDetail.cellScale
						+ (this.gridDetail.cellSizeMm * this.gridDetail.cellScale)
						- gap;
				positions[ip++] = y
						* this.gridDetail.cellSizeMm
						* this.gridDetail.cellScale
						+ (this.gridDetail.cellSizeMm * this.gridDetail.cellScale)
						- gap;
				positions[ip++] = z;

				positions[ip++] = x
						* this.gridDetail.cellSizeMm
						* this.gridDetail.cellScale
						+ (this.gridDetail.cellSizeMm * this.gridDetail.cellScale)
						- gap;
				positions[ip++] = y * this.gridDetail.cellSizeMm
						* this.gridDetail.cellScale + gap;
				positions[ip++] = z;

				positions[ip++] = x * this.gridDetail.cellSizeMm
						* this.gridDetail.cellScale + gap;
				positions[ip++] = y * this.gridDetail.cellSizeMm
						* this.gridDetail.cellScale + gap;
				positions[ip++] = z;

				// Set color
				if (this.gridDetail.grid[x][y] > 0) {
					color = this.gridDetail.obstacleColor;
				} else if (this.gridDetail.grid[x][y] == 0) {
					color = this.gridDetail.emptyColor;
				} else {
					color = this.gridDetail.unknownColor;
				}

				for (var d = 0; d < this.gridDetail.cellPointCount; d++) {
					colors[ic++] = color.r;
					colors[ic++] = color.g;
					colors[ic++] = color.b;
				}
			}
		}

		this.gridDetail.gridGeometry.addAttribute('position',
				new THREE.BufferAttribute(positions, 3));
		this.gridDetail.gridGeometry.addAttribute('color',
				new THREE.BufferAttribute(colors, 3));

		// Creating Mesh and adding to Scene:
		this.gridDetail.gridGroup = new THREE.Mesh(
				this.gridDetail.gridGeometry, this.gridDetail.material);
		this.gridDetail.gridGroup.name = node.name;

		this.gridDetail.gridGeometry.computeBoundingBox();
		this.gridDetail.gridGeometry.computeVertexNormals();

		offsetVector = node.geometry.gridConfiguration.offsetVector;
		transformation = node.transformation;
		if (transformation == null) {
			transformation = {
				position : offsetVector
			}
		} else {
			transformation.position.x += offsetVector.x;
			transformation.position.y += offsetVector.y;
		}
		AkiScene.applyTransformation(this.gridDetail.gridGroup, transformation);

		parentSceneNode.add(this.gridDetail.gridGroup);
		this.render();
	},

	addNodeFinalisation : function(object3d, node, parentSceneNode) {
		object3d.name = node.name;
		object3d.castShadow = node.castShadow;
		object3d.receiveShadow = node.receiveShadow;

		AkiScene.applyTransformation(object3d, node.transformation);
		object3d.updateMatrix();
		AkiUtils.drawLabel(object3d, node.name);
		if (parentSceneNode == null) {
			console.log("WARNING: add to root scene: " + object3d.name);
			this.scene.add(object3d);
		} else {
			parentSceneNode.add(object3d);
		}
		this.render();
	},

	applyTransformation : function(object3d, transformation) {
		if (transformation == null) {
			console
					.log("Skip empty transformation (for " + object3d.name
							+ ")");
		} else if (object3d == null) {
			console.log("Skip empty object3d");
		} else {
			// Translation (move):
			if (transformation.position != null) {
				object3d.position.set(transformation.position.x
						* this.gridDetail.cellScale, transformation.position.y
						* this.gridDetail.cellScale, transformation.position.z
						* this.gridDetail.cellScale);
			}
			// Rotation:
			if (transformation.rotation != null) {
				object3d.rotation.x = transformation.rotation.x;
				object3d.rotation.y = transformation.rotation.y;
				object3d.rotation.z = transformation.rotation.z;
			}
			// Scale:
			if (transformation.scale != null) {
				object3d.scale.set(transformation.scale.x,
						transformation.scale.y, transformation.scale.z);
			}
		}
	},

	getBufferArrayIndex : function(x, y) {
		yCount = this.gridDetail.grid[0].length;
		arrayValueSize = this.gridDetail.cellPointCount
				* this.gridDetail.xyzCoordinatesCount;
		return (x * yCount + y) * arrayValueSize;
	},

	updateGridCell : function(x, y, newValue) {
		// console.log("updateGridCell(" + gridDetail.gridGroup + ", " + x + ",
		// " + y + ",
		// " + newValue + ")");
		var r, g, b;
		if (this.gridDetail.grid[x][y] != newValue) {
			// change color
			var idx = this.getBufferArrayIndex(x, y);
			var colors = this.gridDetail.gridGeometry.attributes.color.array;
			var color;
			// Set color
			if (newValue > 0) {
				color = this.gridDetail.obstacleColor;
				r = color.r;
				g = color.g;
				b = color.b;
			} else if (newValue == 0) {
				color = this.gridDetail.emptyColor;
				r = color.r;
				g = color.g;
				b = color.b;
			} else {
				color = this.gridDetail.unknownColor;
				r = color.r;
				g = color.g;
				b = color.b;
			}
			for (var d = 0; d < this.gridDetail.cellPointCount; d++) {
				colors[idx++] = r;
				colors[idx++] = g;
				colors[idx++] = b;
			}
			this.gridDetail.grid[x][y] = newValue;
		}
	},

	applyGridGeometryFullTransformation : function(gridGroup, newGrid) {
		for (x = 0; x < this.gridDetail.grid.length; x++) {
			for (y = 0; y < this.gridDetail.grid[0].length; y++) {
				this.updateGridCell(x, y, newGrid[x][y]);
			}
		}
		this.gridDetail.gridGeometry.attributes.color.needsUpdate = true;
	},

	applyGridGeometryPartialTransformation : function(gridGroup, event) {
		for (var i = 0; i < event.changeLog.length - 1; i++) {
			change = event.changeLog[i];
			this.updateGridCell(change.x, change.y, change.value);
		}
		this.gridDetail.gridGeometry.attributes.color.needsUpdate = true;
	},

	reloadWorldContentResponse : function(messageObject) {
		if (this.sceneContent == null) {
			console.log("reloadWorldContentResponse: New scene");
		} else {
			console.log("reloadWorldContentResponse: Reload scene");
			// TODO: is it needed to remove() all childs individually?
			// this.cleanupChildRecursion(this.sceneContent);
			this.scene.remove(this.sceneContent);
		}
		this.sceneContent = new THREE.Object3D();
		this.scene.add(this.sceneContent);
		this.addNodeRecursion(messageObject.worldContent.worldNode,
				this.sceneContent);
		this.render();
		AkiScene.isWorldContentRequest = false;
	}
}
