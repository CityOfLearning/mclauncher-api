package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import java.util.ArrayList;
import java.util.zip.ZipEntry;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import sk.tomsik68.mclauncher.util.IExtractRules;

/**
 * Library extraction filter
 */
final class LibraryExtractRules implements IExtractRules {
	private ArrayList<String> exclude = new ArrayList<String>();

	public LibraryExtractRules(JSONObject object) {
		JSONArray excludeArray = (JSONArray) object.get("exclude");
		for (Object obj : excludeArray) {
			this.exclude.add(obj.toString());
		}
	}

	/**
	 *
	 * @param entry
	 *            Entry that is being thinked about right now
	 * @return True if entry may be extracted, otherwise false
	 */
	@Override
	public boolean accepts(ZipEntry entry) {
		String path = entry.getName();
		if ((this.exclude != null) && !this.exclude.isEmpty()) {
			for (String p : this.exclude) {
				if (path.startsWith(p)) {
					return false;
				}

			}
		}
		return true;
	}

}
