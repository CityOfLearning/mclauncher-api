package sk.tomsik68.mclauncher.impl.versions.mcassets;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import sk.tomsik68.mclauncher.api.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.api.ui.IProgressMonitor;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionInstallListener;
import sk.tomsik68.mclauncher.api.versions.IVersionInstaller;
import sk.tomsik68.mclauncher.impl.common.Platform;
import sk.tomsik68.mclauncher.util.ExtractUtils;
import sk.tomsik68.mclauncher.util.MCFileUtils;

@Deprecated
public final class MCAssetsVersionInstaller implements IVersionInstaller {
	private static final String LWJGL_DOWNLOAD_URL = "http://kent.dl.sourceforge.net/project/java-game-lib/Official%20Releases/LWJGL%202.9.0/lwjgl-2.9.0.zip";
	private static final String RESOURCES_DOWNLOAD_URL = "http://s3.amazonaws.com/MinecraftResources/";

	static File[] getDefaultLWJGLJars(File mcLocation) {
		File oldLib = new File(mcLocation, "oldLib");
		if (!oldLib.exists()) {
			oldLib.mkdirs();
		}
		return new File[] { new File(oldLib, "lwjgl.jar"), new File(oldLib, "lwjgl_util.jar"),
				new File(oldLib, "jinput.jar") };
	}

	private final ArrayList<IVersionInstallListener> listeners = new ArrayList<IVersionInstallListener>();

	public MCAssetsVersionInstaller() {

	}

	@Override
	public void addVersionInstallListener(IVersionInstallListener listener) {
		listeners.add(listener);
	}

	private File getResourceLocation(File mcLocation, String resource) {
		File file = new File(mcLocation, "resources" + File.separator + resource.replace('/', File.separatorChar));
		return file;
	}

	private String getVersionURL(String id) {
		return "http://assets.minecraft.net/" + id + "/minecraft.jar";
	}

	@Override
	public void install(IVersion version, MinecraftInstance mc, IProgressMonitor progress) throws Exception {
		MCAJarManager jarManager = new MCAJarManager(mc);
		String url = getVersionURL(version.getId());
		// if the jar doesn't exist, download it
		if (!jarManager.getVersionFile(version).exists()) {
			MCFileUtils.downloadFileWithProgress(url, jarManager.getVersionFile(version), progress);
		}

		// check if LWJGL needs to be downloaded
		File[] lwjgl = getDefaultLWJGLJars(mc.getLocation());
		boolean update = false;
		for (File file : lwjgl) {
			update = update || !file.exists();
		}
		update = update || !jarManager.getNativesDirectory().exists();
		if (update) {
			updateJARs(jarManager, mc, version, progress);
		}
		// update resource files
		updateResources(mc.getLocation(), progress);
		notifyListeners(version);

	}

	private void notifyListeners(IVersion version) {
		for (IVersionInstallListener listener : listeners) {
			listener.versionInstalled(version);
		}
	}

	private void updateJARs(MCAJarManager jarManager, MinecraftInstance mc, IVersion version, IProgressMonitor progress)
			throws Exception {
		// download LWJGL from sourceforge
		File lwjglDir = new File(jarManager.getNativesDirectory(), "lwjgl-2.9.0");
		lwjglDir.deleteOnExit();
		File dest = new File(jarManager.getNativesDirectory(), "lwjgl.zip");
		MCFileUtils.downloadFileWithProgress(LWJGL_DOWNLOAD_URL, dest, progress);
		jarManager.getNativesDirectory().mkdirs();
		dest.deleteOnExit();
		// extract all things from ZIP
		ExtractUtils.extractZipWithoutRules(dest, jarManager.getNativesDirectory());
		File[] lwjgl = getDefaultLWJGLJars(mc.getLocation());
		// move JARs from LWJGL
		for (File file : lwjgl) {
			MCFileUtils.copyFile(new File(lwjglDir + File.separator + "jar", file.getName()), file);
		}
		// move natives
		File[] nativeThings = new File(lwjglDir,
				"native" + File.separator + Platform.wrapName(Platform.getCurrentPlatform().getMinecraftName()))
						.listFiles();
		for (File file : nativeThings) {
			MCFileUtils.copyFile(file, new File(jarManager.getNativesDirectory(), file.getName()));
		}
	}

	private void updateResources(File mcLocation, IProgressMonitor progress) throws Exception {
		// parse resources
		ResourcesXMLParser parser = new ResourcesXMLParser(RESOURCES_DOWNLOAD_URL);
		List<String> resources = parser.parse();
		// download them one by one
		for (String resource : resources) {
			File dest = getResourceLocation(mcLocation, resource);
			if (!dest.exists()) {
				// make all directories
				if (!dest.mkdirs()) {
					throw new IOException("Failed to mkdirs for '".concat(dest.getAbsolutePath()).concat("'"));
				}
				if (!resource.endsWith("/")) {
					// if it's a file, remove this directory and download it.
					if (!dest.delete()) {
						throw new IOException("Failed to remove file: '".concat(dest.getAbsolutePath()).concat("'"));
					}
					MCFileUtils.downloadFileWithProgress(RESOURCES_DOWNLOAD_URL + URLEncoder.encode(resource, "UTF-8"),
							dest, progress);
				}
			}
		}

	}

}
