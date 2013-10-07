package fr.labri.scanlib;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Map;
import java.util.Set;

import org.arabidopsis.ahocorasick.AhoCorasick;
import org.arabidopsis.ahocorasick.OutputResult;

import fr.labri.scanlib.database.DB;

public class ScanLib {

	private static ScanLib instance = null;

	private static AhoCorasick tree = null;

	private static Map<String, String> database = new HashMap<String, String>();
	
	private ScanLib() {
		tree = new AhoCorasick();
		database.clear();
		tree.prepare();
	}
	
	private ScanLib(String file) {
		System.out.println("Trying to open "+new File(file).getPath());
		tree = new AhoCorasick();
		database.clear();
		database = DB.readContentIndex(new File(file).getPath());
		for (String kw : database.keySet()) {
			tree.add(kw);
		}
		tree.prepare();
	}

	/**
	 * Load a database of library from a given xml File
	 */
	public static ScanLib buildFromFile(String file) {
		if (null == instance) { // Premier appel
			instance = new ScanLib(file);
		}
		return instance;
	}
	
	/**
	 * Load a database of library from a given xml File
	 */
	public static Set<String> getDatabaseContent() {
		return new HashSet<String>(database.values());
	}
	
	/**
	 * Get the current instance of ScanLib
	 */
	public static ScanLib getEmptyInstance() {
		if (null == instance) { // Premier appel
			instance = new ScanLib();
		}
		return instance;
	}
	
	/**
	 * Get the current instance of ScanLib
	 */
	public static ScanLib getInstance() {
		if (null == instance) { // Premier appel
			instance = new ScanLib("scanlib-data.xml");
		}
		return instance;
	}

	/**
	 * Runtime addition of data in the ScanLib database
	 * @param keyword the associated keyword
	 * @param library the name of the library 
	 */
	public static void add(String keyword, String library) {
		database.put(keyword, library);
		tree = new AhoCorasick();
		for (String kw : database.keySet()) {
			tree.add(kw);
		}
		tree.prepare();
	}

	/**
	 * Runtime deletion of data in the ScanLib database
	 */
	public static void remove(String keyword) {
		database.remove(keyword);
	}

	/**
	 * Save the current ScanLib database in a XML File
	 */
	public static void saveDB(String file) {
		DB.saveContent(database, file);
	}

	/**
	 * Runtime addition of data in the ScanLib database
	 */
	public static Set<String> getKeywords(String library)
			throws LibraryNotFoundException {
		if (database.values().contains(library)) {
			TreeSet<String> kw = new TreeSet<String>();
			for (String k : database.keySet()) {
				if (database.get(k).equals(library))
					kw.add(k);
			}
			return kw;
		} else {
			throw new LibraryNotFoundException("Library " + library
					+ " does not exist in current ScanLib database");
		}
	}
	
	/**
	 * Returns a collection of libraries used by a project
	 */
	public static Set<String> computeLibraries(String dir) {

		TreeSet<String> libraries = new TreeSet<String>();
		javaxt.io.Directory directory = new javaxt.io.Directory(dir);
		javaxt.io.File[] files = directory.getFiles("*.java", true);
		for (javaxt.io.File file : files) {
			if (!file.isHidden()) {
				try {
					byte[] encoded;
					encoded = Files.readAllBytes(Paths.get(file.getPath()
							+ file.getName()));
					libraries.addAll(instance.searchLibraries((
							Charset.forName("UTF-8")
									.decode(ByteBuffer.wrap(encoded))
									.toString())));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return libraries;
	}
	
	/**
	 * Returns true if the directory contains code that use a given library
	 */
	public static boolean hasLibrary(String dir, String lib) {
		return computeLibraries(dir).contains(lib);
	}


	/**
	 * Get a list of keywords that match a given targeted keyword
	 * @param keyword
	 * @return
	 */
	public static Set<String> searchKeyword(String keyword) {
		Set<String> res = new HashSet<String>();
		for (String k : database.keySet()) {
			if (k.contains(keyword)) {
				res.add(database.get(k) + "::" + k);
			}
		}
		return res;
	}

	/**
	 * Get a list of libraries that match a given targeted library
	 * @param keyword
	 * @return
	 */
	private static Set<String> searchLibraries(String text) {
		Set<String> resultats = new HashSet<String>();
		for (OutputResult res : tree.completeSearch(text, false, false)) {
			resultats.add(database.get(res.getOutput()));
		}
		return resultats;
	}

}
