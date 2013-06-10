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
		Map indices = this.countTotal(file, find);
		Random generator = new Random();

		return result;
	}

	public Map countTotal(ArrayList<String> file, String find) {
		Map indices = new HashMap();
		String[] tempPiece, tempLine;
		String tempResult;

		// reset count
		this.totalCount = 0;

		// n = line
		for(int n = 0; n < file.size(); n++) {
			tempLine = file.get(n).split("<");
			tempResult = tempLine[0];

			for(int i = 1; i < tempLine.length; i++) {
				tempLine[i] = "<" + tempLine[i];
				tempPiece = tempLine[i].split(">");

				// j = piece
				for(int j = 0; j < tempPiece.length; j++) {
					if(tempPiece[j].contains("<"))
						tempPiece[j] = tempPiece[j] + ">";

					if(tempPiece[j].equals(find)) {
						this.totalCount++;
						indices.put(n, j);
					}
				}
			}
		}
		System.out.println("total: " + totalCount);
		return indices;
	}
}
