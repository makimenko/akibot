package com.akibot.common.message;

import java.io.Serializable;

import com.akibot.common.element.Point3D;

public class TestMessage implements Serializable {
	private static final long serialVersionUID = -1261574252042644951L;

	private String stringField;
	private double doubleField;
	private boolean booleanField;
	private Point3D point;

	public String getStringField() {
		return stringField;
	}

	public void setStringField(String stringField) {
		this.stringField = stringField;
	}

	public double getDoubleField() {
		return doubleField;
	}

	public void setDoubleField(double doubleField) {
		this.doubleField = doubleField;
	}

	public Point3D getPoint() {
		return point;
	}

	public void setPoint(Point3D point) {
		this.point = point;
	}

	public boolean isBooleanField() {
		return booleanField;
	}

	public void setBooleanField(boolean booleanField) {
		this.booleanField = booleanField;
	}

}
