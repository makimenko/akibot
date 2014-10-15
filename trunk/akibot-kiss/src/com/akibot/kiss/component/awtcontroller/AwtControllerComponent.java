package com.akibot.kiss.component.awtcontroller;

import com.akibot.kiss.component.DefaultComponent;

public class AwtControllerComponent extends DefaultComponent {

	public AwtControllerComponent() {
	}
	
	public void start() {
		AwtControllerAppl appl = new AwtControllerAppl(this.getClient());
		appl.start();
	}

}
