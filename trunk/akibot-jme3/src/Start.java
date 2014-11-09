import graphics.mesh.MyGridMesh;
import graphics.storage.MaterialStorage;

import java.awt.Color;
import java.util.List;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import com.jme3.util.BufferUtils;

public class Start extends SimpleApplication {

	public static void main(String[] args) {
		Start app = new Start();
		AppSettings set = new AppSettings(true);
		set.setWidth(1350);
		set.setHeight(700);
		app.setSettings(set);
		app.setShowSettings(false);
		app.start();
	}

	@Override
	public void simpleInitApp() {
		flyCam.setEnabled(true);
		flyCam.setMoveSpeed(90);
		drawGrid();
		try {
			Node node = new Node();
			node.scale(0.01f);
			rootNode.attachChild(node);


			Node centerNode = new Node();
			node.attachChild(centerNode);
			Sphere sphereMesh = new Sphere(32, 32, 10);
			centerNode.move(337, -106, 486);
			putShape(centerNode, sphereMesh, ColorRGBA.Blue);
			
			
			attachCoordinateAxes(centerNode, new Vector3f());
			addPlots(node, "calibration120.txt", ColorRGBA.White);
			addPlots(node, "table60.txt", ColorRGBA.Red);

			

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void addPlots(Node node, String fileName, ColorRGBA color) throws Exception {
		ArrayFileParser arrayFileParser = new ArrayFileParser();
		List<Vector3f> plotList = arrayFileParser.loadPlotList(fileName);
		System.out.println("SIZE: " + plotList.size());

		Vector3f[] lineVerticies = new Vector3f[plotList.size()];
		for (int i = 0; i < plotList.size(); i++) {
			lineVerticies[i] = plotList.get(i);
		}

		plotPoints(node, lineVerticies, color);
	}

	private void attachCoordinateAxes(Node node, Vector3f pos) {
		float length = 1000;
		Arrow arrow = new Arrow(new Vector3f(length, 0, 0));
		arrow.setLineWidth(4); // make arrow thicker
		putShape(node, arrow, ColorRGBA.Red).setLocalTranslation(pos);

		arrow = new Arrow(new Vector3f(0, length, 0));
		arrow.setLineWidth(4); // make arrow thicker
		putShape(node, arrow, ColorRGBA.Green).setLocalTranslation(pos);

		arrow = new Arrow(new Vector3f(0, 0, length));
		arrow.setLineWidth(4); // make arrow thicker
		putShape(node, arrow, ColorRGBA.Blue).setLocalTranslation(pos);
	}

	private Geometry putShape(Node node, Mesh shape, ColorRGBA color) {
		Geometry g = new Geometry("coordinate axis", shape);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.getAdditionalRenderState().setWireframe(true);
		mat.setColor("Color", color);
		g.setMaterial(mat);
		node.attachChild(g);
		return g;
	}

	public void plotPoints(Node node, Vector3f[] lineVerticies, ColorRGBA pointColor) {
		Mesh mesh = new Mesh();
		mesh.setMode(Mesh.Mode.Points);

		mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(lineVerticies));

		Geometry geo = new Geometry("line", mesh);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", pointColor);
		geo.setMaterial(mat);

		mesh.updateCounts();
		mesh.updateBound();

		node.attachChild(geo);
	}

	public void drawGrid() {
		Mesh gridMesh = new MyGridMesh(200, 5);
		Geometry lineGeometry = new Geometry("grid", gridMesh);
		lineGeometry.setMaterial(MaterialStorage.getMaterial(assetManager, "green"));
		lineGeometry.setLocalTranslation(0, -5, 0); // move down a little
		rootNode.attachChild(lineGeometry);
	}

}
