package sk.tomsik68.mclauncher.impl.login.legacy;

import sk.tomsik68.mclauncher.api.login.IProfile;

public final class LegacyProfile implements IProfile {

	private String pass;
	private String name;

	private final String SKINS_ROOT = "http://skins.minecraft.net/MinecraftSkins/";

	public LegacyProfile(String username, String password) {
		this.name = username;
		this.pass = password;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getPassword() {
		return this.pass;
	}

	@Override
	public String getSkinURL() {
		StringBuilder url = new StringBuilder(this.SKINS_ROOT);
		url = url.append(this.getName());
		url = url.append(".png");
		return url.toString();
	}

	public boolean isRemember() {
		return this.pass.length() > 0;
	}

}
