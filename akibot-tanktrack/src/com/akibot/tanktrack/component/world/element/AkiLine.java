package com.akibot.tanktrack.component.world.element;

import java.io.Serializable;

public class AkiLine implements Serializable {
	private static final long serialVersionUID = 1L;
	private AkiPoint from;
	private AkiPoint to;

	public AkiLine() {

	}

	public AkiLine(AkiPoint from, AkiPoint to) {
		this.from = from;
		this.to = to;
	}

	public AkiPoint getFrom() {
		return from;
	}

	public void setFrom(AkiPoint from) {
		this.from = from;
	}

	public AkiPoint getTo() {
		return to;
	}

	public void setTo(AkiPoint to) {
		this.to = to;
	}

}
