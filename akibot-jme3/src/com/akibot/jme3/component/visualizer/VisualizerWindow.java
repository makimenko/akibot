package com.akibot.jme3.component.visualizer;

import graphics.mesh.MyGridMesh;
import graphics.storage.MaterialStorage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import com.akibot.engine2.logger.AkiLogger;
import com.akibot.jme3.component.message.NodeRegistrationRequest;
import com.akibot.jme3.component.message.NodeTransformationRequest;
import com.akibot.jme3.component.message.VisualizerRequest;
import com.akibot.jme3.component.visualizer.utils.AkiGeometry;
import com.akibot.jme3.component.visualizer.utils.AkiNode;
import com.akibot.jme3.component.visualizer.utils.AkiNodeTransformation;
import com.akibot.jme3.component.visualizer.utils.AkiPoint;
import com.akibot.jme3.component.visualizer.utils.VisualUtils;
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
import com.jme3.scene.shape.Box;

public class VisualizerWindow extends SimpleApplication {
	static final AkiLogger log = AkiLogger.create(VisualizerWindow.class);
	private Node baseNode;
	private MaterialStorage materialStorage;
	private VisualUtils visualUtils;
	private Queue<VisualizerRequest> queue = new LinkedList<VisualizerRequest>();
	private HashMap<String, Node> nodeList = new HashMap<String, Node>();

	public VisualizerWindow() {
		// TODO Auto-generated constructor stub
		visualUtils = new VisualUtils();
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

	public void addNode(Node parentNode, Node node) {
		attachCoordinateAxes(node);
		parentNode.attachChild(node);
	}

	public void nodeTransformation(Node node, AkiNodeTransformation akiNodeTransformation) {

		if (node != null && akiNodeTransformation != null && akiNodeTransformation.isChanged()) {

			if (akiNodeTransformation.isRotationChanged()) {
				// TODO: validate
				node.rotateUpTo(visualUtils.pointToVector3f(akiNodeTransformation.getRotation()));
			}

			if (akiNodeTransformation.isScaleChanged()) {
				// TODO: validate
				node.setLocalScale(visualUtils.pointToVector3f(akiNodeTransformation.getScale()));
			}

			if (akiNodeTransformation.isTranslationChanged()) {
				// TODO: validate
				node.setLocalTranslation(visualUtils.pointToVector3f(akiNodeTransformation.getTranslation()));
			}
		}
	}

	public void nodeGeometry(Node node, AkiNode akiNode) {
		AkiGeometry akiGeometry = akiNode.getGeometry();

		if (akiGeometry != null) {
			AkiPoint dimension = akiNode.getGeometry().getDimension();
			if (dimension != null) {
				Box box = new Box(dimension.getX(), dimension.getY(), dimension.getZ());
				Geometry geometry = new Geometry(akiNode.getName() + " geometry", box);
				// geometry.setLocalTranslation(new Vector3f(1,-1,1));
				String materialName = (akiNode.getGeometry().getMaterialName() != null ? akiNode.getGeometry().getMaterialName() : "red");

				geometry.setMaterial(getMaterialStorage().getMaterial(materialName));
				node.attachChild(geometry);
			}
		}

	}

	public void addQueue(VisualizerRequest visualizerRequest) {
		queue.add(visualizerRequest);

	}

	public void nodeRegistrarion(NodeRegistrationRequest nodeRegistrationRequest) {
		AkiNode akiNode = nodeRegistrationRequest.getAkiNode();
		Node findNode = nodeList.get(akiNode.getName());
		if (findNode == null) {
			Node node = visualUtils.akiNodeToNode(akiNode);
			if (akiNode.getParentNode() == null) {
				addNode(getBaseNode(), node);
			} else {
				Node findParentNode = nodeList.get(akiNode.getParentNode().getName());
				if (findParentNode != null) {
					addNode(findParentNode, node);
				} else {
					log.warn("Node [" + akiNode.getName() + "] can't find parent node [" + akiNode.getParentNode() + "] not found!");
				}
			}
			nodeList.put(akiNode.getName(), node);
			nodeTransformation(node, akiNode.getTransformation());
			nodeGeometry(node, akiNode);
		}
	}

	@Override
	public void update() {

		while (!queue.isEmpty()) {
			VisualizerRequest visualizerRequest = queue.remove();
			if (visualizerRequest instanceof NodeRegistrationRequest) {
				nodeRegistrarion((NodeRegistrationRequest) visualizerRequest);
			} else if (visualizerRequest instanceof NodeTransformationRequest) {
				NodeTransformationRequest nodeTransformationRequest = (NodeTransformationRequest) visualizerRequest;
				AkiNode akiNode = nodeTransformationRequest.getAkiNode();
				AkiNodeTransformation akiNodeTransformation = akiNode.getTransformation();
				Node findNode = nodeList.get(akiNode.getName());
				nodeTransformation(findNode, akiNodeTransformation);
			}
		}
		super.update();
	}
}
