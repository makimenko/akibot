package com.akibot.common.utils;

import java.util.Comparator;

import com.akibot.common.element.Distance;

public class ComparatorDistance implements Comparator<Distance> {
	private final double tolerance;

	public ComparatorDistance() {
		this.tolerance = 0;
	}

	public ComparatorDistance(double tolerance) {
		this.tolerance = tolerance;
	}

	@Override
	public int compare(Distance a, Distance b) {
		if (Math.abs(a.getDistanceMm() - b.getDistanceMm()) <= tolerance) {
			return 0;
		} else {
			return (a.getDistanceMm() > b.getDistanceMm() ? 1 : -1);
		}
	}

}
