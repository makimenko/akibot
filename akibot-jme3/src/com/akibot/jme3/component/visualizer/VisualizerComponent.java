package com.akibot.jme3.component.visualizer;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.jme3.component.visualizer.utils.VisualUtils;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;

public class VisualizerComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(VisualizerComponent.class);

	private VisualizerWindow visualizerWindow;
	private VisualUtils visualUtils;

	public VisualizerComponent(VisualizerWindow visualizerWindow) {
		this.visualizerWindow = visualizerWindow;
		this.visualUtils = new VisualUtils();
	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof VisualizerRequest) {
			VisualizerRequest request = (VisualizerRequest) message;
			Node node = visualizerWindow.getCoordinatesCenterNode();

			Sphere sphere = new Sphere(10, 10, 10);
			Geometry geometry = visualizerWindow.putShape(node, sphere, ColorRGBA.Red, false);
			geometry.move(visualUtils.pointToVector3f(request.getAkiPoint()));
			node.updateGeometricState();

		}
	}

}
