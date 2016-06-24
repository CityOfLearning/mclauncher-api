package sk.tomsik68.mclauncher.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;

import org.tukaani.xz.XZInputStream;

import com.google.common.io.Files;

import sk.tomsik68.mclauncher.api.ui.IProgressMonitor;

public final class MCFileUtils {

	private static final String PACK_NAME = ".pack.xz";

	public static void copyFile(File from, File to) throws Exception {
		createFileSafely(to);
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(from));
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(to));
		byte[] block;
		while (bis.available() > 0) {
			block = new byte[8192];
			final int readNow = bis.read(block);
			bos.write(block, 0, readNow);
		}
		bos.flush();
		bos.close();
		bis.close();
	}

	public static void createFileSafely(File file) throws Exception {
		File parentFile = new File(file.getParent());
		if (!parentFile.exists()) {
			if (!parentFile.mkdirs()) {
				throw new IOException("Unable to create parent file: " + file.getParent());
			}
		}
		if (file.exists()) {
			if (!file.delete()) {
				if (!file.renameTo(new File(file.getParentFile(), "old-" + file.getName()))) {
					throw new IOException(
							"Couldn't delete '".concat(file.getName()).concat("'").concat(" Try Signing in again"));
				}
			}
		}
		if (!file.createNewFile()) {
			throw new IOException("Couldn't create '".concat(file.getAbsolutePath()).concat("'"));
		}
	}

	public static void downloadFileWithProgress(String url, File dest, IProgressMonitor progress) {
		try {
			String md5 = null;
			if (dest.exists()) {
				md5 = getMD5(dest);
			}

			URL u = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) u.openConnection();
			if (md5 != null) {
				connection.setRequestProperty("If-None-Match", md5);
			}
			connection.connect();

			// local copy is up-to-date
			if (connection.getResponseCode() == 304) {
				return;
			}
			createFileSafely(dest);

			BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(dest));

			final int len = connection.getContentLength();
			if (progress != null) {
				progress.setMax(len);
			}

			int readBytes = 0;
			byte[] block;

			while (readBytes < len) {
				block = new byte[8192];
				int readNow = in.read(block);
				if (readNow > 0) {
					out.write(block, 0, readNow);
				}
				if (progress != null) {
					progress.setProgress(readBytes);
				}
				readBytes += readNow;
			}
			out.flush();
			out.close();
			in.close();
		} catch (FileNotFoundException fnf) {
			// most likely we have a file not found error from forge... lets try
			// to grab the pack
			if (dest.getName().contains("forge-1.8.9")) {
				// this assumes we are downloading forge jar
				downloadFileWithProgress(
						"http://files.minecraftforge.net/maven/net/minecraftforge/forge/1.8.9-11.15.1.1890-1.8.9/forge-1.8.9-11.15.1.1890-1.8.9-universal.jar",
						dest, progress);
			} else {
				downloadPackedFileWithProgress(url + PACK_NAME, dest, progress);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	public static void downloadPackedFileWithProgress(String url, File dest, IProgressMonitor progress) {
		File packFile = new File(dest.getParentFile(), dest.getName() + PACK_NAME);
		try {
			String md5 = null;
			if (packFile.exists()) {
				md5 = getMD5(packFile);
			}

			URL u = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) u.openConnection();
			if (md5 != null) {
				connection.setRequestProperty("If-None-Match", md5);
			}
			connection.connect();

			// local copy is up-to-date
			if (connection.getResponseCode() == 304) {
				return;
			}
			createFileSafely(packFile);

			BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(packFile));

			final int len = connection.getContentLength();
			if (progress != null) {
				progress.setMax(len);
			}

			int readBytes = 0;
			byte[] block;

			while (readBytes < len) {
				block = new byte[8192];
				int readNow = in.read(block);
				if (readNow > 0) {
					out.write(block, 0, readNow);
				}
				if (progress != null) {
					progress.setProgress(readBytes);
				}
				readBytes += readNow;
			}
			out.flush();
			out.close();
			in.close();
			try {
				progress.setStatus(String.format("Unpacking packed file %s", dest.getName()));
				unpackLibrary(dest, Files.toByteArray(packFile));
				progress.setStatus(String.format("Successfully unpacked packed file %s", dest.getName()));
				packFile.delete();
			} catch (OutOfMemoryError oom) {
				oom.printStackTrace();
				// artifact.setMemo("Out of Memory: Try restarting installer
				// with JVM Argument: -Xmx1G");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException fnf) {
			fnf.printStackTrace();
			packFile.delete();
			dest.delete();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	// this method is copied from original launcher, as the MD5-ing function
	// needs to be the same
	public static String getMD5(File file) throws Exception {
		DigestInputStream stream = null;
		try {
			stream = new DigestInputStream(new FileInputStream(file), MessageDigest.getInstance("MD5"));
			byte[] buffer = new byte[65536];

			int read = stream.read(buffer);
			while (read >= 1) {
				read = stream.read(buffer);
			}
		} catch (Exception ignored) {
			return null;
		} finally {
			stream.close();
		}

		return String.format("%1$032x", new BigInteger(1, stream.getMessageDigest().digest()));
	}

	public static byte[] readFully(InputStream stream) throws IOException {
		byte[] data = new byte[4096];
		ByteArrayOutputStream entryBuffer = new ByteArrayOutputStream();
		int len;
		do {
			len = stream.read(data);
			if (len > 0) {
				entryBuffer.write(data, 0, len);
			}
		} while (len != -1);

		return entryBuffer.toByteArray();
	}

	public static void unpackLibrary(File output, byte[] data) throws IOException {
		if (output.exists()) {
			output.delete();
		}

		byte[] decompressed = MCFileUtils.readFully(new XZInputStream(new ByteArrayInputStream(data)));

		// Snag the checksum signature
		String end = new String(decompressed, decompressed.length - 4, 4);
		if (!end.equals("SIGN")) {
			System.out.println("Unpacking failed, signature missing " + end);
			return;
		}

		int x = decompressed.length;
		int len = ((decompressed[x - 8] & 0xFF)) | ((decompressed[x - 7] & 0xFF) << 8)
				| ((decompressed[x - 6] & 0xFF) << 16) | ((decompressed[x - 5] & 0xFF) << 24);

		File temp = File.createTempFile("art", ".pack");
		System.out.println("  Signed");
		System.out.println("  Checksum Length: " + len);
		System.out.println("  Total Length:    " + (decompressed.length - len - 8));
		System.out.println("  Temp File:       " + temp.getAbsolutePath());

		byte[] checksums = Arrays.copyOfRange(decompressed, decompressed.length - len - 8, decompressed.length - 8);

		// As Pack200 copies all the data from the input, this creates duplicate
		// data in memory.
		// Which on some systems triggers a OutOfMemoryError, to counter this,
		// we write the data
		// to a temporary file, force GC to run {I know, eww} and then unpack.
		// This is a tradeoff of disk IO for memory.
		// Should help mac users who have a lower standard max memory then the
		// rest of the world (-.-)
		OutputStream out = new FileOutputStream(temp);
		out.write(decompressed, 0, decompressed.length - len - 8);
		out.close();
		decompressed = null;
		data = null;
		System.gc();

		FileOutputStream jarBytes = new FileOutputStream(output);
		JarOutputStream jos = new JarOutputStream(jarBytes);

		Pack200.newUnpacker().unpack(temp, jos);

		JarEntry checksumsFile = new JarEntry("checksums.sha1");
		checksumsFile.setTime(0);
		jos.putNextEntry(checksumsFile);
		jos.write(checksums);
		jos.closeEntry();

		jos.close();
		jarBytes.close();
		temp.delete();
	}

	public static void writeFile(File dest, String str) throws Exception {
		createFileSafely(dest);
		FileWriter fw = new FileWriter(dest);
		fw.write(str);
		fw.flush();
		fw.close();
	}
}
