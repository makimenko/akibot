package sandbox.messaging.fixedsize;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class FixedSizeClient {

	public static void main(String[] args) throws Exception {
		int PORT = 4000;
		String host = "raspberrypi";

		DatagramSocket socket = new DatagramSocket();
		byte[] buf = new byte[1000];
		DatagramPacket inDatagramPacket = new DatagramPacket(buf, buf.length);

		InetAddress hostAddress = InetAddress.getByName(host);
		socket.setTrafficClass(0x02);
		long timeout = 10000;
		long startTime = System.currentTimeMillis();
		int count = 0;
		while (System.currentTimeMillis() - startTime < timeout) {
			count++;
			String requestMessage = "Michael " + count;
			buf = requestMessage.getBytes();
			DatagramPacket outDatagramPacket = new DatagramPacket(buf, buf.length, hostAddress, PORT);
			socket.send(outDatagramPacket);

			socket.receive(inDatagramPacket);
			String responseMessage = new String(inDatagramPacket.getData(), 0, inDatagramPacket.getLength());
			// System.out.println(responseMessage);
		}
		long duration = System.currentTimeMillis() - startTime;
		System.out.println("Stats: count=" + count + ", duration=" + duration + ", avg=" + (duration / count));
		// Stats: count=267015, duration=10000, avg=0
	}
}
