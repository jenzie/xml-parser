package strategy;

/**
 * @author: Jenny Zhen; jenny.zhen@rit.edu
 * date: 06.09.2013
 * language: Java
 * project: xml-parser
 */

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
	public String[] approximate(String[] file, String find, String replace) {
		String[] result = file;
		return result;
	}
}
