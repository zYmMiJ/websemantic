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
		
		if( args.length != 0 && !(
				( args[0].equals("-c") || args[0].equals("--changeAssociation")  ||
				 args[0].equals("-t") || args[0].equals("--translate")  ||
				args[0].equals("t both") || args[0].equals("--translate both")  ||
				args[0].equals("-t all") || args[0].equals("--translate all") ||
				args[0].equals("-h") || args[0].equals("--help")
						))) {
			System.out.println("Incorrect argument");
			System.out.println("-help more informations");
			System.exit(0);
		}

		switch( args.length ) {
		  case 0:
				System.out.println("missing arguments");
				System.out.println("-help more informations");
				System.exit(0);
				 break;
		  case 1:
		    if ( args[0].contains("-h") ||  args[0].contains("-help")) {
		    	System.out.println("-t, --translate : ");
		    	System.out.println("	-t -html  URL... (DIRECTORY)");
		    	System.out.println("Used to translate a html page from this URL.");
		    	System.out.println("	-t -bash SOURCE… (DIRECTORY)");
		    	System.out.println("Used to translate a bash file.");
		    	System.out.println("	-t -both SOURCE… URL… (DIRECTORY)");
		    	System.out.println("Used to translate the both, bash file and html page.");
		    	System.out.println("	-t -all");
		    	System.out.println("Used to translate all html page from https://gforge.inria.fr/plugins/mediawiki/wiki/lazylav/index.php/Experiments \n\r"
		    			+ "and bash file from Experiments Input (contents the  experiment directories ex: 20180311-NOOR)");
		    	System.exit(0);
		    }else if(  args[0].equals("-c") || args[0].equals("--changeAssociation") ){
		    	Translator t = new Translator(ontologyName, "/change", parserType, null);
				t.associationFile();
			 }else {
		    
				System.out.println("missing arguments");
				System.out.println("-help for more informations");
				System.exit(0);
		    }
		    break;
		  case 2:
			  if( args[0].equals("-t") || args[0].equals("--translate") ){
			    	if (args[1].equals("-all")) {
			    		System.out.println("Translate All");
			    		File repertoire = new File("ExperimentsInput");
			    		if( !repertoire.exists() ) {
			    			repertoire.mkdir();
			    		}
				  		File[] files=repertoire.listFiles();
				  		
				  		List<String> absUrlOfExperiments = new ArrayList<String>();
				  		absUrlOfExperiments = getAllXPLink("https://gforge.inria.fr/plugins/mediawiki/wiki/lazylav/index.php/Experiments");
				  		
				  		List<String> linkMatched = new ArrayList<String>();
				  		
				  		for(String link : absUrlOfExperiments) {
				  				
				  			for (int i = 0; i < files.length ; i++) {
				  		  		String paramFileName = repertoire.getCanonicalPath()+"/"+files[i].getName()+"/params.sh";
				  				
				  		  		if(link.contains(files[i].getName())) {
				  		  			//System.out.println("Parse with Bash and HTML : "+paramFileName+" -- "+link);
				  		  			linkMatched.add(link);
				  		  			TranslateTwoType.merge(ontologyName, paramFileName, link, "DataTurtleOutput");
				  		  		}		
				  			}		
				  		}
				  		
				  		absUrlOfExperiments.removeAll(linkMatched);
				  		File output = new File("DataTurtleOutput");
			    		if( !output.exists() ) {
			    			output.mkdir();
			    		}
				  		for(String link : absUrlOfExperiments) {
				  			Translator translate = new Translator(ontologyName, link, "HTML", "DataTurtleOutput");
				  			//System.out.println("Parse with HTML only: "+link);

								translate.run(true);	
				  		}
				  		
				  		File directoryOut = new File("DataTurtleOutput");
						CleanFile cleaner = new CleanFile(directoryOut.getName());
						cleaner.cleanAll();
			    		System.exit(0);
			    	}
			  }

    		System.out.println("Wrong arguments");
			System.out.println("-help for more informations");
			System.exit(0);
		    break;
	  	case 4:
	  		if( args[0].equals("-t") || args[0].equals("--translate") ){

				switch( args[1] ) {
				  case "-all":
						System.out.println("Too much arguments");
						System.out.println("-help for more informations");
						System.exit(0);
				  case "-html":
					  parserType = "HTML";
						System.out.println(args.length);
						String pathOutput;
						String absUrlOfExperiments = null;
						
						absUrlOfExperiments = args[2];	
				  		pathOutput="DataTurtleOutput";
				  		pathOutput=args[3];
				  		System.out.println("1");
						Translator translate = new Translator(ontologyName, absUrlOfExperiments, parserType, pathOutput);
						System.out.println("1");
						File outputFile = translate.run(true);
						
						CleanFile cleaner = new CleanFile();
				  		cleaner.clean(outputFile, pathOutput);
				  		
				  		LOG.info("Translated HTML: "+absUrlOfExperiments);
				  		System.exit(0);
				  case "-bash":
						System.out.println("bash arguments");
						System.exit(0);
				  case "-both":
						System.out.println("both arguments");
						System.exit(0);
				}
	  		}
    		System.out.println("Wrong arguments");
			System.out.println("-help for more informations");
			System.exit(0);
			  break;
		}
	
		
/*
		//Change Association File
		if ( args[0].equals("-c") || args[0].equals("--changeAssociation")) {
			Translator t = new Translator(ontologyName, "/change", parserType, null);
			t.associationFile();
		}
		
		//Translate a Html File, with a link
		else if ( (args[0].equals("-t") || args[0].equals("--translate") )  && args.length>3 && args[1].equals("html")) {
			parserType = "HTML";
			System.out.println(args.length);
			String pathOutput;
			String absUrlOfExperiments = null;
			
			try {
				absUrlOfExperiments = args[2];	
			}
			catch(Exception  e) {
				System.out.println("Url ");
				System.out.println("-help more informations");
				System.exit(0);
			}
	  		if(args.length<4) {
	  			pathOutput="DataTurtleOutput";
	  		}else {
	  			pathOutput=args[3];
	  		}
	  			
	 	
			
			Translator translate = new Translator(ontologyName, absUrlOfExperiments, parserType, pathOutput);
			File outputFile = translate.run(true);
			
			CleanFile cleaner = new CleanFile();
	  		cleaner.clean(outputFile, pathOutput);
	  		
	  		LOG.info("Translated HTML: "+absUrlOfExperiments);
		}
		
		//Translate a bash File
		else if ( ( args[0].equals("-t") || args[0].equals("--translate") ) && args.length>3 && args[1].equals("bash")) {
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
		else if( ( args[0].equals("-t both") || args[0].equals("--translate both") ) && args.length>3) {
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
			System.out.println("missing arguments");
			System.out.println("-help more informations");
			System.exit(0);
		}*/
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
	 * Return tous les href (Experiments) d'une page donnï¿½e. Pattern "-NOOR"
	 */
	private static List<String> getAllXPLink(String s) {

		List<String> l  = new ArrayList<String>();
		Document doc; // HTML document
		try {
			doc = Jsoup.connect(s).get();// On recup la page html
			Elements newsHeadlines = doc.select("a");// On veut recupï¿½rer toutes les balises A
			for (Element headline : newsHeadlines ) {// Parcours les lignes
				if( headline.attr("title").contains("-NOOR") ) {// On recup Les liens contenants -NOOR soit les experiments
					l.add(headline.absUrl("href"));// On les ajoutes ï¿½ une liste pour les garder en mï¿½moires
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l;// On retourne cette liste
	}
	
}
