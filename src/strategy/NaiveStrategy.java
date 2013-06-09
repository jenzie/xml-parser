/**
 * @author: Jenny Zhen; jenny.zhen@rit.edu
 * date: 06.09.2013
 * language: Java
 * project: xml-parser
 */

package strategy;

import java.util.ArrayList;

public class NaiveStrategy implements ApproximationStrategy {
	private int count;

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
		String[] tempPiece, tempLine;
		String tempResult;

		for(int n = 0; n < file.size(); n++) {
			tempLine = file.get(n).split("<");
			tempResult = tempLine[0];

			for(int i = 1; i < tempLine.length; i++) {
				tempLine[i] = "<" + tempLine[i];

				tempPiece = tempLine[i].split(">");
				for(int j = 0; j < tempPiece.length; j++) {
					if(tempPiece[j].contains("<"))
						tempPiece[j] = tempPiece[j] + ">";

					if(tempPiece[j].equals(find)) {
						tempPiece[j] = replace;
						this.count++;
					}
					System.out.println(tempPiece[j]);
					tempResult += tempPiece[j];
				}
			}
			result.add(tempResult);
		}
		return result;
	}
}
