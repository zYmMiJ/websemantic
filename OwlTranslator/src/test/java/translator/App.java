package translator;

import java.io.IOException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class App {

	public static void main(String[] args) throws IOException {
		configLOG();
		
		Translator translate = new Translator("ExperimentOntology3.owl", "/home/rcouret/Documents/Experiments/20181112-NOOR/params.sh");
		translate.run();
		
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

}
