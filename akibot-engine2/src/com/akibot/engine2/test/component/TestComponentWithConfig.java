package com.akibot.engine2.test.component;

import java.io.Serializable;

import com.akibot.engine2.component.configuration.GetConfigurationResponse;
import com.akibot.engine2.exception.FailedToConfigureException;

public class TestComponentWithConfig extends TestComponent {

	private TestConfiguration componentConfiguration;

	@Override
	public TestConfiguration getComponentConfiguration() {
		return componentConfiguration;
	}

	public void setComponentConfiguration(TestConfiguration componentConfiguration) {
		this.componentConfiguration = componentConfiguration;
	}

	@Override
	public void loadDefaults() {
		super.getComponentStatus().setReady(false);
	}

	@Override
	public void onGetConfigurationResponse(GetConfigurationResponse getConfigurationResponse) throws FailedToConfigureException {
		super.getComponentStatus().setReady(false);
		Serializable responseValue = getConfigurationResponse.getComponentConfiguration();
		if (responseValue instanceof TestConfiguration) {
			setComponentConfiguration((TestConfiguration) responseValue);
			super.getComponentStatus().setReady(true);
		} else {
			throw new FailedToConfigureException(responseValue.toString());
		}

	}
}
