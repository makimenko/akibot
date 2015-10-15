package com.akibot.tanktrack.component.world.message;

import com.akibot.engine2.component.configuration.ComponentConfiguration;

public class WorldConfiguration extends ComponentConfiguration {
	private static final long serialVersionUID = 1L;

	private WorldContent worldContent;

	public WorldContent getWorldContent() {
		return worldContent;
	}

	public void setWorldContent(WorldContent worldContent) {
		this.worldContent = worldContent;
	}

}
