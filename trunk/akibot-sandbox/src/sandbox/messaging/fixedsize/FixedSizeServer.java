package sandbox.messaging.fixedsize;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class FixedSizeServer {
	public static void main(String[] args) throws Exception {
		int PORT = 4000;
		byte[] buf = new byte[1000];
		DatagramPacket requestDatagramPacket = new DatagramPacket(buf, buf.length);
		DatagramSocket socket = new DatagramSocket(PORT);
		socket.setTrafficClass(0x02);
		System.out.println("Server started");
		while (true) {
			socket.receive(requestDatagramPacket);
			String request = new String(requestDatagramPacket.getData(), 0, requestDatagramPacket.getLength());

			String response = "ECHO: " + request;
			buf = response.getBytes();
			DatagramPacket responseDatagramPacket = new DatagramPacket(buf, buf.length, requestDatagramPacket.getAddress(),
					requestDatagramPacket.getPort());
			socket.send(responseDatagramPacket);
		}
	}
}