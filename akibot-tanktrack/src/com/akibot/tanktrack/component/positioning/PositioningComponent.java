package com.akibot.tanktrack.component.positioning;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.UnsupportedMessageException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.tanktrack.world.Vector3d;

public class PositioningComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(PositioningComponent.class);

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof PositioningVectorRequest) {
			onPositioningVectorRequest((PositioningVectorRequest) message);
		} else {
			throw new UnsupportedMessageException(message.toString());
		}
	}

	private void onPositioningVectorRequest(PositioningVectorRequest positioningVectorRequest) {
		log.debug(this.getAkibotClient() + ": Positioning request: " + positioningVectorRequest);
		Vector3d vector3d = positioningVectorRequest.getVector3d();

		double x = vector3d.getX();
		double y = vector3d.getY();
		// TODO: Later: Consider Z: double z = vector3d.getZ();
		// TODO: Response
	}

}
