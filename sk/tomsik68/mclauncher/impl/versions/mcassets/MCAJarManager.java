package sk.tomsik68.mclauncher.impl.versions.mcassets;

import java.io.File;

import sk.tomsik68.mclauncher.api.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.api.versions.IVersion;

/**
 * JAR provider specifically for MCAssetsVersions. Model: Minecraft 1.4 jar
 * location: .minecraft/bin/1.4.jar Natives location: .minecraft/natives
 */
@Deprecated
final class MCAJarManager {
	private final File binDirectory;
	private final File nativesDirectory;

	MCAJarManager(MinecraftInstance mc) {
		this.nativesDirectory = new File(mc.getLocation(), "natives");
		this.binDirectory = new File(mc.getLocation(), "bin");
	}

	public File getNativesDirectory() {
		return this.nativesDirectory;
	}

	File getVersionFile(IVersion version) {
		return new File(this.binDirectory, version.getId() + ".jar");
	}
}
