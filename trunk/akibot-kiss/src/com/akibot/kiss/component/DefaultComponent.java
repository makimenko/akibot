package com.akibot.kiss.component;

import com.akibot.kiss.message.CommandMessage;

public class DefaultComponent implements Component {

	private ComponentController componentController;
	
	@Override
	public void executeCommand(CommandMessage commandMessage) throws Exception {
	}

	@Override
	public void setComponentController(ComponentController componentController) {
		this.componentController = componentController;
	}
	
}

