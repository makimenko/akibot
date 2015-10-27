package com.akibot.tanktrack.component.scout;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.component.workflow.WorkflowDefinition;
import com.akibot.engine2.component.workflow.WorkflowElement;
import com.akibot.engine2.component.workflow.WorkflowForkElement;
import com.akibot.engine2.component.workflow.WorkflowJoinElement;
import com.akibot.engine2.component.workflow.WorkflowRequest;
import com.akibot.engine2.component.workflow.WorkflowRequestElement;
import com.akibot.engine2.component.workflow.WorkflowResponse;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.exception.WorkflowException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.tanktrack.component.echolocator.EchoLocatorRequest;
import com.akibot.tanktrack.component.echolocator.EchoLocatorResponse;
import com.akibot.tanktrack.component.echolocator.MultipleDistanceDetails;
import com.akibot.tanktrack.component.gyroscope.GyroscopeResponse;
import com.akibot.tanktrack.component.gyroscope.GyroscopeValueRequest;
import com.akibot.tanktrack.component.world.element.NodeTransformation;
import com.akibot.tanktrack.component.world.element.Point;
import com.akibot.tanktrack.component.world.element.VectorUtils;
import com.akibot.tanktrack.component.world.message.WorldMultipleDistanceUpdateRequest;
import com.akibot.tanktrack.component.world.message.WorldNodeTransformationRequest;
import com.akibot.tanktrack.launcher.Constants;

public class ScoutComponent extends DefaultComponent {
	static final AkiLogger log = AkiLogger.create(ScoutComponent.class);
	private WorkflowRequest scoutArroundDistanceWorkflowRequest;
	private int TIMEOUT_DISTANCE_AROUND = 5000;
	private String CORRELATION_A = "ScoutComponent.A";
	private String CORRELATION_B = "ScoutComponent.B";
	private String CORRELATION_C = "ScoutComponent.C";

	@Override
	public void loadDefaults() {
		addTopic(new ScoutRequest());
		addTopic(new WorkflowResponse());
		try {
			init();
			getComponentStatus().setReady(true);
		} catch (Exception e) {
			getComponentStatus().setReady(false);
			log.catching(getAkibotClient(), e);
		}
	}

