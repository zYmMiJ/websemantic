package translator;

import java.util.List;
/**
 * Superclass of {@link FileParser} and {@link HtmlParser}.
 * @author javae
 * 
 */
public class Parser {
	
	public Parser() {}
	
	public List<DataParsed> dataToList(){return null;};
	
	/**
	 * Print string list
	 * @param list of string
	 */
	
	public void printStringList(List<String> l) {
		for (String s : l ) {
			System.out.println( s );
		}
	}
}
