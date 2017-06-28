var AkiUtils = {
	GRID_HELPER_NAME : "gridHelper",
	AXIS_HELPER_NAME : "axisHelper",

	cleanupChildRecursion : function(startNode) {
		if (startNode != null && startNode.children != null) {
			for (i = 0; i < startNode.children.length; i++) {
				node = startNode.children[i];
				cleanupChildRecursion(node);
				startNode.remove(node);
			}
		}
	},

	findObjectArray : function(result, startNode, pattern) {
		if (startNode != null) {
			if (startNode.name.match(pattern)) {
				result.push(startNode);
			}
			for (var i = 0; i < startNode.children.length; i++) {
				AkiUtils.findObjectArray(result, startNode.children[i], pattern);
			}
		}
	},

	appendGridHelper : function(parentNode, size, divisions, visible) {
		gridHelper = new THREE.Object3D();// create an empty container
		gridHelper.name = AkiUtils.GRID_HELPER_NAME;
		gridHelper.visible = visible;

		var gridHelper1 = new THREE.GridHelper(size, divisions);
		gridHelper.add(gridHelper1);

		var gridHelper2 = new THREE.GridHelper(size, divisions);
		gridHelper2.rotation.x = Math.PI / 2;
		gridHelper.add(gridHelper2);

		var gridHelper3 = new THREE.GridHelper(size, divisions);
		gridHelper3.rotation.x = Math.PI / 2;
		gridHelper3.rotation.z = Math.PI / 2;
		gridHelper.add(gridHelper3);

		parentNode.add(gridHelper);
	},

	toggleVisibility : function(startNode, namePattern) {
		var result = [];
		AkiUtils.findObjectArray(result, startNode, namePattern);
		for (i = 0; i < result.length; i++) {
			result[i].visible = !result[i].visible;
		}
	},

	setVisibility : function(startNode, namePattern, visible) {
		var result = [];
		AkiUtils.findObjectArray(result, startNode, namePattern);
		for (i = 0; i < result.length; i++) {
			result[i].visible = visible;
		}
	},

	appendAxisHelper : function(parentNode, size, visible) {
		axisHelper = new THREE.AxisHelper(size);
		axisHelper.name = AkiUtils.AXIS_HELPER_NAME;
		axisHelper.renderOrder = 999;
		axisHelper.visible = visible;
		parentNode.add(axisHelper);
	},

	getChar : function(event) {
		if (event.which == null) {
			return String.fromCharCode(event.keyCode) // IE
		} else if (event.which != 0 && event.charCode != 0) {
			return String.fromCharCode(event.which) // the rest
		} else {
			return null // special key
		}
	},

	roundRect : function(ctx, x, y, w, h, r) {
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
	},

	makeTextSprite : function(message, parameters) {
		if (parameters === undefined) {
			parameters = {};
		}
		var fontface = parameters.hasOwnProperty("fontface") ? parameters["fontface"] : "Arial";
		var fontsize = parameters.hasOwnProperty("fontsize") ? parameters["fontsize"] : 18;
		var fontColor = parameters.hasOwnProperty("fontColor") ? parameters["fontColor"] : {
			r : 0,
			g : 0,
			b : 0,
			a : 1.0
		};
		var borderThickness = parameters.hasOwnProperty("borderThickness") ? parameters["borderThickness"] : 4;
		var borderColor = parameters.hasOwnProperty("borderColor") ? parameters["borderColor"] : {
			r : 0,
			g : 0,
			b : 0,
			a : 1.0
		};
		var backgroundColor = parameters.hasOwnProperty("backgroundColor") ? parameters["backgroundColor"] : {
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
		context.fillStyle = "rgba(" + backgroundColor.r + "," + backgroundColor.g + "," + backgroundColor.b + "," + backgroundColor.a + ")";
		// border color
		context.strokeStyle = "rgba(" + borderColor.r + "," + borderColor.g + "," + borderColor.b + "," + borderColor.a + ")";

		context.lineWidth = borderThickness;
		this.roundRect(context, borderThickness / 2, borderThickness / 2, textWidth + borderThickness, fontsize * 1.4 + borderThickness, 6);
		// 1.4 is extra height factor for text below baseline: g,j,p,q.

		// text color
		context.fillStyle = "rgba(" + fontColor.r + "," + fontColor.g + "," + fontColor.b + "," + fontColor.a + ")";

		context.fillText(message, borderThickness, fontsize + borderThickness);

		// canvas contents will be used for a texture
		var texture = new THREE.Texture(canvas)
		texture.needsUpdate = true;
		texture.minFilter = THREE.NearestFilter;

		var spriteMaterial = new THREE.SpriteMaterial({
			map : texture
			// ,useScreenCoordinates : false
		// ,alignment : spriteAlignment
		});
		var sprite = new THREE.Sprite(spriteMaterial);
		sprite.scale.set(100, 50, 1);
		sprite.name = "label(" + message + ")";
		return sprite;
	},

	drawLabel : function(node, text) {
		var spritey = this.makeTextSprite(text, {
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
		spritey.visible = this.labelInitialVisibility;
		node.add(spritey);
	},

	printGrid : function(gridArray) {
		console.log("printGrid:");
		for (var x = 0; x < gridArray.length; x++) {
			for (var y = 0; y < gridArray[x].length; y++) {
				console.log("  (" + x + "," + y + ")=" + gridArray[x][y]);
			}
		}
	}

}
