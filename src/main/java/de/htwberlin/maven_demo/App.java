package de.htwberlin.maven_demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Hello world!
 *
 */
public class App {

	private static final Logger LOGGER = LogManager.getLogger(App.class);

	public static void main(String[] args) {
		LOGGER.debug("main started");
		foobar();
		bar();
		LOGGER.debug("main finished");
	}

	private static void foobar() {
		System.out.println("Hallo Welt!");
	}

	private static void bar() {
		System.out.println("Hello World!");
	}
}
