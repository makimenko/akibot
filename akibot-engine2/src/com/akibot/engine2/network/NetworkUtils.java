package com.akibot.engine2.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class NetworkUtils {

	public String getLocalIP() throws Exception {
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
