package translator;

import java.io.IOException;

import org.apache.log4j.BasicConfigurator;

public class App {

	public static void main(String[] args) throws IOException {
		
		BasicConfigurator.configure();
		
		Translator2 translate = new Translator2("ExperimentOntology3.owl", "params.sh");
		translate.run();
	}

}
