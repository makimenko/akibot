package com.akibot.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.akibot.common.device.Gyroscope;

public class AkiBotApplication {

	public static void main(String[] args) throws InterruptedException {
		ApplicationContext context = new ClassPathXmlApplicationContext(args[0]);

		for (int i = 0; i <= 1; i++) {
			Gyroscope mainGyroscope = (Gyroscope) context.getBean("mainGyroscope");
			System.out.println(mainGyroscope.getGyroscopeValue());
			Thread.sleep(500);
		}

	}
}
