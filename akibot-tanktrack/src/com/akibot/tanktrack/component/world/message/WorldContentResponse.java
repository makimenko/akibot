package com.akibot.tanktrack.component.world.message;

public class WorldContentResponse extends WorldResponse {
	private static final long serialVersionUID = 1L;

	private WorldContent worldContent;

	public WorldContentResponse() {
		this.setClassName(WorldContentResponse.class.getSimpleName());
	}

	public WorldContent getWorldContent() {
		return worldContent;
	}

	public void setWorldContent(WorldContent worldContent) {
		this.worldContent = worldContent;
	}

}
