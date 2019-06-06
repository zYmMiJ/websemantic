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
		
		String ontologyName = "ExperimentOntology05-06.owl";
		String parserType = "";
		
		if (args[0].equals("-c") || args[0].equals("--changeAssociation")) {
			Translator t = new Translator(ontologyName, null, parserType);
			t.associationFile();
		}
		
		if (args[0].equals("-a") && args[1].equals("html")) {
			parserType = "HTML";
			
			List<String> absUrlOfExperiments = new ArrayList<String>();
	  		absUrlOfExperiments = getAllXPLink("https://gforge.inria.fr/plugins/mediawiki/wiki/lazylav/index.php/Experiments");
	  			
			for(String link: absUrlOfExperiments ) {	
				System.out.println(link);
				Translator translate = new Translator(ontologyName, link, parserType);

	  			translate.run(true);
			}
		}
		
		if (args[0].equals("-a") && args[1].equals("bash")) {
			parserType = "FILE";
			
			File repertoire = new File("ExperimentsInput");
	  		System.out.println(	"Repertoire ? "+repertoire.isDirectory());
	  		File[] files=repertoire.listFiles();
			
	  		for(int i = 0; i < files.length ; i++) {
	  			String paramFileName = repertoire.getCanonicalPath()+"/"+files[i].getName()+"/params.sh";
	  			System.out.println(	paramFileName );
	  			Translator translate = new Translator(ontologyName, paramFileName, parserType);
	  			translate.run(true);
	  		}
	  		
		}
		
		if (args[0].equals("-m")) {
			
			File repertoire = new File("ExperimentsInput");
	  		System.out.println(	"Repertoire ? "+repertoire.isDirectory());
	  		File[] files=repertoire.listFiles();
	  		
	  		List<String> absUrlOfExperiments = new ArrayList<String>();
	  		absUrlOfExperiments = getAllXPLink("https://gforge.inria.fr/plugins/mediawiki/wiki/lazylav/index.php/Experiments");
	  		
	  		List<String> linkMatched = new ArrayList<String>();
	  				
	  		for(String link : absUrlOfExperiments) {
	  				
	  			for (int i = 0; i < files.length ; i++) {
	  		  		String paramFileName = repertoire.getCanonicalPath()+"/"+files[i].getName()+"/params.sh";
	  				
	  		  		if(link.contains(files[i].getName())) {
	  		  			System.out.println(" match : "+paramFileName+" -- "+link);
	  		  			linkMatched.add(link);
	  		  			MergeInputs.merge(ontologyName, paramFileName, link);
	  		  		}		
	  			}		
	  		}
	  		
	  		absUrlOfExperiments.removeAll(linkMatched);
	  		
	  		for(String link : absUrlOfExperiments) {
	  			Translator translate = new Translator(ontologyName, link, "HTML");
	  			System.out.println(" no match : "+link);
	  			translate.run(true);
	  		}	  		
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
	 * Return tous les href (Experiments) d'une page donn�e. Pattern "-NOOR"
	 */
	private static List<String> getAllXPLink(String s) {
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
}
