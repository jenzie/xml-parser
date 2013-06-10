/**
 * @author: Jenny Zhen; jenny.zhen@rit.edu
 * date: 06.09.2013
 * language: Java
 * project: xml-parser
 */

package strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RandomStrategy implements ApproximationStrategy {
	private ArrayList<ArrayList<String>> parsedFile;
	private int count;
	private int totalCount;

	@Override
	/**
	 * @return the number of variables changed using the strategy.
	 */
	public int getCount() {
		return this.count;
	}

	@Override
	/**
	 * Performs the approximation based on the strategy to replace all
	 * existing variables.
	 *
	 * @param file current file to approximate.
	 * @param find data type to find
	 * @param replace data type to replace the original/find with
	 * @return the root node of the modified tree, which is necessary for
	 * comparing results of multiple strategies.
	 */
	public ArrayList<String> approximate(
			ArrayList<String> file, String find, String replace) {

		ArrayList<String> result = new ArrayList<String>();
		ArrayList<String> indices = this.countTotal(file, find);

		// number of variables to replace
		int ncount = this.totalCount/2;

		Random generator = new Random();
		String[] tempIndices;
		int index, line, piece;
		String tempLine;
		this.count = 0; // reset the count

		for(int i = 0; i < ncount; i++) {
			// generate positive indices only
			index = generator.nextInt(Integer.MAX_VALUE) + 1;
			index = index % indices.size();

			tempIndices = indices.get(index).split(",");
			line = Integer.parseInt(tempIndices[0]);
			piece = Integer.parseInt(tempIndices[1]);
			this.parsedFile.get(line).set(piece, replace);
			this.count++;
			indices.remove(index);
		}
		for(ArrayList<String> l : parsedFile) {
			tempLine = "";
			for(String p : l)
				tempLine += p;
			result.add(tempLine);
		}

		return result;
	}

	public ArrayList<String> countTotal(ArrayList<String> file, String find) {
		this.parsedFile = new ArrayList<ArrayList<String>>();
		ArrayList<String> indices = new ArrayList<String>();
		String[] tempPiece, tempLine;
		String tempResult;
		int index;

		// reset count
		this.totalCount = 0;

		// n = line
		for(int n = 0; n < file.size(); n++) {
			tempLine = file.get(n).split("<");
			ArrayList<String> entry = new ArrayList<String>();
			index = 0; // index of piece in the parsed file

			if(tempLine[0] != null)
				entry.add(tempLine[0]);

			for(int i = 1; i < tempLine.length; i++) {
				tempLine[i] = "<" + tempLine[i];
				tempPiece = tempLine[i].split(">");

				// j = piece
				for(int j = 0; j < tempPiece.length; j++) {
					if(tempPiece[j].contains("<"))
						tempPiece[j] = tempPiece[j] + ">";

					index++;
					entry.add(tempPiece[j]);
					if(tempPiece[j].equals(find)) {
						this.totalCount++;
						indices.add(n + "," + index);
					}
				}
			}
			this.parsedFile.add(entry);
		}
		return indices;
	}
}
