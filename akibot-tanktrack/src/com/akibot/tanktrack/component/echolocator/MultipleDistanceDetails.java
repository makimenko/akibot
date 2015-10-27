package com.akibot.tanktrack.component.echolocator;

import java.io.Serializable;
import java.util.ArrayList;

import com.akibot.tanktrack.component.distance.DistanceDetails;

public class MultipleDistanceDetails implements Serializable {
	private static final long serialVersionUID = 1L;

	private ArrayList<DistanceDetails> distanceDetailsList = new ArrayList<>();

	public ArrayList<DistanceDetails> getDistanceDetailsList() {
		return distanceDetailsList;
	}

	public void setDistanceDetailsList(ArrayList<DistanceDetails> distanceDetailsList) {
		this.distanceDetailsList = distanceDetailsList;
	}

}
