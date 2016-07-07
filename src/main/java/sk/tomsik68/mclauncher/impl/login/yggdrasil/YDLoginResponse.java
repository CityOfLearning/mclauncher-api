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
		sessionID = json.get("accessToken").toString();
		clientToken = json.get("clientToken").toString();
		selectedProfile = new YDPartialGameProfile((JSONObject) json.get("selectedProfile"));
		JSONArray profiles = (JSONArray) json.get("availableProfiles");
		if (profiles != null) {
			for (Object object : profiles) {
				JSONObject jsonObj = (JSONObject) object;
				YDPartialGameProfile p = new YDPartialGameProfile(jsonObj);
				this.profiles.put(p.getName(), p);
			}
		}
		if (json.containsKey("user")) {
			user = new YDUserObject((JSONObject) json.get("user"));
		}
	}

	public String getClientToken() {
		return clientToken;
	}

	public YDPartialGameProfile getProfile(String name) {
		return profiles.get(name);
	}

	public YDPartialGameProfile getSelectedProfile() {
		return selectedProfile;
	}

	public String getSessionID() {
		return sessionID;
	}

	public YDUserObject getUserObject() {
		if (user == null) {
			user = new YDUserObject(selectedProfile.getName());
		}
		return user;
	}
}
