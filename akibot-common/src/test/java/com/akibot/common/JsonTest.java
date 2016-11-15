package com.akibot.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.akibot.common.element.Point3D;
import com.akibot.common.message.TestMessage;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTest {

	@Test
	public void testJsonToBean() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();

		String jsonInString = "{\"stringField\" : \"hello\", \"doubleField\": \"-1.001\", \"booleanField\": \"true\", \"point\" : {\"x\":\"1\", \"y\":\"2\", \"z\":3} }";

		TestMessage testMessage = mapper.readValue(jsonInString, TestMessage.class);
		assertEquals("hello", testMessage.getStringField());
		assertEquals(-1.001, testMessage.getDoubleField(), 0.0000001);
		assertEquals(1, testMessage.getPoint().getX(), 0.0000001);
		assertEquals(2, testMessage.getPoint().getY(), 0.0000001);
		assertEquals(3, testMessage.getPoint().getZ(), 0.0000001);
		assertTrue(testMessage.isBooleanField());
	}

	@Test
	public void testBeanToJson() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();

		TestMessage testMessage = new TestMessage();
		testMessage.setBooleanField(true);
		testMessage.setStringField("aaa");
		testMessage.setDoubleField(0.03);
		testMessage.setPoint(new Point3D(-1.01, -2.02, -3.03));

		String jsonString = mapper.writeValueAsString(testMessage);

		assertEquals("{\"stringField\":\"aaa\",\"doubleField\":0.03,\"booleanField\":true,\"point\":{\"x\":-1.01,\"y\":-2.02,\"z\":-3.03}}",
				jsonString);
	}

	@Test
	public void testJsonToPartlyBean() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();

		String jsonInString = "{\"stringField\" : \"hello\", \"doubleField\": \"-1.001\", \"booleanField\": \"true\", \"point\" : {\"x\":\"1\", \"y\":\"2\", \"z\":3} }";

		JsonNode root = mapper.readTree(jsonInString);
		JsonNode stringFieldNode = root.get("stringField");

		assertEquals("hello", stringFieldNode.textValue());

	}

}
