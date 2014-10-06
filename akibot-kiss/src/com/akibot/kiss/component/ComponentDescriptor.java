package com.akibot.kiss.component;

import com.akibot.kiss.types.ComponentType;

public class ComponentDescriptor {
	private ComponentType componentType;
	private String name;
	
	public ComponentDescriptor(ComponentType componentType, String name) {
		this.componentType = componentType;
		this.name = name;
	}
	public ComponentType getComponentType() {
		return componentType;
	}
	public void setComponentType(ComponentType componentType) {
		this.componentType = componentType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
}