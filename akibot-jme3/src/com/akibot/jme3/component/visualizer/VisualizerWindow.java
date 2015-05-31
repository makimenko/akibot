package com.akibot.jme3.component.visualizer;

import graphics.mesh.MyGridMesh;
import graphics.storage.MaterialStorage;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Sphere;

public class VisualizerWindow extends SimpleApplication {
	private Node baseNode;
	private MaterialStorage materialStorage;

	public VisualizerWindow() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void simpleInitApp() {
		materialStorage = new MaterialStorage(assetManager);
		flyCam.setEnabled(true);
		flyCam.setMoveSpeed(90);
		drawGrid();
		try {
			baseNode = new Node("baseNode");
			Quaternion roll = new Quaternion();
			roll.fromAngleAxis(FastMath.PI * 3 / 2, new Vector3f(1, 0, 0));
			baseNode.setLocalRotation(roll);
			rootNode.attachChild(baseNode);

			attachCoordinateAxes(baseNode);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void attachCoordinateAxes(Node node) {
		Node coordinatesCenterNode = new Node();
		node.attachChild(coordinatesCenterNode);
		Vector3f pos = new Vector3f();

		Node pivot = new Node("pivot");
		BitmapFont font = assetManager.loadFont("Interface/Fonts/Default.fnt");
		BitmapText text = new BitmapText(font, false);
		text.setSize(0.1f);
		text.setText(node.getName());
		pivot.attachChild(text);
		coordinatesCenterNode.attachChild(pivot);

		float length = 1;
		Arrow arrow = new Arrow(new Vector3f(length, 0, 0));
		arrow.setLineWidth(1); // make arrow thicker
		putShape(coordinatesCenterNode, arrow, ColorRGBA.Red, true).setLocalTranslation(pos);

		arrow = new Arrow(new Vector3f(0, length, 0));
		arrow.setLineWidth(1); // make arrow thicker
		putShape(coordinatesCenterNode, arrow, ColorRGBA.Green, true).setLocalTranslation(pos);

		arrow = new Arrow(new Vector3f(0, 0, length));
		arrow.setLineWidth(1); // make arrow thicker
		putShape(coordinatesCenterNode, arrow, ColorRGBA.Blue, true).setLocalTranslation(pos);
	}

	public Geometry putShape(Node node, Mesh shape, ColorRGBA color, boolean wireframe) {
		Geometry g = new Geometry("coordinate axis", shape);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.getAdditionalRenderState().setWireframe(wireframe);
		mat.setColor("Color", color);
		g.setMaterial(mat);
		node.attachChild(g);
		return g;
	}

	public void drawGrid() {
		Mesh gridMesh = new MyGridMesh(200, 5);
		Geometry lineGeometry = new Geometry("grid", gridMesh);
		lineGeometry.setMaterial(materialStorage.getMaterial("green"));
		lineGeometry.setLocalTranslation(0, -1, 0); // move down a little
		rootNode.attachChild(lineGeometry);
	}

	public Node getBaseNode() {
		return baseNode;
	}

	public MaterialStorage getMaterialStorage() {
		return materialStorage;
	}

}
