package se.csn.ark.common.util;

import java.io.IOException;
import java.io.InputStream;


/**
 * Properties för ett bygge.
 *
 * @author Joakim Olsson
 * @since 20050203
 * @version 0.1 skapad
 *
 */
public class BuildProperties extends java.util.Properties {
	private static final String POSTFIX = ".properties";

	/**
	 * @param name namn på property-filen
	 */
	public BuildProperties(String name) {
		init(name);
	}




	/**
	 * @param stream property-filen
	 */
	public BuildProperties(InputStream stream) {
		init(stream);
	}

	/**
	 * Läser in alla properties
	 * @param name namn på property-filen
	 */
	private void init(String name) {
		try {
			InputStream in;

			in = BuildProperties.class.getClassLoader().getResourceAsStream(name
				                                                            + POSTFIX);

			if (in == null) {
				in = ClassLoader.getSystemResourceAsStream(name + POSTFIX);
			}

			load(in);
		} catch (Exception e) {
			System.err.println("se.csn.ark.common.util.BuildProperties.init()"
			                   + ", kan ej hitta filen " + name + POSTFIX);
			e.printStackTrace();
		}
	}




	/**
	 * Läser in alla properties
	 * @param stream property-filen
	 */
	private void init(InputStream stream) {
		try {
			load(stream);
		} catch (IOException e) {
			System.err.println("se.csn.ark.common.util.BuildProperties.init()"
			                   + ", kan ej läsa strömmen");
			e.printStackTrace();
		}
	}




	/**
	 * @return build.version
	 */
	public String getVersion() {
		return getProperty("build.version");
	}




	/**
	 * @return build.time
	 */
	public String getTime() {
		return getProperty("build.time");
	}




	/**
	 * @return build.name
	 */
	public String getName() {
		return getProperty("build.name");
	}




	/**
	 * @return build.buildname
	 */
	public String getBuildName() {
		return getProperty("build.buildname");
	}




	/**
	 * @param nl radbryt som ska användas
	 * @return alla build-properties
	 */
	public String getLogString(String nl) {
		StringBuffer str = new StringBuffer("Delsystem " + getName() + nl);

		str.append("  namn vid bygge: " + getBuildName() + nl);
		str.append("  version: " + getVersion() + nl);
		str.append("  tidpunkt för bygge: " + getTime() + nl);

		return str.toString();
	}




	/**
	 * @return alla build-properties
	 */
	public String getLogString() {
		return getLogString("\n");
	}
}