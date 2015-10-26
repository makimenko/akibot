package com.akibot.engine2.component.workflow;

import java.util.HashMap;

import com.akibot.engine2.component.DefaultComponent;
import com.akibot.engine2.exception.FailedToSendMessageException;
import com.akibot.engine2.exception.WorkflowException;
import com.akibot.engine2.logger.AkiLogger;
import com.akibot.engine2.message.Message;
import com.akibot.engine2.message.Response;

public class WorkflowComponent extends DefaultComponent {
	private static final AkiLogger log = AkiLogger.create(WorkflowComponent.class);
	private WorkflowRequest currentWorkflowRequest;
	private long currentWorkflowStart;
	private WorkflowResponse workflowResponse;
	private HashMap<String, WorkflowElement> worfklowWaitList = new HashMap<>();

	@Override
	public void loadDefaults() {
		addTopic(new WorkflowRequest());
		addTopic(new Response());
		super.getComponentStatus().setReady(true);
	}

	@Override
	public void onMessageReceived(Message message) throws Exception {
		if (message instanceof WorkflowRequest) {
			onWorflowRequest((WorkflowRequest) message);
		} else if (message instanceof Response) {
			onResponse((Response) message);
		}
	}

	private void onWorflowRequest(WorkflowRequest workflowRequest) throws Exception {
		if (isWorkflowRunning()) {
			log.error(this.getAkibotClient() + ": Workflow is running!");
		} else {
			this.currentWorkflowRequest = workflowRequest;
			validateWorkflow();
			startWorkflow();
		}
	}

	private void validateWorkflow() {
		// TODO: Implement Workflow validation
	}

	private void startWorkflow() throws Exception {
		clear();
		currentWorkflowStart = System.currentTimeMillis();
		WorkflowDefinition workflowDefinition = currentWorkflowRequest.getWorflowDefinition();
		WorkflowElement startWorkflowElement = workflowDefinition.getStartWorkflowElement();
		execute(startWorkflowElement);
	}

	private void onResponse(Response response) throws Exception {
		WorkflowElement waitingWorkflowElement = whoIsWaitingThisResponse(response);
		if (waitingWorkflowElement != null) {
			executeNext(waitingWorkflowElement);
		} else {
			log.warn(this.getAkibotClient() + ": The response is not in a waitlist: " + response);
		}
	}

	private void execute(WorkflowElement workflowElement) throws Exception {
		WorkflowWait workflowWait = workflowElement.executeElement(this);
		if (workflowWait == null) {
			executeNext(workflowElement);
		} else if (workflowWait instanceof WorkflowWaitResponse) {
			addWorkflowWait(workflowWait, workflowElement);
		} else {
			throw new WorkflowException(this.getAkibotClient() + ": Unknown WorkflowWait: " + workflowWait);
		}
	}

	private void executeNext(WorkflowElement workflowElement) throws Exception {
		if (workflowElement instanceof WorkflowForkElement) {
			WorkflowForkElement fork = (WorkflowForkElement) workflowElement;
			for (WorkflowElement nextWorkflowElement : fork.getForkList()) {
				execute(nextWorkflowElement);
			}
		} else {
			WorkflowElement nextWorkflowElement = workflowElement.getNextWorkflowElement();
			if (nextWorkflowElement instanceof WorkflowJoinElement) {
				WorkflowJoinElement join = (WorkflowJoinElement) nextWorkflowElement;
				if (join.elementsLeft() <= 0) {
					execute(nextWorkflowElement);
				}
			} else {
				if (nextWorkflowElement == null) {
					endWorkflow();
				} else {
					execute(nextWorkflowElement);
				}
			}
		}
	}

	private void clear() {
		currentWorkflowStart = 0;
		worfklowWaitList.clear();
		workflowResponse = new WorkflowResponse();
	}

	private boolean isTimeOk() {
		return (System.currentTimeMillis() - currentWorkflowStart) < currentWorkflowRequest.getWorflowDefinition().getTimeoutMilliseconds();
	}

	private boolean isWorkflowRunning() {
		return currentWorkflowRequest != null && isTimeOk();
	}

	private void endWorkflow() throws FailedToSendMessageException {
		broadcastResponse(getWorkflowResponse(), currentWorkflowRequest);
		clear();
		this.currentWorkflowRequest = null;
	}

	public WorkflowResponse getWorkflowResponse() {
		return workflowResponse;
	}

	private void addWorkflowWait(WorkflowWait workflowWait, WorkflowElement workflowElement) {
		log.trace(this.getAkibotClient() + ": addWorkflowWait = " + workflowWait.getCorrelationId());
		worfklowWaitList.put(workflowWait.getCorrelationId(), workflowElement);
	}

	private WorkflowElement whoIsWaitingThisResponse(Response response) {
		String responseCorrelationId = WorkflowWaitResponse.messageCorrelationId(response);
		log.trace(this.getAkibotClient() + ": whoIsWaitingThisResponse = " + responseCorrelationId);
		WorkflowElement workflowElement = worfklowWaitList.get(responseCorrelationId);
		if (workflowElement != null) {
			getWorkflowResponse().getResponseList().put(responseCorrelationId, response);
			worfklowWaitList.remove(responseCorrelationId);
		}
		return workflowElement;
	}

}
