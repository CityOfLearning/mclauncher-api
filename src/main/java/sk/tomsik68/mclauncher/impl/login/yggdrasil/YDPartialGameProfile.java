package sk.tomsik68.mclauncher.impl.login.yggdrasil;

import net.minidev.json.JSONObject;
import sk.tomsik68.mclauncher.api.json.IJSONSerializable;

final class YDPartialGameProfile implements IJSONSerializable {
	private final String name, id;
	private final boolean legacy;

	public YDPartialGameProfile(JSONObject jsonObj) {
		this(jsonObj.get("name").toString(), jsonObj.get("id").toString(), false);

	}

	public YDPartialGameProfile(String name, String id, boolean isLegacy) {
		this.name = name;
		this.id = id;
		legacy = isLegacy;
	}

	String getId() {
		return id;
	}

	String getName() {
		return name;
	}

	boolean isLegacy() {
		return legacy;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		obj.put("id", id);
		obj.put("name", name);
		return obj;
	}
}
