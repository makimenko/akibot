package com.akibot.kiss.component;

import com.akibot.kiss.message.CommandMessage;

public interface Component {

	public void setComponentController(ComponentController componentController);
	public void executeCommand(CommandMessage command) throws Exception;
	
}
