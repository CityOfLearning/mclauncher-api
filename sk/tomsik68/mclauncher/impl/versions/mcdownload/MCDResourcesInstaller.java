package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import java.io.File;
import java.io.FileReader;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.api.ui.IProgressMonitor;
import sk.tomsik68.mclauncher.util.FilePathBuilder;
import sk.tomsik68.mclauncher.util.FileUtils;

final class MCDResourcesInstaller {

	private static final String RESOURCES_INDEX_URL = "https://s3.amazonaws.com/Minecraft.Download/indexes/";
	private final File assetsDir, indexesDir, objectsDir, virtualDir;

	MCDResourcesInstaller(MinecraftInstance mc) {
		this.assetsDir = new File(mc.getLocation(), "assets");
		this.indexesDir = new File(this.assetsDir, "indexes");
		this.objectsDir = new File(this.assetsDir, "objects");
		this.virtualDir = new File(this.assetsDir, "virtual");
	}

	/**
	 * Download all the {@link Asset} objects inside this index
	 * 
	 * @param index
	 *            AssetIndex to download from
	 * @param progress
	 * @throws Exception
	 */
	private void downloadAssetList(AssetIndex index, IProgressMonitor progress) throws Exception {
		for (Asset asset : index.getAssets()) {
			MCLauncherAPI.log.info("Updating asset file: ".concat(asset.getKey()).concat(" (size:")
					.concat("" + asset.getSize()).concat("B)"));
			if (progress != null) {
				progress.setStatus("Updating asset file: " + asset.getKey());
			}

			File dest = this.getDestFile(index, asset);
			dest.getParentFile().mkdirs();
			if (!dest.exists() || (dest.length() != asset.getSize())) {
				MCLauncherAPI.log.finest("Downloading ".concat(asset.getKey()));
				FileUtils.downloadFileWithProgress(asset.getUrl(), dest, progress);
			} else {
				MCLauncherAPI.log.finest("No need to update ".concat(asset.getKey()));
			}
		}
	}

	File getAssetsDirectory() {
		return this.assetsDir;
	}

	/**
	 *
	 * @param index
	 *            AssetIndex where the asset belongs to
	 * @param asset
	 *            Asset which is going to be downloaded
	 * @return Path to file where the asset in given index should be located
	 */
	private File getDestFile(AssetIndex index, Asset asset) {
		File result;
		// the difference is, that some indexes are virtual.
		// virtual indexes have their assets downloaded to assets/virtual/1.7.3
		// etc
		if (index.isVirtual()) {
			File assetsDir = new File(this.virtualDir, index.getName());
			assetsDir.mkdirs();
			String path = asset.getKey().replace('/', File.separatorChar);
			result = new File(assetsDir, path);
		} else {
			// while non-virtual indexes download them into 'objects' folder and
			// name them after their hash
			FilePathBuilder pathBuilder = new FilePathBuilder(this.objectsDir);
			pathBuilder.append(asset.getPreHash()).append(asset.getHash());
			result = pathBuilder.getResult();
		}
		return result;
	}

	/**
	 * Installs resources for given version
	 * 
	 * @param version
	 *            Version to install resources for
	 * @param progress
	 *            ProgressMonitor
	 * @throws Exception
	 *             Connection and I/O errors
	 */
	void installAssetsForVersion(MCDownloadVersion version, IProgressMonitor progress) throws Exception {
		// let's see which asset index is needed by this version
		String index = version.getAssetsIndexName();
		MCLauncherAPI.log.fine("Installing asset index ".concat(index));
		File indexDest = new File(this.indexesDir, index + ".json");
		String indexDownloadURL = RESOURCES_INDEX_URL + index + ".json";
		// download this asset index
		if (!indexDest.exists() || (indexDest.length() == 0)) {
			FileUtils.downloadFileWithProgress(indexDownloadURL, indexDest, progress);
		}
		// parse it from JSON
		JSONObject jsonAssets = (JSONObject) JSONValue.parse(new FileReader(indexDest));
		AssetIndex assets = new AssetIndex(index, jsonAssets);
		// and download individual assets inside it
		this.downloadAssetList(assets, progress);
		MCLauncherAPI.log.fine("Finished installing asset index ".concat(index));
	}

}
