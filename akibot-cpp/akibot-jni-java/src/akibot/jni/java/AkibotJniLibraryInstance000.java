package akibot.jni.java;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipFile;

public class AkibotJniLibraryInstance000 extends AkibotJniLibrary {
    static String INSTANCE_NAME = "000";
    static {
        try {
            final String libraryname = "akibot-jni-cpp-"+INSTANCE_NAME+".so";
            System.out.println("AkibotJniLibrary Loading: "+libraryname);
                        
            final Class c = AkibotJniTest.class;
            final URL location = c.getProtectionDomain().getCodeSource().getLocation();

            ZipFile zf = new ZipFile(location.getPath());
            InputStream in = zf.getInputStream(zf.getEntry(libraryname));

            File f = File.createTempFile("JARLIB-"+INSTANCE_NAME+"-", "-"+libraryname);
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
    
}
