package com.akibot.tanktrack.launcher;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class Test {
	public static void main(String[] args) throws Exception {
		System.out.println("IP = " + getLocalIP());
	}

	static public String getLocalIP() throws Exception {
		Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
		while (e.hasMoreElements()) {
			NetworkInterface n = e.nextElement();
			Enumeration<InetAddress> ee = n.getInetAddresses();
			while (ee.hasMoreElements()) {
				InetAddress i = ee.nextElement();
				if (!i.isLoopbackAddress()) {
					return i.getHostAddress();
				}
			}
		}
		throw new Exception("Unable to identify IP address");
	}
}
