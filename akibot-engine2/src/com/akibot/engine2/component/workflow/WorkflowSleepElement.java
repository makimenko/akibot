package com.akibot.engine2.component.workflow;

public class WorkflowSleepElement extends WorkflowDefaultElement {
	private static final long serialVersionUID = 1L;
	private long sleepMilliseconds;

	public WorkflowSleepElement(long sleepMilliseconds) {
		this.sleepMilliseconds = sleepMilliseconds;
	}

	public long getSleepMilliseconds() {
		return sleepMilliseconds;
	}

	public void setSleepMilliseconds(long sleepMilliseconds) {
		this.sleepMilliseconds = sleepMilliseconds;
	}

	@Override
	public void executeElement() throws Exception {
		System.out.println("WorkflowSleepElement.executeElement: " + getSleepMilliseconds());
		Thread.sleep(this.getSleepMilliseconds());
	}

}
