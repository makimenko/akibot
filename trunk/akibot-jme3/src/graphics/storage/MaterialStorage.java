package graphics.storage;

import java.util.HashMap;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture;

public class MaterialStorage {
	static HashMap<String, Material> materials;

	public static void loadMaterials(AssetManager assetManager) {
		materials = new HashMap<String, Material>();
		// materials.put("material1", loadMaterialFromFile(assetManager,
		// "res/texture/tex1.jpg"));
		materials.put("green", loadSimpleColor(assetManager, new ColorRGBA(0, 1, 0, 0.3f)));
		materials.put("red", loadSimpleColor(assetManager, ColorRGBA.Red));
		materials.put("blue", loadSimpleColor(assetManager, ColorRGBA.Blue));
	}

	private static Material loadSimpleColor(AssetManager assetManager, ColorRGBA color) {
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", color);
		mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		return mat;
	}

	private static Material loadMaterialFromFile(AssetManager assetManager, String texture) {
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/SimpleTextured.j3md");
		TextureKey key = new TextureKey(texture, true);
		key.setGenerateMips(true);
		Texture tex = assetManager.loadTexture(key);
		tex.setMinFilter(Texture.MinFilter.Trilinear);
		mat.setTexture("m_ColorMap", tex);
		return mat;
	}

	public static Material getMaterial(AssetManager assetManager, String name) {
		if (materials == null)
			loadMaterials(assetManager);
		return materials.get(name);
	}

}
