/**
 * @author: Jenny Zhen; jenny.zhen@rit.edu
 * date: 06.09.2013
 * language: Java
 * project: xml-parser
 */

import strategy.ApproximationStrategy;
import strategy.LoopStrategy;
import strategy.NaiveStrategy;
import strategy.RandomStrategy;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class XMLParser {
	private static final String FILE_EXTENSION = ".xml";
	private String XMLFile;
	private Scanner input;
	private PrintWriter out;

	public XMLParser(String XMLFile) throws InterruptedException {
		this.XMLFile = XMLFile;

		try {
			input = new Scanner(new File("input", XMLFile)); // directory, file
		} catch (FileNotFoundException e) {
			System.err.println("Usage: java XMLParser.java FILENAME.xml");
			System.exit(0);
		}
		run();
	}

	public static void main(String[] args) throws InterruptedException {
		String fileExtension;
		boolean status = false;

		if(args.length != 0) {
			fileExtension =
				args[0].substring(args[0].length() - FILE_EXTENSION.length());
			if(fileExtension.equals(FILE_EXTENSION))
				status = true;
		}

		if(!status) {
			System.err.println("Usage: java XMLParser.java FILENAME.xml");
			System.exit(0);
		}

		new XMLParser(args[0]);
	}

	private void run() throws InterruptedException {
		ArrayList<String> file = new ArrayList<String>();
		ArrayList<String>[] results;

		while(input.hasNextLine())
			file.add(input.nextLine());

		// get results, pausing between file writes
		results = performApproximation(file, "double", "float");
		System.out.println("\nWriting results to output directory.");

		Thread.sleep(1000);
		getOutFile(results[0]);
		Thread.sleep(1000);
		getOutFile(results[1]);
		Thread.sleep(1000);
		//getOutFile(results[2]);

		System.out.println("Writing complete.");
	}

	private ArrayList<String>[] performApproximation(
			ArrayList<String> file, String find, String replace) {

		ArrayList<String>[] results = new ArrayList[3];

		// create all strategy objects
		ApproximationStrategy naive = new NaiveStrategy();
		ApproximationStrategy random = new RandomStrategy();
		ApproximationStrategy loop = new LoopStrategy();

		results[0] = naive.approximate(file, find, replace);
		System.out.println("There were " + naive.getCount() + " variables " +
				"changed from " + find + " to " + replace + ".");

		results[1] = random.approximate(file, find, replace);
		System.out.println("There were " + random.getCount() + " variables " +
				"changed from " + find + " to " + replace + ".");

		results[2] = loop.approximate(file, find, replace);
		System.out.println("There were " + loop.getCount() + " variables " +
				"changed from " + find + " to " + replace + ".");

		return results;
	}

	private void getOutFile(ArrayList<String> result) {
		// produce unique file names
		String filename =
				XMLFile.substring(0, XMLFile.length() - FILE_EXTENSION.length());
		SimpleDateFormat dateFormat =
				new SimpleDateFormat("MMddHHmmss");
		filename += "_" + dateFormat.format(new Date()) + FILE_EXTENSION;

		try {
			out = new PrintWriter(new FileWriter(new File("output", filename)));
		} catch (IOException ioe) {
			System.err.println("IOException: " +
					"Could not create print writer for /results/" + XMLFile);
		}
		out.flush();
		for(int i = 0; i < result.size(); i++)
			out.write(result.get(i) + "\r\n");
		out.flush();
		out.close();
	}
}
