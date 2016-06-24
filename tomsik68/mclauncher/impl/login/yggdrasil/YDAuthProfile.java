package sk.tomsik68.mclauncher.impl.login.yggdrasil;

import net.minidev.json.JSONObject;
import sk.tomsik68.mclauncher.api.json.IJSONSerializable;
import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.ISession;

public final class YDAuthProfile implements IProfile, IJSONSerializable {
	private final String userName;
	private final String uuid;
	private final String displayName;
	private final String userId;
	private String accessToken;
	private String profileName = "(Default)";

	private final String SKINS_ROOT = "http://skins.minecraft.net/MinecraftSkins/";

	public YDAuthProfile(JSONObject json) {
		userName = json.get("username").toString();
		accessToken = json.get("accessToken").toString();
		uuid = json.get("uuid").toString();
		displayName = json.get("displayName").toString();
		userId = json.get("userid").toString();

	}

	public YDAuthProfile(String name, String displayName, String sessid, String uuid, String userId) {
		userName = name;
		accessToken = sessid;
		this.uuid = uuid;
		this.displayName = displayName;
		this.userId = userId;
	}

	YDAuthProfile(YDSession session) {
		this(session.getUsername(), session.getUsername(), session.getSessionID(), session.getUUID(),
				session.getUserObject().id);
	}

	String getDisplayName() {
		return displayName;
	}

	@Override
	public String getName() {
		return userName;
	}

	@Override
	public String getPassword() {
		return accessToken;
	}

	String getProfileName() {
		return profileName;
	}

	@Override
	public String getSkinURL() {
		StringBuilder url = new StringBuilder(SKINS_ROOT);
		url = url.append(getUUID()).append(".png");
		return url.toString();
	}

	String getUserId() {
		return userId;
	}

	public String getUUID() {
		return uuid;
	}

	YDPartialGameProfile getYDGameProfile() {
		YDPartialGameProfile result = new YDPartialGameProfile(userName, uuid, false);
		return result;
	}

	public void setPassword(String sessionID) {
		accessToken = sessionID;
	}

	void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("username", userName);
		json.put("accessToken", accessToken);
		json.put("uuid", uuid);
		json.put("userid", userId);
		json.put("displayName", displayName);
		return json;
	}

	void update(ISession session) {
		setPassword(session.getSessionID());
	}

}
