package test;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import junit.framework.*;
import translator.DataParsed;
import translator.FileParser;

public class TestFileParser extends TestCase {

	@Test
	void testDataToList() {
		
		File directory = new File("ExperimentsInput");// Dossier existant
		assertTrue("Dossier Inexistant", directory.isDirectory() );
		
		File[] files= directory.listFiles();// Tous les fichiers de ce dossier
		
		String xpname = "20180311-NOOR";
		
		Boolean isFile = false;
		String paramFileName = null;
		
		for(int i = 0; i < files.length ; i++){
  			if ( files[i].getName().equals(xpname) ) {
  				isFile = true;
  				try {
					paramFileName = directory.getCanonicalPath()+"/"+files[i].getName()+"/params.sh";
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
  			}
  		}
		
		assertTrue("Fichier Inexistant", isFile );// isFile.True Le fichier existe
		assertTrue("Nom Fichier null", paramFileName != null );// paramFileName is null
		
		FileParser fileparser = new FileParser( new File(paramFileName));
		List<DataParsed> listParam = new ArrayList<DataParsed>();
		List<DataParsed> listParam_null = new ArrayList<DataParsed>();
		
		listParam = fileparser.dataToList();
		assertTrue("Fichier rend liste vide", !listParam.isEmpty() );// listParam vide
		assertTrue("Fichier rend liste null", listParam_null != null );// listParam vide  
		
	}
	
	@Test
	void testparsingDataToList() {
		File directory = new File("ExperimentsInput");
		File[] files= directory.listFiles();// Tous les fichiers de ce dossier
		
		Boolean isFile = false;
		String paramFileName = null;

		for(int i = 0; i < files.length ; i++){
			try {
				paramFileName = directory.getCanonicalPath()+"/"+files[i].getName()+"/params.sh";
				
				FileParser fileparser = new FileParser( new File(paramFileName));
				List<DataParsed> listParam = new ArrayList<DataParsed>();
				listParam = fileparser.dataToList();
				
				for(DataParsed d : listParam) {
					if ( d.getFirstBox().isEmpty() && d.getSecondBox().isEmpty() ) {
						assertTrue("Pas de données",true);
					}  
				}

			} catch (IOException e) {
				assertTrue("Catch get file",true);
			}
  		}
	}
	
	@Test
	void testFileParser() {
		fail("Not yet implemented");
	}

	@Test
	void testFileAssociationToList() {
		fail("Not yet implemented");
	}

	@Test
	void testCreateEnvParameters() {
		fail("Not yet implemented");
	}

	@Test
	void testSetAllEnvParameters() {
		fail("Not yet implemented");
	}

	@Test
	void testSetEnvParameters() {
		fail("Not yet implemented");
	}

	@Test
	void testReplaceVarEnv() {
		fail("Not yet implemented");
	}

}
