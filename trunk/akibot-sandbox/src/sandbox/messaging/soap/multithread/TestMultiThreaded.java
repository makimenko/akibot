package sandbox.messaging.soap.multithread;

public class TestMultiThreaded {

	public static void main(String[] args) {
		MultiThreadedServer server = new MultiThreadedServer(9000);
		new Thread(server).start();

		try {
			Thread.sleep(20 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Stopping Server");
		server.stop();

	}
}
