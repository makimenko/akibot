var AkiCursor = {
	radius : 10,
	num_of_corners : 7,
	obj_resolution : 10,
	linewidth : 0.2,
	speedInc : 0.15,
	maxCounter : 35,
	colors : [ 0x379392, 0x2E4952, 0x379392, 0x2E4952 ],
		
	cursorGroup : null,
	all_vertices : [],
	objects : [],	
	materials : [],
	counter : 0,	

	startCursor : function(parentNode, point) {
		this.counter = 0;
		if (this.cursorGroup == null) {
			// TODO: add to separate control layer
			this.addCursor(parentNode);
		} else if (!this.cursorGroup.visible) {
			this.cursorGroup.visible = true;
		}
		this.cursorGroup.position.set(point.x, point.y, point.z + 1);
		AkiScene.render();
	},

	stopCursor : function() {
		this.counter = 0;
		this.cursorGroup.visible = false;
	},

	addCursor : function(parentNode) {
		this.cursorGroup = new THREE.Object3D();
		for (var i = 0; i < this.colors.length; i++) {
			var obj = this.createCursorMesh(this.colors[i], 1 + this.linewidth * i, this.all_vertices, i);
			this.objects.push(obj);
			this.cursorGroup.add(obj);
			obj.rotation.y = Math.PI / 180 * 180;
		}
		this.cursorGroup.rotation.x = Math.PI / 180 * 360;
		parentNode.add(this.cursorGroup);
	},

	createCursorMesh : function(clr, r_coof, ver_arr, wave_type) {
		var geometry = new THREE.BufferGeometry();
		var s_r = this.radius / 20 + Math.sin(0) * this.radius / 20;
		var points = this.generateCursorPoints(this.radius, s_r, 5, wave_type);
		var points2 = this.generateCursorPoints(this.radius * (1 - this.linewidth), s_r, 5, wave_type);
		var vertices = this.generateCursorVertices(points, points2);
		ver_arr.push(vertices);
		geometry.addAttribute('position', new THREE.BufferAttribute(vertices, 3));
		var material = new THREE.MeshBasicMaterial({
			color : clr,
			wireframe : false, // (wave_type<=1?true:false),
			transparent : true,
			opacity : 0.5
		});
		this.materials.push(material);
		var mesh = new THREE.Mesh(geometry, this.materials[this.materials.length - 1]);
		mesh.anim_shape = this.num_of_corners;
		mesh.anim = -1;
		mesh.r_coof = r_coof;
		mesh.wave_type = wave_type;
		return mesh;
	},

	generateCursorPoints : function(radius, wave_height, anim_shape, wave_type) {
		var new_poistions = [];

		for (var i = 0; i <= this.obj_resolution; i++) {
			var angle = 1 * Math.PI / this.obj_resolution * i;
			var radius_addon = 0;
			var speed_incrementer = this.counter * this.speedInc;
			var sine_pct = 0.5;

			if (i < sine_pct * this.obj_resolution || i == this.obj_resolution) {
				var smoothing_amount = 0.14;
				var smooth_pct = 1;
				if (i < sine_pct * this.obj_resolution * smoothing_amount)
					smooth_pct = i / (sine_pct * this.obj_resolution * smoothing_amount);
				if (i > sine_pct * this.obj_resolution * (1 - smoothing_amount) && i <= sine_pct * this.obj_resolution)
					smooth_pct = (sine_pct * this.obj_resolution - i) / (sine_pct * this.obj_resolution * smoothing_amount);
				if (i == this.obj_resolution)
					smooth_pct = 0;

				/*
				 * if (wave_type == 1) radius_addon = wave_height * smooth_pct *
				 * Math.cos((angle + speed_incrementer) * anim_shape); if
				 * (wave_type == 0) radius_addon = wave_height * smooth_pct *
				 * Math.sin((angle + speed_incrementer) * anim_shape); if
				 * (wave_type == 2) radius_addon = wave_height * smooth_pct *
				 * Math.cos((angle + Math.PI / 180 * 120 + speed_incrementer) *
				 * anim_shape);
				 */
			}

			var x = (radius + radius_addon) * Math.cos((angle + speed_incrementer) + wave_type);
			var y = (radius + radius_addon) * Math.sin((angle + speed_incrementer) + wave_type);
			var z = wave_type;

			new_poistions.push([ x, y, -z ]);
		}

		return new_poistions;
	},

	generateCursorVertices : function(points, points2) {
		var vertexPositions = [];
		var center_point = [ 0, 0, 0 ];

		for (var i = 0; i < points.length - 1; i++) {
			vertexPositions.push(points[i], points2[i], points[i + 1]);
			vertexPositions.push(points2[i], points2[i + 1], points[i + 1]);
		}
		vertexPositions.push(points[points.length - 1], points2[points.length - 1], points[0]);
		vertices = new Float32Array(vertexPositions.length * 3);

		for (var i = 0; i < vertexPositions.length; i++) {
			vertices[i * 3 + 0] = vertexPositions[i][0];
			vertices[i * 3 + 1] = vertexPositions[i][1];
			vertices[i * 3 + 2] = vertexPositions[i][2];
		}

		return vertices;
	},

	updateCursorVertices : function(points, points2, my_arr) {
		var vertexPositions = [];
		for (var i = 0; i < points.length - 1; i++) {
			vertexPositions.push(points[i], points2[i], points[i + 1]);
			vertexPositions.push(points2[i], points2[i + 1], points[i + 1]);
		}
		vertexPositions.push(points[points.length - 1], points2[points.length - 1], points[0]);
		for (var i = 0; i < vertexPositions.length; i++) {
			my_arr[i * 3 + 0] = vertexPositions[i][0];
			my_arr[i * 3 + 1] = vertexPositions[i][1];
			my_arr[i * 3 + 2] = vertexPositions[i][2];
		}
	},

	animateCursor : function() {
		if (this.cursorGroup != null && this.cursorGroup.visible) {
			for (var k = 0; k < this.objects.length; k++) {
				var obj = this.objects[k];
				var rad = this.radius * obj.r_coof;
				var s_r = rad / 15;
				var points = this.generateCursorPoints(rad, s_r, obj.anim_shape, obj.wave_type);
				var points2 = this.generateCursorPoints(rad * (1 - this.linewidth), s_r, obj.anim_shape, obj.wave_type);
				this.updateCursorVertices(points, points2, this.all_vertices[k]);
				obj.geometry.attributes.position.needsUpdate = true;

				var speed_incrementer = this.counter * this.speedInc;
				this.materials[k].opacity = 1 / speed_incrementer;
			}
			this.counter++;
			if (this.counter >= this.maxCounter) {
				this.stopCursor();
			}
		}
	}

}