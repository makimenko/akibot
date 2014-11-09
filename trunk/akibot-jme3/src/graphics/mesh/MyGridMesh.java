package graphics.mesh;

import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;

public class MyGridMesh extends Mesh {

	public MyGridMesh(float max, float step) {
		super();
		this.setMode(Mesh.Mode.Lines);
		int arrLength = ((int) (max / step) + 1) * 3 * 2 * 2;
		int v = 0;
		float[] f = new float[arrLength];
		for (float i = 0; i <= max; i += step) {
			// Line 1:
			f[v++] = -max / 2;
			f[v++] = 0;
			f[v++] = i - (max / 2);
			f[v++] = max / 2;
			f[v++] = 0;
			f[v++] = i - (max / 2);

			// Line 2:
			f[v++] = i - (max / 2);
			f[v++] = 0;
			f[v++] = -max / 2;
			f[v++] = i - (max / 2);
			f[v++] = 0;
			f[v++] = max / 2;
		}
		this.setBuffer(VertexBuffer.Type.Position, 3, f);
		this.updateBound();
		this.updateCounts();
	}
}
