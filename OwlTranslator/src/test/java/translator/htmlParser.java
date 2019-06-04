package translator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.log4j.LogMF;
import org.apache.log4j.Logger;

public class htmlParser {
	private static final Logger LOG = Logger.getLogger(MakerIndividual.class);
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
	
	private List<String> absUrlOfExperiments = new ArrayList<String>();
	private String link;
	
	public htmlParser(String link) {
		absUrlOfExperiments = this.getAllXPLink("https://gforge.inria.fr/plugins/mediawiki/wiki/lazylav/index.php/Experiments");
		this.link=link;
	}
	
	
	/*
	 * Return tous les href (Experiments) d'une page donn�e. Pattern "-NOOR"
	 */
	public List<String> getAllXPLink(String s) {
		List<String> l  = new ArrayList<String>();
		Document doc; // HTML document
		try {
			doc = Jsoup.connect(s).get();// On recup la page html
			Elements newsHeadlines = doc.select("a");// On veut recup�rer toutes les balises A
			for (Element headline : newsHeadlines ) {// Parcours les lignes
				if( headline.attr("title").contains("-NOOR") ) {// On recup Les liens contenants -NOOR soit les experiments
					l.add(headline.absUrl("href"));// On les ajoutes � une liste pour les garder en m�moires
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l;// On retourne cette liste
	}
	/*
	 * Affiche une list de string
	 */
	public void printStringList(List<String> l) {
		for (String s : l ) {
			System.out.println( s );
		}
	}
	public List<String> getabsUrlOfExperiments(){
		return absUrlOfExperiments;
	}
	/*
	 * Method qui parse les donn�es et qui rends
	 */
	public List<DataParsed> parser(String s) {
		List<DataParsed> dataparsedHTML = new ArrayList<DataParsed>();
		Document doc; // HTML document
		try {
			doc = Jsoup.connect(s).get();// On recur la page html
			Element el = doc.getElementById("mw-content-text");// On veut recup�rer toutes les balises A
			String str = el.toString();
			str = str.replaceAll("<[^>]*>", "");
			String[] parts = str.split("[\\r\\n]+");
			int memo_i = 0;// Pour garder en m�moire si on ne trouve pas le param suivant
			int j = 0;// parcours Datas
			for( int i = 0; i < parts.length; i ++) {
				if( parts[i].length() > 2 ) {
					parts[i] = parts[i].replaceAll("Back to Experiments.", "");
					if( parts[i].contains(datas[j]) ) {
						int tmpi = i;// Permet de parcourir jusqu'� la prochaine occurence ddes datas qu'on veut recup�rer
						String[] partitions;// On split les param�tres de la valeurs
						String params;
						String value;
						parts[i] = parts[i].replaceFirst(" ", "");// On enleve un " ";
						
						if( datas[j].contains("Experimentator")) {
							partitions = parts[i].split(" ");
							params = partitions[0];
							value = "";
						}else {
							partitions = parts[i].split(datas[j]);
							params = datas[j];
							value = "";
						}
						Boolean stopWhile = true;// Pour stoper le parsing � la prochaine occcurences d'un params (datas)
						while( tmpi<parts.length && !parts[tmpi].contains(datas[j+1]) && stopWhile) {// || lenght parts[]
							for( int _j = 0; _j < datas.length; _j ++) {
								if( parts[tmpi].contains(datas[_j]) && _j != j  ) {
									stopWhile = false;
								}	
							}
							 
							// Empeche d'avoir des params dans les values
							if( stopWhile ) {
								
								parts[tmpi] = parts[tmpi].replaceAll(params, "");
								parts[tmpi] = parts[tmpi].replaceFirst(":", "");
								value = value + parts[tmpi];
								tmpi ++;
							}
							
						}
						// parsing de la date qui pour certaines comprenait le titre
						if(params.contains("Date")){
							String titleValue = value;
							value = value.replaceAll(" ", "");
							value = value.replaceAll("[^\\d-]", "");
							if( value.length() > 10) {// ajout du titre � partir de l'index TODO incoh�rence 
								titleValue = titleValue.substring(11,titleValue.length());
								titleValue = titleValue.replaceAll(value,"");
								DataParsed d = new DataParsed("Title", titleValue);
								dataparsedHTML.add(d);
							}
							// Valeur des dates avec et sans -
							if( value.length() >= 10 && value.contains("-") ) {
								value  = value.substring(0, 10);
							}else if( value.length() >= 8 ) {
								value  = value.substring(0, 8);
							}
						}
						DataParsed d = new DataParsed(params, value);
						dataparsedHTML.add(d);
						j ++;
						memo_i = i;
					}
				}
				if( i+1 >= parts.length && j+1 < datas.length ) {
					i = memo_i;
					j ++;
				}
			}
			System.out.println("L'experiences : "+s);
			for(DataParsed d : dataparsedHTML) {
				LOG.info( d.getFirstBox() + " ; " + d.getSecondBox() );
			}
			System.out.println();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataparsedHTML;
	}
}