	private void init() throws WorkflowException {
		scoutArroundDistanceWorkflowRequest = createScoutArroundDistanceWorkflowRequest();

	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof ScoutRequest) {
			onScoutRequest((ScoutRequest) message);
		}
	}

	private void onScoutRequest(ScoutRequest scoutRequest) throws WorkflowException, FailedToSendMessageException {
		if (scoutRequest instanceof ScoutDistanceAroundRequest) {
			onScoutDistanceAroundRequest((ScoutDistanceAroundRequest) scoutRequest);
		}
	}

	private void onScoutDistanceAroundRequest(ScoutDistanceAroundRequest scoutDistanceAroundRequest) throws WorkflowException, FailedToSendMessageException {

		log.debug("onScoutDistanceAroundRequest: " + scoutDistanceAroundRequest);
		// EXECUTE WORKFLOW:
		WorkflowResponse workflowResponse = (WorkflowResponse) sendSyncRequest(scoutArroundDistanceWorkflowRequest, TIMEOUT_DISTANCE_AROUND);

		// UPDATE WORLD:
		EchoLocatorResponse frontEchoLocatorResponse = (EchoLocatorResponse) workflowResponse.getResponseList().get(CORRELATION_A);
		//EchoLocatorResponse backEchoLocatorResponse = (EchoLocatorResponse) workflowResponse.getResponseList().get(CORRELATION_B);
		GyroscopeResponse gyroscopeResponse = (GyroscopeResponse) workflowResponse.getResponseList().get(CORRELATION_C);

		WorldNodeTransformationRequest gyroscopeNodeTransformationRequest = new WorldNodeTransformationRequest();
		gyroscopeNodeTransformationRequest.setNodeName(Constants.COMPONENT_NAME_AKIBOT_GYROSCOPE);
		NodeTransformation gyroTransformation = new NodeTransformation();
		gyroTransformation.setRotation(new Point(0, 0, VectorUtils.gradToRad(gyroscopeResponse.getNorthDegreesXY())));
		gyroscopeNodeTransformationRequest.setTransformation(gyroTransformation);

		broadcastMessage(gyroscopeNodeTransformationRequest);

		MultipleDistanceDetails frontMultipleDistanceDetails = frontEchoLocatorResponse.getMultipleDistanceDetails();
		WorldMultipleDistanceUpdateRequest frontWorldMultipleDistanceUpdateRequest = new WorldMultipleDistanceUpdateRequest();
		frontWorldMultipleDistanceUpdateRequest.setMultipleDistanceDetails(frontMultipleDistanceDetails);
		frontWorldMultipleDistanceUpdateRequest.setDistanceNodeName(Constants.COMPONENT_NAME_AKIBOT_ECHOLOCATOR_FRONT);
		frontWorldMultipleDistanceUpdateRequest.setGridNodeName(Constants.NODE_NAME_GRID);
		broadcastMessage(frontWorldMultipleDistanceUpdateRequest);

		// MultipleDistanceDetails backMultipleDistanceDetails = backEchoLocatorResponse.getMultipleDistanceDetails();
		// WorldMultipleDistanceUpdateRequest backWorldMultipleDistanceUpdateRequest = new WorldMultipleDistanceUpdateRequest();
		// backWorldMultipleDistanceUpdateRequest.setMultipleDistanceDetails(backMultipleDistanceDetails);
		// backWorldMultipleDistanceUpdateRequest.setDistanceNodeName(Constants.COMPONENT_NAME_AKIBOT_ECHOLOCATOR_BACK);
		// backWorldMultipleDistanceUpdateRequest.setGridNodeName(Constants.NODE_NAME_GRID);
		// broadcastMessage(backWorldMultipleDistanceUpdateRequest);

		// SEND RESPONSE:
		ScoutDistanceAroundResponse scoutDistanceAroundResponse = new ScoutDistanceAroundResponse();
		broadcastResponse(scoutDistanceAroundResponse, scoutDistanceAroundRequest);
	}

	private WorkflowRequest createScoutArroundDistanceWorkflowRequest() throws WorkflowException {
		WorkflowRequest worflowRequest = new WorkflowRequest();
		WorkflowDefinition workflowDefinition = new WorkflowDefinition(TIMEOUT_DISTANCE_AROUND);

		EchoLocatorRequest frontEchoLocatorRequest = new EchoLocatorRequest();
		frontEchoLocatorRequest.setTo(Constants.COMPONENT_NAME_AKIBOT_ECHOLOCATOR_FRONT);
		frontEchoLocatorRequest.setServoBaseFrom(4);
		frontEchoLocatorRequest.setServoBaseTo(24);
		frontEchoLocatorRequest.setServoBaseStep(1);
		frontEchoLocatorRequest.setServoHeadNormal(14);	

		//EchoLocatorRequest backEchoLocatorRequest = new EchoLocatorRequest();
		//backEchoLocatorRequest.setTo(Constants.COMPONENT_NAME_AKIBOT_ECHOLOCATOR_BACK);

		GyroscopeValueRequest gyroscopeValueRequest = new GyroscopeValueRequest();
		gyroscopeValueRequest.setTo(Constants.COMPONENT_NAME_AKIBOT_GYROSCOPE);

		WorkflowElement fork = new WorkflowForkElement();
		WorkflowElement request1 = new WorkflowRequestElement(CORRELATION_A, frontEchoLocatorRequest);
		//WorkflowElement request2 = new WorkflowRequestElement(CORRELATION_B, backEchoLocatorRequest);
		WorkflowElement request3 = new WorkflowRequestElement(CORRELATION_C, gyroscopeValueRequest);
		WorkflowElement join = new WorkflowJoinElement();

		fork.setNextWorkflowElement(request1);
		//fork.setNextWorkflowElement(request2);
		fork.setNextWorkflowElement(request3);
		request1.setNextWorkflowElement(join);
		//request2.setNextWorkflowElement(join);
		request3.setNextWorkflowElement(join);

		workflowDefinition.setStartWorkflowElement(fork);
		worflowRequest.setWorflowDefinition(workflowDefinition);

		return worflowRequest;
	}

}
