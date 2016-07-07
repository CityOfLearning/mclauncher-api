package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import java.io.File;

import sk.tomsik68.mclauncher.api.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.api.versions.IVersion;

final class MCDJarManager {
	private final File versionsFolder;

	MCDJarManager(MinecraftInstance mc) {
		versionsFolder = new File(mc.getLocation(), "versions");
	}

	File getInfoFile(IVersion version) {
		return new File(getVersionFolder(version), version.getId() + ".json");
	}

	File getVersionFolder(IVersion version) {
		return new File(versionsFolder, version.getId());
	}

	File getVersionJAR(MCDownloadVersion version) {
		return new File(new File(versionsFolder, version.getJarVersion()), version.getJarVersion() + ".jar");
	}
}
