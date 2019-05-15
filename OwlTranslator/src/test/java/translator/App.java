package translator;

import org.apache.log4j.BasicConfigurator;

public class App {

	public static void main(String[] args) {
		
		BasicConfigurator.configure();
		
		Translator translate = new Translator("prototype1.owl", "params.sh");
		translate.run();
	}

}
