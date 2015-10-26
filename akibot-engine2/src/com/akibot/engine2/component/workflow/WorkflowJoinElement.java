package com.akibot.engine2.component.workflow;

import java.util.ArrayList;

public class WorkflowJoinElement extends WorkflowDefaultElement {
	private static final long serialVersionUID = 1L;
	private ArrayList<WorkflowElement> joinList = new ArrayList<WorkflowElement>();
	private long joinElements = 0;

	@Override
	public WorkflowWait executeElement(WorkflowComponent workflowComponent) throws Exception {
		return null;
	}

	public ArrayList<WorkflowElement> getJoinList() {
		return joinList;
	}

	public void addJoin(WorkflowElement workflowElement) {
		joinList.add(workflowElement);
		joinElements++;
	}

	public long elementsLeft() {
		return --joinElements;
	}

}
