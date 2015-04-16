package com.akibot.jme3.component.visualizer;

import graphics.mesh.MyGridMesh;
import graphics.storage.MaterialStorage;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Sphere;

public class VisualizerWindow extends SimpleApplication {
	private Node baseNode;
	private Node coordinatesCenterNode;

	public VisualizerWindow() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void simpleInitApp() {
		flyCam.setEnabled(true);
		flyCam.setMoveSpeed(90);
		drawGrid();
		try {
			baseNode = new Node();
			baseNode.scale(0.01f);
			rootNode.attachChild(baseNode);

			coordinatesCenterNode = new Node();
			baseNode.attachChild(coordinatesCenterNode);
			Sphere sphereMesh = new Sphere(32, 32, 10);
			coordinatesCenterNode.move(337, -106, 486);
			putShape(coordinatesCenterNode, sphereMesh, ColorRGBA.Blue, true);

			attachCoordinateAxes(coordinatesCenterNode, new Vector3f());

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void attachCoordinateAxes(Node node, Vector3f pos) {
		float length = 1000;
		Arrow arrow = new Arrow(new Vector3f(length, 0, 0));
		arrow.setLineWidth(2); // make arrow thicker
		putShape(node, arrow, ColorRGBA.Red, true).setLocalTranslation(pos);

		arrow = new Arrow(new Vector3f(0, length, 0));
		arrow.setLineWidth(2); // make arrow thicker
		putShape(node, arrow, ColorRGBA.Green, true).setLocalTranslation(pos);

		arrow = new Arrow(new Vector3f(0, 0, length));
		arrow.setLineWidth(2); // make arrow thicker
		putShape(node, arrow, ColorRGBA.Blue, true).setLocalTranslation(pos);
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
		lineGeometry.setMaterial(MaterialStorage.getMaterial(assetManager, "green"));
		lineGeometry.setLocalTranslation(0, -5, 0); // move down a little
		rootNode.attachChild(lineGeometry);
	}

	public Node getBaseNode() {
		return baseNode;
	}

	public Node getCoordinatesCenterNode() {
		return coordinatesCenterNode;
	}

}
