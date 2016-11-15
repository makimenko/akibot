package com.akibot.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.akibot.common.device.Gyroscope;

public class AkiBotApplication {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(args[0]);
		Gyroscope mainGyroscope = (Gyroscope) context.getBean("mainGyroscope");
		System.out.println(mainGyroscope.getGyroscopeValue());
	}
}
