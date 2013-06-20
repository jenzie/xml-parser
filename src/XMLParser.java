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
import java.util.ArrayList;
import java.util.Scanner;

/**
 * XMLParser.
 * Main class for parsing the .xml files, calling methods to perform the
 * approximation, and writing the output .xml files.
 */
public class XMLParser {
	private static final String FILE_EXTENSION = ".xml";
	private String inputXML, outputXML;
	private Scanner input;
	private PrintWriter out;
	private static final Boolean DEBUG = false; // print debug messages if true

	/**
	 * Constructor.
	 * @param inputXML input file
	 * @param outputXML output file
	 * @throws InterruptedException
	 */
	public XMLParser(String inputXML, String outputXML)
												throws InterruptedException {
		this.inputXML = inputXML;
		this.outputXML = outputXML;

		try {
			input = new Scanner(new File(inputXML));
		} catch (FileNotFoundException fnfe) {
			System.err.println(
				"Usage: java xml-parser.java INPUT.xml OUTPUT.xml");
			System.exit(0);
		}

		// run the program
		run();
	}

	/**
	 * Main function.
	 * Checks the number of arguments and extensions of the files given in the
	 * arguments before proceeding with the program.
	 * @param args input/output file directories and names
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		String fileExtension1, fileExtension2;
		boolean status = false;

		// check arguments
		if(args.length == 2) {
			fileExtension1 =
				args[0].substring(args[0].length() - FILE_EXTENSION.length());
			fileExtension2 =
				args[1].substring(args[1].length() - FILE_EXTENSION.length());
			if(fileExtension1.equals(FILE_EXTENSION) &&
					fileExtension2.equals(FILE_EXTENSION))
				status = true;
		}

		// did not pass check
		if(!status) {
			System.err.println(
				"Usage: java xml-parser.java INPUT.xml OUTPUT.xml");
			System.exit(0);
		}

		// passed check, proceed
		new XMLParser(args[0], args[1]);
	}

	/**
	 * Runs the program to parse the input file, call approximation function,
	 * and call function to write the output.
	 * @throws InterruptedException
	 */
	private void run() throws InterruptedException {
		ArrayList<String> file = new ArrayList<String>();
		ArrayList[] results;

		while(input.hasNextLine())
			file.add(input.nextLine());

		// get results, pausing between file writes
		results = performApproximation(file, "double", "float");
		if(DEBUG)
			System.out.println("\nWriting results to output directory.");

		//Thread.sleep(1000);
		getOutFile(results[0], 1);
		getOutFile(results[1], 2);
		getOutFile(results[2], 3);

		if(DEBUG)
			System.out.println("Writing complete.");
	}

	/**
	 * Perform approximation on input using the available strategies.
	 *
	 * @param file input to perform approximation.
	 * @param find text to find.
	 * @param replace text to replace.
	 * @return results of approximation.
	 */
	private ArrayList[] performApproximation(
			ArrayList<String> file, String find, String replace) {

		ArrayList[] results = new ArrayList[3];

		// create all strategy objects
		ApproximationStrategy naive = new NaiveStrategy();
		ApproximationStrategy random = new RandomStrategy();
		ApproximationStrategy loop = new LoopStrategy();

		if(DEBUG)
			System.out.println(
					"\nPerforming approximation for " + inputXML + ".\n");

		// store approximation results
		results[0] = naive.approximate(file, find, replace);
		results[1] = random.approximate(file, find, replace);
		results[2] = loop.approximate(file, find, replace);

		// print results if in debug mode
		if(DEBUG) {
			if(naive.getCount() == 1)
				System.out.println("There was " + naive.getCount() +
					" variable " + "changed from " +
						find + " to " + replace + ".");
			else
				System.out.println("There were " + naive.getCount() +
					" variables " + "changed from " +
						find + " to " + replace + ".");

			if(random.getCount() == 1)
				System.out.println("There was " + random.getCount() +
					" variable " + "changed from " +
						find + " to " + replace + ".");
			else
				System.out.println("There were " + random.getCount() +
					" variables " + "changed from " +
						find + " to " + replace + ".");

			if(loop.getCount() == 1)
				System.out.println("There was " + loop.getCount() +
					" variable " + "changed from " +
						find + " to " + replace + ".");
			else
				System.out.println("There were " + loop.getCount() +
					" variables " + "changed from " +
						find + " to " + replace + ".");
		}

		return results;
	}

	/**
	 * Write the results to the output file supplied in the arguments when
	 * the program was run.
	 * @param result output to write to file.
	 */
	private void getOutFile(ArrayList<String> result, int strategy_id) {
		// produce unique file names
		//String filename =
		//	outputXML.substring(0, outputXML.length() - FILE_EXTENSION.length());
		//SimpleDateFormat dateFormat =
		//	new SimpleDateFormat("MMddHHmmss");
		//filename += "_" + dateFormat.format(new Date()) + FILE_EXTENSION;
		String filename = outputXML;
		String[] file = filename.split("output");
		filename = file[0] + "output_" + strategy_id + file[1];

		// create file writer
		try {
			out = new PrintWriter(new FileWriter(new File(filename)));
		} catch (IOException ioe) {
			System.err.println("IOException: " +
					"Could not create print writer for " + filename);
		}

		// write to file
		out.flush();
		for(String line : result)
			out.write(line + "\r\n");
		out.flush();
		out.close();
	}
}
