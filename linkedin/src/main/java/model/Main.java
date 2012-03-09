package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String[] keys = readAPIKeys();
		System.out.println(keys[0] + " " + keys[1]);

	}

	public static String[] readAPIKeys() {
		String[] keys = new String[2];
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(File.separator
					+ "linkedin.properties"));
			keys[0] = properties.getProperty("API_KEY");
			keys[1] = properties.getProperty("API_SECRET");
		} catch (FileNotFoundException e) {
			System.err.println("Properties file not found!");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Could not read API-key properly!");
			e.printStackTrace();
		}

		return keys;
	}

}
