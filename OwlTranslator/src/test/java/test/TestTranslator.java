package test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;
import translator.DataParsed;
import translator.HtmlParser;


import java.io.File;

import translator.Translator;

public class TestTranslator extends TestCase {

	@Test
	public final void testTranslator() {
		File repertoire = new File("ExperimentsInput");
  		System.out.println(	"Repertoire ? "+repertoire.isDirectory());
  		File[] files=repertoire.listFiles();
  		
  		
  		String inputHTML = "HTML";
  		String inputFile = "FILE";
  		
  		if( true ) {
  			
  		}
  		for(int i = 0; i < files.length ; i++){
  			
  			String paramFileName;
			try {
				paramFileName = repertoire.getCanonicalPath()+"/"+files[i].getName()+"/params.sh";System.out.println( paramFileName );
				Translator translate = new Translator("ExperimentOntology3.owl", paramFileName, "", paramFileName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
  		}
  		
		 try {
			Translator translateHTML = new Translator("ExperimentOntology3.owl", "", "HTML", inputFile);
			Translator translateFILE = new Translator("ExperimentOntology3.owl", "", "FILE", inputFile);
			Translator translateNULL = new Translator("ExperimentOntology3.owl", "", null, inputFile);
			Translator translateBAD  = new Translator("ExperimentOntology3.owl", "", "BAD", inputFile);
		    
	    	} catch (Exception e) {
	    		assert(false);
    	}
	}
	

	 

	 
	@Test
	public void testRun() {
		fail("Not yet implemented");
	}

}
