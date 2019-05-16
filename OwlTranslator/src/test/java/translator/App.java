package translator;

import org.apache.log4j.BasicConfigurator;

public class App {

	public static void main(String[] args) {
		
		BasicConfigurator.configure();
		
		Translator translate = new Translator("prototype1T.owl", "params.sh");
		translate.run();
	}

}
