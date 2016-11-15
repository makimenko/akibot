package com.akibot.common;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.akibot.common.utils.ArrayUtils;

public class ArrayTest {

	private final ArrayUtils arrayUtils = new ArrayUtils();

	@Test
	public void updateValue() {
		int[][] arr = new int[2][2];
		int value;

		value = -1;
		arrayUtils.updateValue(arr, value);
		assertEquals(value, arr[0][0]);
		assertEquals(value, arr[1][0]);
		assertEquals(value, arr[0][1]);
		assertEquals(value, arr[1][1]);

		value = 700;
		arrayUtils.updateValue(arr, value);
		assertEquals(value, arr[0][0]);
		assertEquals(value, arr[1][0]);
		assertEquals(value, arr[0][1]);
		assertEquals(value, arr[1][1]);
	}

	@Test
	public void countIf() {
		int[][] arr = new int[2][2];
		int value;

		value = -1;
		arrayUtils.updateValue(arr, value);
		assertEquals(4, arrayUtils.countIf(arr, value));

		arr[0][1] = 7;
		assertEquals(3, arrayUtils.countIf(arr, value));
		assertEquals(1, arrayUtils.countIf(arr, 7));

	}

}
