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
		ArrayList<String> result = file;
		String[] tempLine;
		String tempResult = "";

		for(String line : file) {
			tempLine = line.split("<");
			for(String part : tempLine) {
				if(part.equals(find))
					part = replace;
				tempResult += part;
			}
			line = tempResult;
		}

		return result;
	}
}
