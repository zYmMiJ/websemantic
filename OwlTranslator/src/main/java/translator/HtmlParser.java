package translator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.apache.log4j.Logger;

/**
 * Parse a HTML file (type :https://gforge.inria.fr/plugins/mediawiki/wiki/lazylav/index.php/Experiments)
 * @author javae
 *
 */
public class HtmlParser extends Parser{
	private String datas[] = {
			"Date",
			"Hash",
			"Hypothesis", 
			"Hypotheses",
			"Experimental setting" , 
			"Command line",
			"Class used",
			"Results", 
			"Analysis",
			"Further experiments",
			"Full log",
			"Execution environment",
			"Designer",
			"Experimenter",
			"Experimentator",
			"Analyst",
			"END"
			};
	
	private String link = "";
	 
	/**
	 * 	Constructor of a Html Parser, Init the link of the document we want to parse
	 * @param link of HTML
	 */
	
	public HtmlParser(String l) {
		super();
		this.link = l;
	}
	
	/**
	 * Connect to a web,html page and generate a {@link DataParsed} list
	 * * @return a {@link List} that contains {@link DataParsed}, 
	 * in the first box of {@link DataParsed} there is a Datahtml per example Experimentator,
	 * in the second box there is the value per example Jerome Euzenat
	 */
	
	public List<DataParsed> dataToList() {
		List<DataParsed> dataparsedHTML = new ArrayList<DataParsed>();
		Document doc; // HTML document
		/*
		 * General Pattern param:value ... /n/r param:value
		 */
		try {
			doc = Jsoup.connect(this.link).get();// Get html Page
			Element el = doc.getElementById("mw-content-text");// Get a specific div 
			
			// Delete balise
			String str = el.toString();
			str = str.replaceAll("<[^>]*>", "");
			
			// Split in line
			String[] parts = str.split("[\\r\\n]+");
			
			int memo_i = 0;// i:Line memo_i kept the actual line
			int j = 0;// Datas
			
			
			for( int i = 0; i < parts.length; i ++) {
				if( parts[i].length() > 2 ) {
					// delete some string
					parts[i] = parts[i].replaceAll("Back to Experiments.", "");
					if( parts[i].contains(datas[j]) ) {
						int tmpi = i;// tmp line
						String[] partitions;// Split into params value
						String params;
						String value;
						parts[i] = parts[i].replaceFirst(" ", "");// delete some space
						
						// Special Case
						if( datas[j].contains("Experimentator")) {
							partitions = parts[i].split(" ");
							params = partitions[0];
							value = "";
						}else {
							partitions = parts[i].split(datas[j]);
							params = datas[j];
							value = "";
						}
						// To stop the parsing next occurence of a "params"
						Boolean stopWhile = true;
						while( tmpi<parts.length && !parts[tmpi].contains(datas[j+1]) && stopWhile) {
							for( int _j = 0; _j < datas.length; _j ++) {
								if( parts[tmpi].contains(datas[_j]) && _j != j  ) {
									stopWhile = false;
								}	
							}
							 
							// Avoid to have params in value
							if( stopWhile ) {
								parts[tmpi] = parts[tmpi].replaceAll(params, "");
								parts[tmpi] = parts[tmpi].replaceFirst(":", "");
								value = value + parts[tmpi];
								tmpi ++;
							}
							
						}
							// Special Case
						if(params.contains("Date")){
							String titleValue = value;
							value = value.replaceAll(" ", "");
							value = value.replaceAll("[^\\d-]", "");
							if( value.length() > 10) {// Add title with index 
								titleValue = titleValue.substring(11,titleValue.length());
								titleValue = titleValue.replaceAll(value,"");
								DataParsed d = new DataParsed("Title", titleValue);
								dataparsedHTML.add(d);
							}
							// Get different pattern of String date
							value = value.replace("-", "");
							value  = value.substring(0, 4)+"-"+value.substring(4, 6)+"-"+value.substring(6, 8);
							
						}
						params = params.replace(":", "");// Clean some char into our params
						params = params.trim();
						value = value.trim();
			
						DataParsed d = new DataParsed(params, value);
						dataparsedHTML.add(d);
						j ++;// to check next (params,datas)
						memo_i = i;// return to the line
					}
				}
				// Last iterations
				if( i+1 >= parts.length && j+1 < datas.length ) {
					i = memo_i;
					j ++;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataparsedHTML;
	}

}
