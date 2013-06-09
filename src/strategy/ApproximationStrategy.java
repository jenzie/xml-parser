package strategy;

import java.util.ArrayList;

/**
 * @author: Jenny Zhen; jenny.zhen@rit.edu
 * date: 06.09.2013
 * language: Java
 * project: xml-parser
 */

public interface ApproximationStrategy {
	/**
	 * @return the number of variables changed using the strategy.
	 */
	public int getCount();

	/**
	 * Perform approximation based on the strategy's specific algorithm.
	 * @param file current file to approximate.
	 * @param find data type to find
	 * @param replace data type to replace the original/find with
	 * @return the root node of the modified tree, which is necessary for
	 * comparing results of multiple strategies.
	 */
	public ArrayList<String> approximate(
			ArrayList<String> file, String find, String replace);
}
