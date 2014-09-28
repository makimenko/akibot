package sandbox.motion.simple;

import java.util.Comparator;

public class AscendingComparator implements Comparator<int[]> {

	@Override
    public int compare(int[] i1, int[] i2) {
        int t1 = i1[0];
        int t2 = i2[0];
        return (t1 < t2? -1: 1);
    }
    
}

