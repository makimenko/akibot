package com.akibot.world.dom.geometry;

public class GridGeometryCellValue extends BaseNodeGeometry {
	private static final long serialVersionUID = 1L;

	private int x;
	private int y;
	private int value;

	public GridGeometryCellValue(int x, int y, int value) {
		this.x = x;
		this.y = y;
		this.value = value;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getAddress() {
		final StringBuffer buf = new StringBuffer();
		buf.append(x).append(';').append(y);
		return buf.toString();
	}

}
