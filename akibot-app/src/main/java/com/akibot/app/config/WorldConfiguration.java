package com.akibot.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.akibot.app.workflow.WorldSynchronizer;
import com.akibot.app.workflow.WorldSynchronizerImpl;

@Configuration
@Import({ WorkflowConfiguration.class, ContentConfiguration.class })
public class WorldConfiguration {

	@Bean
	public WorldSynchronizer worldSynchronizer() {
		return new WorldSynchronizerImpl();
	}

}
