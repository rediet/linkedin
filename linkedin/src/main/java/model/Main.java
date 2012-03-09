package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Main {

	// TODO: replace with current API-key and secret
	private static String API_KEY = null;
	private static String API_SECRET = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		readAPISettings();
		System.out.println(API_KEY + " " + API_SECRET);

	}

	public static void readAPISettings() {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(File.separator
					+ "linkedin.properties"));
			API_KEY = properties.getProperty("API_KEY");
			API_SECRET = properties.getProperty("API_SECRET");
		} catch (FileNotFoundException e) {
			System.err.println("Properties file not found!");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Could not read API-key properly!");
			e.printStackTrace();
		}
	}

}
