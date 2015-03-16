package akibot.jni.java;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipFile;

public class AkibotJniLibrary {

    static {
        try {
            final Class c = AkibotJniTest.class;
            final URL location = c.getProtectionDomain().getCodeSource().getLocation();
            final String libraryname = "akibot-jni-cpp.so";

            ZipFile zf = new ZipFile(location.getPath());
            InputStream in = zf.getInputStream(zf.getEntry(libraryname));

            File f = File.createTempFile("JARLIB-", "-"+libraryname);
            FileOutputStream out = new FileOutputStream(f);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();

            System.load(f.getAbsolutePath());
            f.delete();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public native void initialize();

    public native int getDistance(int triggerPin, int echoPin, int timeoutMicroseconds);

    public native void servo(int servoPin, int initialValue, int pwmRange, int divisor, int value, int microseconds);
    
    public native int pulseIn(int pin, int level, int timeoutMicroseconds);

}
