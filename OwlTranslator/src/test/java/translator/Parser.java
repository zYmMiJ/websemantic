package translator;

import java.io.File;

public abstract class Parser {
	
	protected File file;
	
	public Parser(File file) {
		// Notre Fichier Bash à lire
		 this.file = file; 
	}

}
