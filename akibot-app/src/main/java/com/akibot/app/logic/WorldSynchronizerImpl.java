package com.akibot.app.logic;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akibot.app.exception.WorkflowException;
import com.akibot.common.element.Angle;
import com.akibot.common.element.Vector3D;
import com.akibot.common.utils.VectorUtils;
import com.akibot.world.dao.WorldContentDao;
import com.akibot.world.dom.node.Node;
import com.akibot.world.dom.transformation.NodeTransformation3D;
import com.akibot.world.message.WorldNodeTransformationEvent;

@Component
public class WorldSynchronizerImpl implements WorldSynchronizer {
	private Logger logger = LoggerFactory.getLogger(WorldSynchronizerImpl.class);

	private long realtimeNotificationDelay;

	@Autowired
	private WorldContentDao worldContentDao;

	private VectorUtils vectorUtils;
	private Map<String, Long> lastNotification;

	public WorldSynchronizerImpl(long realtimeNotificationDelay) {
		this.vectorUtils = new VectorUtils();
		this.lastNotification = new HashMap<String, Long>();
		this.realtimeNotificationDelay = realtimeNotificationDelay;
	}

	@Override
	public void syncGyroscope(String nodeName, Vector3D gyroscopeValue) throws WorkflowException {
		if (logger.isTraceEnabled()) {
			logger.trace("syncGyroscope({}): {}", nodeName, gyroscopeValue);
		}

		// Extract Transformation update:
		Angle angle = vectorUtils.getNorthAngle(gyroscopeValue);
		logger.trace("Gyroscope: {}", angle);
		NodeTransformation3D gyroTransformation = new NodeTransformation3D();
		gyroTransformation.setRotation(new Vector3D(0, 0, angle.getRadians()));

		// Find nodes:
		Node node = worldContentDao.findNode(nodeName);
		Node masterNode = worldContentDao.findMasterNode(node);

		if (node == null) {
			throw new WorkflowException(String.format("Node [%s] not found!", nodeName));
		}

		if (masterNode == null) {
			throw new WorkflowException(String.format("MasterNode of [%s] not found!", nodeName));
		}

		// Apply Transformation:
		// Calculate relative transformation (gyro vs robot):
		NodeTransformation3D nodeTransformation3D = node.getTransformation();
		if (nodeTransformation3D != null && nodeTransformation3D.getRotation() != null) {
			Vector3D nodeRotation = node.getTransformation().getRotation();
			// TODO: verify: add or subtract
			Vector3D gyroRotation = gyroTransformation.getRotation();
			gyroRotation.subtract(nodeRotation);
		}

		if (masterNode != null) {
			worldContentDao.applyTransformation(masterNode, gyroTransformation);

			// Notify others:
			String masterNodeName = masterNode.getName();
			if (System.currentTimeMillis() - getLastNotification(masterNodeName) > realtimeNotificationDelay) {
				logger.trace("Notifying others");
				WorldNodeTransformationEvent event = new WorldNodeTransformationEvent();
				event.setNodeName(masterNodeName);
				event.setTransformation(masterNode.getTransformation());
				// TODO: processor.getClient().broadcastMessage(event);
				lastNotification.put(masterNodeName, System.currentTimeMillis());
			} else {
				if (logger.isTraceEnabled()) {
					logger.trace("Skip notification ({}), will do later", masterNodeName);
				}
			}
		} 
	}

	private long getLastNotification(String key) {
		Long value = lastNotification.get(key);
		if (value == null) {
			return 0;
		} else {
			return value.longValue();
		}
	}

}
