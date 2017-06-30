package com.akibot.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.akibot.app.workflow.PeriodicGyroscopeRefreshWorkflowImpl;
import com.akibot.app.workflow.Workflow;

@Configuration
public class WorkflowConfiguration {

	@Bean
	public Workflow periodicGyroscopeRefreshWorkflow() {
		return new PeriodicGyroscopeRefreshWorkflowImpl();
	}

}
