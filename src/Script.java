import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author: Jenny Zhen; jenny.zhen@rit.edu
 * date: 06.11.2013
 * language: Java
 * project: xml-parser
 */

public class Script {
	public static void main(String[] args) {
		String current = System.getProperty("user.dir") + "\\input";
		File folder = new File(current);

		traverse(folder);
	}

	private static void traverse(File fileObject) {
		if(fileObject.isDirectory()) {
			File[] allFileObjects = fileObject.listFiles();
			for(File file : allFileObjects) {
				traverse(file);
			}
		}
		else if(fileObject.isFile()) {
			// Run a java app in a separate system process
			Process process;

			try {
				process = Runtime.getRuntime().exec("java -jar xml-parser.jar "
					+ fileObject.getCanonicalPath() + " " + fileObject.getCanonicalPath());
				System.out.println(fileObject.getCanonicalFile());
				// Then retrieve the process output
				InputStream in = process.getInputStream();
				InputStream err = process.getErrorStream();
				System.out.println(in);
			} catch (IOException e) {
				System.err.println("Error: Could not run xml-parser.jar.");
				System.exit(0);
			}
		}
	}
}
