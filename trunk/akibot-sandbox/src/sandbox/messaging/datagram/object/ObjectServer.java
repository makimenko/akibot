package sandbox.messaging.datagram.object;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ObjectServer {
	public static void main(String[] args) throws Exception {
		int PORT = 4000;
		byte[] buf = new byte[1000];
		Utils utils = new Utils();
		DatagramPacket requestDatagramPacket = new DatagramPacket(buf, buf.length);
		DatagramSocket socket = new DatagramSocket(PORT);
		System.out.println("Server started");
		while (true) {
			socket.receive(requestDatagramPacket);

			MyObject myObject = (MyObject) utils.byteToObject(requestDatagramPacket.getData());
			myObject.setName("ECHO: " + myObject.getName());

			buf = utils.objectToByte(myObject);
			DatagramPacket responseDatagramPacket = new DatagramPacket(buf, buf.length, requestDatagramPacket.getAddress(),
					requestDatagramPacket.getPort());
			socket.send(responseDatagramPacket);
			
		}
	}
}