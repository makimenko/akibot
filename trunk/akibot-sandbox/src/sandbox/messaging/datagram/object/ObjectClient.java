package sandbox.messaging.datagram.object;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ObjectClient {

	public static void main(String[] args) throws Exception {
		int PORT = 4000;
		String host = "raspberrypi";
		Utils utils = new Utils();

		DatagramSocket socket = new DatagramSocket();
		byte[] buf = new byte[1000];
		DatagramPacket inDatagramPacket = new DatagramPacket(buf, buf.length);

		InetAddress hostAddress = InetAddress.getByName(host);

		System.out.println("Client started...");
		long timeout = 10000;
		long startTime = System.currentTimeMillis();
		int count = 0;
		while (System.currentTimeMillis() - startTime < timeout) {
			count++;
			MyObject myObject = new MyObject();
			myObject.setName("Michael " + count);

			buf = utils.objectToByte(myObject);

			DatagramPacket outDatagramPacket = new DatagramPacket(buf, buf.length, hostAddress, PORT);
			socket.send(outDatagramPacket);

			socket.receive(inDatagramPacket);

			MyObject responseObject = (MyObject) utils.byteToObject(inDatagramPacket.getData());

		}
		long duration = System.currentTimeMillis() - startTime;
		System.out.println("Stats: count=" + count + ", duration=" + duration + ", avg=" + (duration / count));
		// Stats: count=124048, duration=10000, avg=0

	}

}
