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

/**
 * Main Class
 * Execute the Translator, with different argument configurations.
 * @author rcouret
 *
 */

public class App {
	
	private static final Logger LOG = Logger.getLogger(App.class);
	
	public static void main(String[] args) throws IOException {
		configLOG();
		
		String ontologyName = "ExperimentOntology.owl";
		String parserType = "";
		
		if(args.length == 0) {
			System.out.println("You need to use a argument.");
		}
		
		//Change Association File
		if (args[0].equals("-c") || args[0].equals("--changeAssociation")) {
			Translator t = new Translator(ontologyName, "/change", parserType, null);
			t.associationFile();
		}
		
		//Translate a Html File, with a link
		else if (args[0].equals("-t") || args[0].equals("--translate") && args[1].equals("html") && args.length>3) {
			parserType = "HTML";
			
	  		String absUrlOfExperiments = args[2];
	  		String pathOutput;
	  		
	  		if(args.length<4)
	  			pathOutput="DataTurtleOutput";
	  		else
	  			pathOutput=args[3];
	 	
			
			Translator translate = new Translator(ontologyName, absUrlOfExperiments, parserType, pathOutput);
			File outputFile = translate.run(true);
			
			CleanFile cleaner = new CleanFile();
	  		cleaner.clean(outputFile, pathOutput);
	  		
	  		LOG.info("Translated HTML: "+absUrlOfExperiments);
		}
		
		//Translate a bash File
		else if (args[0].equals("-t") || args[0].equals("--translate") && args[1].equals("bash") && args.length>3) {
			parserType = "FILE";
			
			String pathOutput;
	  		
			if(args.length<4)
	  			pathOutput="DataTurtleOutput";
	  		else
	  			pathOutput=args[3];
			
	  		String paramFileName = args[2];
	  		
	  		
	  		Translator translate = new Translator(ontologyName, paramFileName, parserType, pathOutput);
	  		File outputFile = translate.run(true);
	  		
	  		CleanFile cleaner = new CleanFile();
	  		cleaner.clean(outputFile, pathOutput);
	  		
	  		LOG.info("Translated Bash: "+paramFileName);
	  		
		}
		
		//args 1 : File, args 2 : HTML
		else if(args[0].equals("-t both") || args[0].equals("--translate both") && args.length>3) {
			String pathOutput;
			
			if(args.length<4)
	  			pathOutput="DataTurtleOutput";
	  		else
	  			pathOutput=args[3];
			
			Translator translate = new Translator(ontologyName, args[1], "FILE", pathOutput);
			File outputFile = translate.run(false);
			TranslateTwoType.merge(ontologyName, args[1], args[2], pathOutput);
			
			CleanFile cleaner = new CleanFile();
	  		
	  		cleaner.clean(outputFile, pathOutput);
	  		
	  		LOG.info("Translated Bash && HTML: "+args[1]+"&&"+args[2]);
		}
		
		//Merge : browse all link in LazyLavender Wiki, and Bash in Experiment Input
		else if (args[0].equals("-t all") || args[0].equals("--translate all")) {
			
			File repertoire = new File("ExperimentsInput");
	  		File[] files=repertoire.listFiles();
	  		
	  		List<String> absUrlOfExperiments = new ArrayList<String>();
	  		absUrlOfExperiments = getAllXPLink("https://gforge.inria.fr/plugins/mediawiki/wiki/lazylav/index.php/Experiments");
	  		
	  		List<String> linkMatched = new ArrayList<String>();
	  		
	  		for(String link : absUrlOfExperiments) {
	  				
	  			for (int i = 0; i < files.length ; i++) {
	  		  		String paramFileName = repertoire.getCanonicalPath()+"/"+files[i].getName()+"/params.sh";
	  				
	  		  		if(link.contains(files[i].getName())) {
	  		  			System.out.println("Parse with Bash and HTML : "+paramFileName+" -- "+link);
	  		  			linkMatched.add(link);
	  		  			TranslateTwoType.merge(ontologyName, paramFileName, link, "DataTurtleOutput");
	  		  		}		
	  			}		
	  		}
	  		
	  		absUrlOfExperiments.removeAll(linkMatched);
	  		
	  		for(String link : absUrlOfExperiments) {
	  			Translator translate = new Translator(ontologyName, link, "HTML", "DataTurtleOutput");
	  			System.out.println("Parse with HTML only: "+link);

					translate.run(true);	
	  		}
	  		
	  		File directoryOut = new File("DataTurtleOutput");
			CleanFile cleaner = new CleanFile(directoryOut.getName());
			cleaner.cleanAll();
		}
		
		//help
		else {
			
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
        fileAppender.setFile("log.txt");
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
