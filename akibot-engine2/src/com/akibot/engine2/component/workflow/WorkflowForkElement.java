package com.akibot.engine2.component.workflow;

import java.util.ArrayList;

import com.akibot.engine2.exception.WorkflowException;

public class WorkflowForkElement extends WorkflowDefaultElement {
	private static final long serialVersionUID = 1L;
	private ArrayList<WorkflowElement> forkList = new ArrayList<>();

	@Override
	public WorkflowWait executeElement(WorkflowComponent workflowComponent) throws Exception {
		return null;
	}

	@Override
	public void setNextWorkflowElement(WorkflowElement nextWorkflowElement) throws WorkflowException {
		forkList.add(nextWorkflowElement);
	}

	@Override
	public WorkflowElement getNextWorkflowElement() throws WorkflowException {
		throw new WorkflowException("WorkflowForkElement does not support getNextWorkflowElement() method");
	}

	public ArrayList<WorkflowElement> getForkList() {
		return forkList;
	}

}
