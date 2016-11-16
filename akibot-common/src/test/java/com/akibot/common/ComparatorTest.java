package com.akibot.common;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.akibot.common.element.Dimension3D;
import com.akibot.common.element.Distance;
import com.akibot.common.utils.ComparatorDimension3D;
import com.akibot.common.utils.ComparatorDistance;

public class ComparatorTest {

	private Distance mm1negative = new Distance(-1);
	private Distance mm0 = new Distance(0);
	private Distance mm1 = new Distance(1);
	private Distance mm2 = new Distance(2);

	private Dimension3D dimension000 = new Dimension3D(0, 0, 0);
	private Dimension3D dimension100 = new Dimension3D(1, 0, 0);
	private Dimension3D dimension010 = new Dimension3D(0, 1, 0);
	private Dimension3D dimension200 = new Dimension3D(2, 0, 0);
	private Dimension3D dimension300 = new Dimension3D(3, 0, 0);

	@Test
	public void exactDistance() {
		ComparatorDistance comparator = new ComparatorDistance();
		assertEquals(0, comparator.compare(mm0, mm0));
		assertEquals(1, comparator.compare(mm1, mm0));
		assertEquals(-1, comparator.compare(mm0, mm1));
	}

	@Test
	public void tolerantDistance() {
		double toleranceMm = 2;
		ComparatorDistance comparator = new ComparatorDistance(toleranceMm);
		assertEquals(0, comparator.compare(mm0, mm1));
		assertEquals(0, comparator.compare(mm0, mm1negative));
		assertEquals(0, comparator.compare(mm1, mm1negative));
		assertEquals(-1, comparator.compare(mm1negative, mm2));
	}

	@Test
	public void exactDimension() {
		ComparatorDimension3D comparator = new ComparatorDimension3D();
		assertEquals(0, comparator.compare(dimension000, dimension000));
		assertEquals(1, comparator.compare(dimension000, dimension100));
		assertEquals(1, comparator.compare(dimension100, dimension200));
		assertEquals(1, comparator.compare(dimension100, dimension010));
	}

	@Test
	public void toletanceDimension() {
		ComparatorDimension3D comparator = new ComparatorDimension3D(1);
		assertEquals(0, comparator.compare(dimension000, dimension000));
		assertEquals(0, comparator.compare(dimension000, dimension100));
		assertEquals(0, comparator.compare(dimension100, dimension200));
		assertEquals(1, comparator.compare(dimension000, dimension200));
		assertEquals(1, comparator.compare(dimension100, dimension300));
	}
}
