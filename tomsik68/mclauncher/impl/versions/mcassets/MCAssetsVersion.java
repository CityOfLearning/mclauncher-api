package sk.tomsik68.mclauncher.impl.versions.mcassets;

import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionInstaller;
import sk.tomsik68.mclauncher.api.versions.IVersionLauncher;

@Deprecated
final class MCAssetsVersion implements IVersion {
	private static final MCAssetsVersionInstaller installer = new MCAssetsVersionInstaller();
	private static final MCAssetsVersionLauncher launcher = new MCAssetsVersionLauncher();
	private final String id;

	public MCAssetsVersion(String vid) {
		this.id = vid;
	}

	@Override
	public int compareTo(IVersion o) {
		return this.getId().compareTo(o.getId());
	}

	@Override
	public String getDisplayName() {
		String type = "release";
		if (MCAssetsVersionList.isSnapshot(this)) {
			type = "snapshot";
		}
		return type.concat(" ").concat(this.getId());
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public String getIncompatibilityReason() {
		return null;
	}

	@Override
	public IVersionInstaller getInstaller() {
		return installer;
	}

	@Override
	public IVersionLauncher getLauncher() {
		return launcher;
	}

	@Override
	public String getUniqueID() {
		return this.getId();
	}

	@Override
	public boolean isCompatible() {
		return true;
	}

}
