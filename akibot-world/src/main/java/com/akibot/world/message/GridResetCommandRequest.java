package com.akibot.world.message;

public class GridResetCommandRequest extends WorldCommandRequest {
	private static final long serialVersionUID = -4010432339439900920L;
	private String gridNodeName;
	private Integer resetToValue;

	public GridResetCommandRequest() {
	}

	public GridResetCommandRequest(String gridNodeName, Integer resetToValue) {
		this.gridNodeName = gridNodeName;
		this.resetToValue = resetToValue;
	}

	public String getGridNodeName() {
		return gridNodeName;
	}

	public void setGridNodeName(String gridNodeName) {
		this.gridNodeName = gridNodeName;
	}

	public Integer getResetToValue() {
		return resetToValue;
	}

	public void setResetToValue(Integer resetToValue) {
		this.resetToValue = resetToValue;
	}

}
