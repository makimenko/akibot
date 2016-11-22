package com.akibot.world.message;

public class WorldCommandResponse extends WorldResponse {
	private static final long serialVersionUID = -932073097693007852L;
	private CommandResult commandResult;

	public CommandResult getCommandResult() {
		return commandResult;
	}

	public void setCommandResult(CommandResult commandResult) {
		this.commandResult = commandResult;
	}

	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append(super.toString()).append(": state=").append(getCommandResult());
		return buf.toString();
	}

}
