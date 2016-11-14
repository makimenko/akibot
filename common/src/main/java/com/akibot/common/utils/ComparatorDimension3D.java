package com.akibot.common.utils;

import java.util.Comparator;

import com.akibot.common.element.Dimension3D;

public class ComparatorDimension3D implements Comparator<Dimension3D> {
	private final double tolerance;

	public ComparatorDimension3D() {
		this.tolerance = 0;
	}

	public ComparatorDimension3D(double tolerance) {
		this.tolerance = tolerance;
	}

	public int compare(Dimension3D a, Dimension3D b) {
		if (Math.abs(a.getX() - b.getX()) <= tolerance && Math.abs(a.getY() - b.getY()) <= tolerance && Math.abs(a.getZ() - b.getZ()) <= tolerance) {
			return 0;
		} else {
			return 1;
		}
	}

}
