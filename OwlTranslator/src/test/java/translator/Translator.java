package translator;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
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
		
		private static final Logger LOG = Logger.getLogger(Translator.class);
		
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
			List<Parameter> list = parser.fileToList();
			
			// Affiche l'hypothèses
			LOG.info("Hyp :"+list.get(3).getValeur().get(2));
			LOG.info("Recherche :"+list.get(3).getOneParam("HYPOTHESIS"));
				
			// Traduction de nos données du parser en owl
			MakerDatatype makerData = new MakerDatatype(manager, ontology);
			MakerIndividual makerIndividual = new MakerIndividual(manager, ontology);
			
			makerData.makeDataType("HypothesisFormulation", list.get(3).getOneParam("HYPOTHESIS"));
			makerIndividual.makeIndividual("Hypothesis", "HypothesisIndividual");
			
			//saveOntology();
			
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

