package com.akibot.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.akibot.common.element.Point3D;
import com.akibot.common.message.TestMessage;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class JsonTest {

	@Test
	public void testJsonToBean() throws JsonParseException, IOException {
		Gson gson = new Gson();

		String jsonInString = "{\"stringField\" : \"hello\", \"doubleField\": \"-1.001\", \"booleanField\": \"true\", \"point\" : {\"x\":\"1\", \"y\":\"2\", \"z\":3} }";

		TestMessage testMessage = gson.fromJson(jsonInString, TestMessage.class);
		assertEquals("hello", testMessage.getStringField());
		assertEquals(-1.001, testMessage.getDoubleField(), 0.0000001);
		assertEquals(1, testMessage.getPoint().getX(), 0.0000001);
		assertEquals(2, testMessage.getPoint().getY(), 0.0000001);
		assertEquals(3, testMessage.getPoint().getZ(), 0.0000001);
		assertTrue(testMessage.isBooleanField());
	}

	@Test
	public void testBeanToJson() throws JsonParseException, IOException {
		Gson gson = new Gson();

		TestMessage testMessage = new TestMessage();
		testMessage.setBooleanField(true);
		testMessage.setStringField("aaa");
		testMessage.setDoubleField(0.03);
		testMessage.setPoint(new Point3D(-1.01, -2.02, -3.03));

		String jsonString = gson.toJson(testMessage);
		assertEquals(
				"{\"stringField\":\"aaa\",\"doubleField\":0.03,\"booleanField\":true,\"point\":{\"z\":-3.03,\"x\":-1.01,\"y\":-2.02}}",
				jsonString);
	}

}
