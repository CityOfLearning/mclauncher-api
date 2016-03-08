package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.json.IJSONSerializable;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionInstaller;
import sk.tomsik68.mclauncher.api.versions.IVersionLauncher;
import sk.tomsik68.mclauncher.impl.versions.mcdownload.Rule.Action;

final class MCDownloadVersion implements IVersion, IJSONSerializable {
	private static final MCDownloadVersionInstaller installer = new MCDownloadVersionInstaller();
	private static final IVersionLauncher launcher = new MCDownloadVersionLauncher();
	private static final String DEFAULT_ASSETS_INDEX = "legacy";

	private String id, time, releaseTime, type, minecraftArgs, mainClass, jarVersion;
	private int minimumLauncherVersion;
	private final JSONObject json;
	private String incompatibilityReason, processArgs, assets, inheritsFrom;
	private ArrayList<Rule> rules = new ArrayList<Rule>();
	private ArrayList<Library> libraries = new ArrayList<Library>();

	private boolean needsInheritance;

	MCDownloadVersion(JSONObject json) {
		this.json = json;
		this.id = json.get("id").toString();
		if (json.containsKey("jar")) {
			this.jarVersion = json.get("jar").toString();
		} else {
			this.jarVersion = this.id;
		}
		this.time = json.get("time").toString();
		this.releaseTime = json.get("releaseTime").toString();
		this.type = json.get("type").toString();
		if (json.containsKey("processArguments")) {
			this.processArgs = json.get("processArguments").toString();
		}
		this.minecraftArgs = json.get("minecraftArguments").toString();
		this.minimumLauncherVersion = Integer.parseInt(json.get("minimumLauncherVersion").toString());
		this.mainClass = json.get("mainClass").toString();
		if (json.containsKey("assets")) {
			this.assets = json.get("assets").toString();
		} else {
			this.assets = DEFAULT_ASSETS_INDEX;
		}
		if (json.containsKey("rules")) {
			JSONArray rulesArray = (JSONArray) json.get("rules");
			for (Object o : rulesArray) {
				JSONObject jsonRule = (JSONObject) o;
				this.rules.add(new Rule(jsonRule));
			}
		}
		if (json.containsKey("libraries")) {
			JSONArray libs = (JSONArray) json.get("libraries");
			for (int i = 0; i < libs.size(); ++i) {
				this.libraries.add(new Library((JSONObject) libs.get(i)));
			}
		}
		if (json.containsKey("incompatibilityReason")) {
			this.incompatibilityReason = json.get("incompatibilityReason").toString();
		}
		if (json.containsKey("inheritsFrom")) {
			this.inheritsFrom = json.get("inheritsFrom").toString();
			this.needsInheritance = true;
		} else {
			this.needsInheritance = false;
		}
	}

	@Override
	public int compareTo(IVersion arg0) {
		return this.getId().compareTo(arg0.getId());
	}

	void doInherit(MCDownloadVersion parent) {
		MCLauncherAPI.log.finer("Inheriting version ".concat(this.id).concat(" from ").concat(parent.getId()));
		if (!parent.getId().equals(this.getInheritsFrom())) {
			throw new IllegalArgumentException("Wrong inheritance version passed!");
		}

		if (this.minecraftArgs == null) {
			this.minecraftArgs = parent.getMinecraftArgs();
		}

		if (this.mainClass == null) {
			this.mainClass = parent.getMainClass();
		}

		if (this.incompatibilityReason == null) {
			this.incompatibilityReason = parent.getIncompatibilityReason();
		}

		if (this.assets == null) {
			this.assets = parent.getAssetsIndexName();
		}

		this.libraries.addAll(parent.getLibraries());
		this.rules.addAll(parent.rules);

		if ((this.jarVersion == null) || this.jarVersion.isEmpty()) {
			this.jarVersion = parent.getJarVersion();
		}

		if (this.rules.isEmpty()) {
			this.rules.addAll(parent.rules);
		}

		this.needsInheritance = false;
		MCLauncherAPI.log.finer("Inheriting version ".concat(this.id).concat(" finished."));
	}

	String getAssetsIndexName() {
		return this.assets;
	}

	@Override
	public String getDisplayName() {
		return this.type.concat(" ").concat(this.id);
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getIncompatibilityReason() {
		return this.incompatibilityReason;
	}

	String getInheritsFrom() {
		return this.inheritsFrom;
	}

	@Override
	public IVersionInstaller getInstaller() {
		return installer;
	}

	String getJarVersion() {
		return this.jarVersion;
	}

	@Override
	public IVersionLauncher getLauncher() {
		return launcher;
	}

	List<Library> getLibraries() {
		return this.libraries;
	}

	String getMainClass() {
		return this.mainClass;
	}

	String getMinecraftArgs() {
		return this.minecraftArgs;
	}

	int getMinimumLauncherVersion() {
		return this.minimumLauncherVersion;
	}

	String getProcessArgs() {
		return this.processArgs;
	}

	String getReleaseTime() {
		return this.releaseTime;
	}

	String getTime() {
		return this.time;
	}

	String getType() {
		return this.type;
	}

	@Override
	public String getUniqueID() {
		return this.type.charAt(0) + this.getId();
	}

	/**
	 *
	 * @return True if this version is compatible with our current operating
	 *         system
	 */
	@Override
	public boolean isCompatible() {
		Action action = null;
		for (Rule rule : this.rules) {
			if (rule.applies()) {
				action = rule.getAction();
			}
		}
		return this.rules.isEmpty() || (action == Action.ALLOW);
	}

	boolean needsInheritance() {
		return this.needsInheritance;
	}

	@Override
	public JSONObject toJSON() {
		return this.json;
	}
}
