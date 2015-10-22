package com.akibot.engine2.component.workflow;

import java.util.ArrayList;

import com.akibot.engine2.exception.WorkflowException;

public class WorkflowForkElement implements WorkflowElement {
	private static final long serialVersionUID = 1L;
	private ArrayList<WorkflowElement> forkList = new ArrayList<WorkflowElement>();

	@Override
	public void setNext(WorkflowElement nextWorkflowElement) throws WorkflowException {
		forkList.add(nextWorkflowElement);
	}

	@Override
	public void executeElement() throws Exception {
		System.out.println("WorkflowForkElement.executeElement");
	}

	@Override
	public void executeNext() throws Exception {
		System.out.println("WorkflowForkElement.executeNext");
		// TODO execute in parallel
		for (WorkflowElement workflowElement : forkList) {
			workflowElement.executeElement();
			workflowElement.executeNext();
		}

	}

}
