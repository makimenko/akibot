/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package akibot.jni.java;

/**
 *
 * @author dm
 */
public class AkibotJniTest {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("STARTING...");
        AkibotJniLibraryInstance001 lib = new AkibotJniLibraryInstance001();
        lib.initialize();
        for (int i = 0; i <= 100; i++) {
            int cm = lib.getDistance(13, 12, 50000);
            System.out.println("Distance is: " + cm + " cm");
            Thread.sleep(50);
        }
        System.out.println("END");
    }

}
