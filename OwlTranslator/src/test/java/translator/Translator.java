package translator;
 
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

public class Translator {
	
		private OWLOntologyManager manager;
		private OWLOntology ontology;
		
		private File fileOwl;
		private File fileBash;
		
		private static int experimentParameters = 0;
		private static int experimentInfoParameters = 1;
		private static int documentationParameters = 2;
		private static int experimentVariation = 3;
		private static int defaultValues = 4;
		 
		public Translator(String fileOwlName, String fileBashName) {
			
			manager = OWLManager.createOWLOntologyManager();
			this.fileOwl = new File(fileOwlName);
			loadOntology(fileOwl);

			this.fileBash = new File(fileBashName);
			
		}
		
		public void run() {
			
			// Fichier à parser
			ParserBash parser= new ParserBash(fileBash);
			// Rend la liste avec les parametres
			List<DataParsed> list = parser.fileToList();
			parser.setAllEnvParameters(list);		
		}
		
		private void loadOntology (File file) {
			
			try {
				ontology = manager.loadOntologyFromOntologyDocument(file);

			} catch (OWLOntologyCreationException e) {
				e.printStackTrace();
			}
			
		}
		
		private void saveOntology() {
			try {
				manager.saveOntology(this.ontology);
			} catch (OWLOntologyStorageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//TODO: update depracte
		private void printOntologyWithAxiom() {

			
			//StreamDocumentSource(InputStream is);
			//InputStream targetStream = new FileInputStream(file);
			int i=0;
			
			for (OWLAxiom axiome :  ontology.getAxioms()) {
				System.out.println(i+" - "+axiome);
				i++;
			}
			
			System.out.println();
				
		}

}

