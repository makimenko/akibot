package graphics.storage;

import java.util.HashMap;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture;

public class MaterialStorage {
	private static HashMap<String, Material> materials;
	private AssetManager assetManager;

	public MaterialStorage(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	private void loadInitialMaterials() {
		materials = new HashMap<String, Material>();
		// materials.put("material1", loadMaterialFromFile(assetManager,
		// "res/texture/tex1.jpg"));
		materials.put("green", loadSimpleColor(new ColorRGBA(0, 1, 0, 0.3f), false));
		materials.put("red", loadSimpleColor(ColorRGBA.Red, false));
		materials.put("blue", loadSimpleColor(ColorRGBA.Blue, false));

		materials.put("ground", loadSimpleColor(ColorRGBA.Brown, true));
		materials.put("object", loadSimpleColor(ColorRGBA.Pink, true));

	}

	public Material loadSimpleColor(ColorRGBA color, boolean wireframe) {
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", color);
		mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		mat.getAdditionalRenderState().setWireframe(wireframe);
		return mat;
	}

	public Material loadMaterialFromFile(String texture) {
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/SimpleTextured.j3md");
		TextureKey key = new TextureKey(texture, true);
		key.setGenerateMips(true);
		Texture tex = assetManager.loadTexture(key);
		tex.setMinFilter(Texture.MinFilter.Trilinear);
		mat.setTexture("m_ColorMap", tex);
		return mat;
	}

	public Material getMaterial(String name) {
		if (materials == null)
			loadInitialMaterials();
		return materials.get(name);
	}

}
