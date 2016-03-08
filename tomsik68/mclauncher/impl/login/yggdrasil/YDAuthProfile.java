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
		this.userName = json.get("username").toString();
		this.accessToken = json.get("accessToken").toString();
		this.uuid = json.get("uuid").toString();
		this.displayName = json.get("displayName").toString();
		this.userId = json.get("userid").toString();

	}

	public YDAuthProfile(String name, String displayName, String sessid, String uuid, String userId) {
		this.userName = name;
		this.accessToken = sessid;
		this.uuid = uuid;
		this.displayName = displayName;
		this.userId = userId;
	}

	YDAuthProfile(YDSession session) {
		this(session.getUsername(), session.getUsername(), session.getSessionID(), session.getUUID(),
				session.getUserObject().id);
	}

	String getDisplayName() {
		return this.displayName;
	}

	@Override
	public String getName() {
		return this.userName;
	}

	@Override
	public String getPassword() {
		return this.accessToken;
	}

	String getProfileName() {
		return this.profileName;
	}

	@Override
	public String getSkinURL() {
		StringBuilder url = new StringBuilder(this.SKINS_ROOT);
		url = url.append(this.getUUID()).append(".png");
		return url.toString();
	}

	String getUserId() {
		return this.userId;
	}

	public String getUUID() {
		return this.uuid;
	}

	YDPartialGameProfile getYDGameProfile() {
		YDPartialGameProfile result = new YDPartialGameProfile(this.userName, this.uuid, false);
		return result;
	}

	public void setPassword(String sessionID) {
		this.accessToken = sessionID;
	}

	void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("username", this.userName);
		json.put("accessToken", this.accessToken);
		json.put("uuid", this.uuid);
		json.put("userid", this.userId);
		json.put("displayName", this.displayName);
		return json;
	}

	void update(ISession session) {
		this.setPassword(session.getSessionID());
	}

}
