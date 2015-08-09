package com.akibot.engine2.component;

public class DefaultDNSComponent extends DefaultComponent {

	@Override
	public void loadDefaults() {
		super.loadDefaults();
		getComponentStatus().setReady(true);
	}
}
