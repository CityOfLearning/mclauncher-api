package sk.tomsik68.mclauncher.impl.login.yggdrasil;

import java.util.HashMap;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

final class YDLoginResponse extends YDResponse {
	private final String sessionID, clientToken;
	private final YDPartialGameProfile selectedProfile;
	private HashMap<String, YDPartialGameProfile> profiles = new HashMap<String, YDPartialGameProfile>();
	private YDUserObject user;

	public YDLoginResponse(JSONObject json) {
		super(json);
		this.sessionID = json.get("accessToken").toString();
		this.clientToken = json.get("clientToken").toString();
		this.selectedProfile = new YDPartialGameProfile((JSONObject) json.get("selectedProfile"));
		JSONArray profiles = (JSONArray) json.get("availableProfiles");
		if (profiles != null) {
			for (Object object : profiles) {
				JSONObject jsonObj = (JSONObject) object;
				YDPartialGameProfile p = new YDPartialGameProfile(jsonObj);
				this.profiles.put(p.getName(), p);
			}
		}
		if (json.containsKey("user")) {
			this.user = new YDUserObject((JSONObject) json.get("user"));
		}
	}

	public String getClientToken() {
		return this.clientToken;
	}

	public YDPartialGameProfile getProfile(String name) {
		return this.profiles.get(name);
	}

	public YDPartialGameProfile getSelectedProfile() {
		return this.selectedProfile;
	}

	public String getSessionID() {
		return this.sessionID;
	}

	public YDUserObject getUserObject() {
		if (this.user == null) {
			this.user = new YDUserObject(this.selectedProfile.getName());
		}
		return this.user;
	}
}
