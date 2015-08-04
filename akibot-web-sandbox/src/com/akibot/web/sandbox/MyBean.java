package com.akibot.web.sandbox;

import javax.persistence.Column;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MyBean {

	@Column(name = "ID")
	private long id;

	@Column(name = "NAME")
	private String name;

	public MyBean() {
		
	}
	public MyBean(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
