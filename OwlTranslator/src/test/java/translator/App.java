package translator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App {

	public static void main(String[] args) throws IOException {
		configLOG();
		
		File repertoire = new File("ExperimentsInput");
  		System.out.println(	"Repertoire ? "+repertoire.isDirectory());
  		File[] files=repertoire.listFiles();
  		
  		/*for(int i = 0; i < files.length ; i++){
  			
  			String paramFileName = repertoire.getCanonicalPath()+"/"+files[i].getName()+"/params.sh";
  			System.out.println(	paramFileName );
  			Translator translate = new Translator("ExperimentOntology3.owl", paramFileName);
  			translate.run();
  			
  		}*/
  		List<String> absUrlOfExperiments = new ArrayList<String>();
  		absUrlOfExperiments = getAllXPLink("https://gforge.inria.fr/plugins/mediawiki/wiki/lazylav/index.php/Experiments");
  			
		for(String link: absUrlOfExperiments ) {	
			System.out.println(link);
			Translator translate = new Translator("ExperimentOntology3.owl", link, "HTML");
  			translate.run();
		}
		
		
	} 
	
	private static void configLOG() {
		
		// creates pattern layout
        PatternLayout layout = new PatternLayout();
        String conversionPattern = "%-7p %d [%t] %c %x - %m%n";
        layout.setConversionPattern(conversionPattern);
 
        // creates console appender
        ConsoleAppender consoleAppender = new ConsoleAppender();
        consoleAppender.setLayout(layout);
        consoleAppender.activateOptions();
 
        // creates file appender
        FileAppender fileAppender = new FileAppender();
        fileAppender.setFile("logAll.txt");
        fileAppender.setLayout(layout);
        fileAppender.activateOptions();
 
        // configures the root logger
        Logger rootLogger = Logger.getRootLogger();
        //rootLogger.setLevel(Level.DEBUG);
        
        // Affiche dans la console 
        //rootLogger.addAppender(consoleAppender);
        rootLogger.addAppender(fileAppender);// Affiche dans le fichier
	}

	
	/*
	 * Return tous les href (Experiments) d'une page donnée. Pattern "-NOOR"
	 */
	public static List<String> getAllXPLink(String s) {
		List<String> l  = new ArrayList<String>();
		Document doc; // HTML document
		try {
			doc = Jsoup.connect(s).get();// On recup la page html
			Elements newsHeadlines = doc.select("a");// On veut recupérer toutes les balises A
			for (Element headline : newsHeadlines ) {// Parcours les lignes
				if( headline.attr("title").contains("-NOOR") ) {// On recup Les liens contenants -NOOR soit les experiments
					l.add(headline.absUrl("href"));// On les ajoutes à une liste pour les garder en mémoires
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l;// On retourne cette liste
	}
}
