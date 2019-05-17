package translator;

import java.io.IOException;

import org.apache.log4j.BasicConfigurator;

public class App {

	public static void main(String[] args) throws IOException {
		
		BasicConfigurator.configure();
		
		Translator translate = new Translator("ExperimentOntologyTurtle.owl", "params.sh");
		translate.run();
	}

}
