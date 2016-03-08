package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import java.io.File;

import sk.tomsik68.mclauncher.api.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.api.versions.IVersion;

final class MCDJarManager {
	private final File versionsFolder;

	MCDJarManager(MinecraftInstance mc) {
		this.versionsFolder = new File(mc.getLocation(), "versions");
	}

	File getInfoFile(IVersion version) {
		return new File(this.getVersionFolder(version), version.getId() + ".json");
	}

	File getVersionFolder(IVersion version) {
		return new File(this.versionsFolder, version.getId());
	}

	File getVersionJAR(MCDownloadVersion version) {
		return new File(new File(this.versionsFolder, version.getJarVersion()), version.getJarVersion() + ".jar");
	}
}
