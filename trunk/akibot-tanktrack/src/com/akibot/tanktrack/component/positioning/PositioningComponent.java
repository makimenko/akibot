package com.akibot.tanktrack.component.positioning;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.akibot.engine.component.DefaultComponent;
import com.akibot.engine.message.Message;

public class PositioningComponent extends DefaultComponent {
	static final Logger log = LogManager.getLogger(PositioningComponent.class.getName());

	@Override
	public void processMessage(Message message) throws Exception {
		if (message instanceof PositioningVectorRequest) {
			PositioningVectorRequest request = (PositioningVectorRequest) message;
			log.debug("Positioning request: " + request);
			Vector3d vector3d = request.getVector3d();

			double x = vector3d.getX();
			double y = vector3d.getY();
			// TODO: Later: Consider Z: double z = vector3d.getZ();

		}
	}
}
