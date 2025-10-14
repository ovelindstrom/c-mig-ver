package se.csn.ark.common.io;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.StringTokenizer;


/**
 * Looks for a file (given by name) in a given path-string (for example the classpath). 
 * Does NOT look inside a jar or zip file for the specified file. Returns the file if found, 
 * otherwise null.
 *
 * @deprecated Använd inte denna längre. Använd <b>se.csn.ark.common.util.Properties</b> istället.
 * @author K-G Sjöström
 * @since 20040920
 * @version 1 skapad
 * @see se.csn.ark.common.util.Properties
 */
public class FileFinder {
	private File[] files; // for jdk1.2
	private String[] filenames; // for jdk1.1
	private File foundFile; // for jdk 1.1
	private boolean done;
	private int i;

	/**
	 * @param filename fil att hitta
	 * @param concatenatedPath path att söka i 
	 * @return eftersökt fil
	 * @throws FileNotFoundException filen finns ej
	 */
	public File findFile(String filename, String concatenatedPath)
	              throws FileNotFoundException {
		// filename and path required input parameters
		if ((filename == null) | (concatenatedPath == null)) {
			return null;
		} else {
			done = false;

			// Returns null if file is not found, otherwise a File object
			StringTokenizer st =
				new StringTokenizer(
				                    concatenatedPath,
				                    System.getProperty("path.separator"));

			while (st.hasMoreTokens() & !done) {
				// loop över alla directories i pathen:
				File searchDirectory = new File(st.nextToken());

				if (!searchDirectory.exists() | searchDirectory.isFile()) {
					// Does not exist or is not a directory, skip	
				} else {
					// System.out.println("\nSearching " + searchDirectory.getName() + " ...");
					// For jdk 1.2:
					// files = searchDirectory.listFiles();
					// for jdk 1.1:
					filenames = searchDirectory.list();

					// loop över alla filer i aktuellt bibliotek:
					//  jdk 1.2:   for (i=0 ; i  < files.length & !done; i++ ) {
					for (i = 0; (i < filenames.length) & !done; i++) {
						try {
							foundFile = new File(searchDirectory, filenames[i]); // for jdk1.1
						} catch (Exception e) {
							e.printStackTrace();
						}

						//  jdk1.2: 	if (files[i].isFile() 
                        //                  & filename.equalsIgnoreCase(files[i].getName())) {
						if (
						    foundFile.isFile()
						    & filename.equalsIgnoreCase(filenames[i])) {
							// System.out.println("Found " + files[i].getName());
							// System.out.println("Found " + foundFile.getName());
							done = true;
						}
					}

					// for i
				}

				// else
			}

			// while hasmoretokens
			if (done) {
				// jdk1.2	return files[i-1]; // the iteration has increased 
                                               // the index once after the file was found, 
                                               // we have to decrease by one

                // the iteration has increased the index once after the file was found, 
                // we have to decrease by one
				return foundFile; 
			} else {
				throw new FileNotFoundException("Could not find file "
				                                + filename + " in path "
				                                + concatenatedPath);
			}

			// if done
		}

		// if else
	}

	// method findFile
} // class FileFinder
