/**
 * @author: Jenny Zhen; jenny.zhen@rit.edu
 * date: 06.09.2013
 * language: Java
 * project: xml-parser
 */

package strategy;

import java.util.ArrayList;
import java.util.Map;

public class LoopStrategy implements ApproximationStrategy {
	private int count;
	private ArrayList<String> openLoop, closeLoop;

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

		// populate with open/close loop tags
		this.populateLoopTags();

		ArrayList<String> loops = new ArrayList<String>();
		ArrayList<String> result = new ArrayList<String>();
		String[] tempPiece, tempLine;
		String tempResult;

		for(int n = 0; n < file.size(); n++) {
			tempLine = file.get(n).split("<");
			tempResult = tempLine[0];
			if(tempResult != null)
				checkTags(loops, tempResult);

			for(int i = 1; i < tempLine.length; i++) {
				tempLine[i] = "<" + tempLine[i];
				tempPiece = tempLine[i].split(">");

				for(int j = 0; j < tempPiece.length; j++) {
					if(tempPiece[j].contains("<"))
						tempPiece[j] = tempPiece[j] + ">";

					if(!checkTags(loops, tempPiece[j])) {
						if(loops.size() > 0) {
							if(tempPiece[j].equals(find)) {
								tempPiece[j] = replace;
								this.count++;
							}
						}
					}
					tempResult += tempPiece[j];
				}
			}
			result.add(tempResult);
		}
		return result;
	}

	private void populateLoopTags() {
		openLoop = new ArrayList<String>();
		closeLoop = new ArrayList<String>();

		openLoop.add("<for>");
		closeLoop.add("</for>");
		openLoop.add("<while>");
		closeLoop.add("</while>");
	}

	// return true if changes were made
	private boolean checkTags(ArrayList<String> loops, String piece) {
		if(openLoop.contains(piece)) {
			loops.add(piece);
			return true;
		}
		else if(closeLoop.contains(piece)) {
			if(loops.get(loops.size() - 1)
					.equals(openLoop.get(closeLoop.indexOf(piece)))) {
				loops.remove(loops.size() - 1);
				return true;
			}
		}
		return false;
	}
}
