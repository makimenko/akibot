package com.akibot.common.engine.processor.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.akibot.common.engine.processor.Processor;

public class ProcessorImpl implements Processor {

	private ExecutorService executorService;

	public ProcessorImpl(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public Future submit(Callable callable) {
		Future future = executorService.submit(callable);
		return future;
	}

}
