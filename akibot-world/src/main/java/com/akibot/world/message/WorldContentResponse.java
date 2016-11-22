package com.akibot.world.message;

import com.akibot.world.dao.WorldContentDao;

public class WorldContentResponse extends WorldResponse {
	private static final long serialVersionUID = 1915551651191389749L;

	private WorldContentDao worldContent;

	public WorldContentDao getWorldContent() {
		return worldContent;
	}

	public void setWorldContent(WorldContentDao worldContent) {
		this.worldContent = worldContent;
	}

}
