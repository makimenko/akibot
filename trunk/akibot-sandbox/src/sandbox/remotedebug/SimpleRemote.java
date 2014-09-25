package sandbox.remotedebug;

import java.net.InetAddress;

public class SimpleRemote {
	public static void main(String[] args) throws Exception {
		System.out.println("Start...");

		for (int i = 0; i <= 10; i++) {
			String host = InetAddress.getLocalHost().getHostName();
			System.out.println("host=" + host);
		}

		System.out.println("...END.");
	}
}