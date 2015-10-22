package com.akibot.engine2.component.workflow;

import java.util.ArrayList;

import com.akibot.engine2.exception.WorkflowException;

public class WorkflowJoinElement extends WorkflowDefaultElement {
	private static final long serialVersionUID = 1L;
	private ArrayList<WorkflowElement> joinList = new ArrayList<WorkflowElement>();
	private int readyCount = 0;

	@Override
	public void executeElement() throws Exception {
		readyCount++;
		System.out.println("WorkflowJoinElement.executeElement: " + readyCount);
	}

	@Override
	public void setNext(WorkflowElement nextWorkflowElement) throws WorkflowException {
		this.nextWorkflowElement = nextWorkflowElement;
	}

	public ArrayList<WorkflowElement> getJoinList() {
		return joinList;
	}

	public boolean isReady() {
		return readyCount == joinList.size();
	}
}
