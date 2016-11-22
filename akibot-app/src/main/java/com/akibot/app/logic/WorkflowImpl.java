package com.akibot.app.logic;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akibot.common.device.Gyroscope;

@Component
public class WorkflowImpl implements Workflow {

	private static final int GYRO_REQUEST_INTERVAL_MS = 500;
	private ScheduledExecutorService scheduler;

	@Autowired
	private Gyroscope mainGyroscope;

	public WorkflowImpl() {

	}

	@Override
	public void startSample() {
		System.out.println("Hello");
		intiScheduler();
	}

	private void intiScheduler() {
		this.scheduler = Executors.newScheduledThreadPool(1);

		final Runnable gyroRequestTask = new Runnable() {
			@Override
			public void run() {
				System.out.println("gyro: " + mainGyroscope.getGyroscopeValue());
			}
		};

		scheduler.scheduleAtFixedRate(gyroRequestTask, 0, GYRO_REQUEST_INTERVAL_MS, TimeUnit.MILLISECONDS);

	}

}
