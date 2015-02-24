package sk.tomsik68.mclauncher.api.mods;

import sk.tomsik68.mclauncher.impl.versions.mcdownload.Library;

import java.io.File;
import java.util.List;

/**
 * Describes current set of mods that will be injected into JAR file
 */
public interface IModdingProfile {
    /**
     * Returns <code>separator</code>-separated list of absolute paths to JAR files that will be injected before libraries
     * @param separator
     * @return Empty string for none. <b>Don't return null or you'll get an error</b>
     */
    public String injectBeforeLibs(String separator);

    /**
     * Returns <code>separator</code>-separated list of absolute paths to JAR files that will be injected after libraries
     * @param separator
     * @return Empty string for none. <b>Don't return null or you'll get an error</b>
     */
    public String injectAfterLibs(String separator);

    /**
     *
     * @param library
     * @return True if specified library may be injected along with all vanilla libraries
     */
    public boolean isLibraryAllowed(Library library);

    /**
     *
     * @return Custom game JAR file to use. If you don't want to change it, return null
     */
    public String getCustomGameJar();

    /**
     *
     * @return Name of main class to use while launching Minecraft.
     */
    public String getMainClass();

    /**
     * Minecraft arguments are arguments that will be available in minecraft's main method.
     * These contain mostly user information, but also assets path, saves path etc, which might be useful...
     * @param minecraftArguments
     * @return Array of string which is formatted in the same way as the input array. If you don't want to make any changes, return null or <code>minecraftArguments</code>
     */
    public String[] changeMinecraftArguments(String[] minecraftArguments);

    /**
     *
     * @return List of parameters that will be appended after all parameters to launch the JAR. These most likely won't influence the launching process, but you may find it useful...
     */
    public List<String> getLastParameters();
}
