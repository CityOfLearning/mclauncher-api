package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import net.minidev.json.JSONObject;

/**
 * Represents a single Asset. It may be a sound file, texture, language file
 * etc.
 */
final class Asset {
	private static final String RESOURCES_URL = "http://resources.download.minecraft.net/";

	private final String hash, key;
	private final int size;

	Asset(JSONObject obj, String key) {
		this.hash = obj.get("hash").toString();
		this.size = (Integer) obj.get("size");
		this.key = key;
	}

	/**
	 *
	 * @return Hash of this Asset
	 */
	String getHash() {
		return this.hash;
	}

	/**
	 *
	 * @return Key/Name of this asset in JSON structure
	 */
	String getKey() {
		return this.key;
	}

	/**
	 *
	 * @return First 2 characters of hash
	 */
	String getPreHash() {
		return this.hash.substring(0, 2);
	}

	/**
	 * Size of this Asset
	 * 
	 * @return Size in bytes
	 */
	int getSize() {
		return this.size;
	}

	/**
	 *
	 * @return URL where this asset can be downloaded
	 */
	String getUrl() {
		return RESOURCES_URL + this.getPreHash() + "/" + this.hash;
	}
}
