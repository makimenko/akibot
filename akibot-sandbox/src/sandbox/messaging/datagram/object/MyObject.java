package sandbox.messaging.datagram.object;

import java.io.Serializable;

public class MyObject implements Serializable {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
