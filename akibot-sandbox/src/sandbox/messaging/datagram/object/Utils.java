package sandbox.messaging.datagram.object;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Utils {

	public byte[] objectToByte(Object object) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(object);
		oos.flush();
		return baos.toByteArray();
	}

	public Object byteToObject(byte[] byteArray) throws IOException, ClassNotFoundException {
		ByteArrayInputStream baos = new ByteArrayInputStream(byteArray);
		ObjectInputStream oos = new ObjectInputStream(baos);
		return oos.readObject();
	}
}
