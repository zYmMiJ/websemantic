package translator;
 
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLOntologyDocumentTarget;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
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
		
		public void run() throws IOException {
			
			Parser parserBash = new Parser(fileBash);
			Tools tools = new Tools(ontology);
			
			//List with parameter and value
			List<DataParsed> listParameterValue = parserBash.fileToList();
			Map<String, String> mapParameterValue = new HashMap<String, String>();
			for(DataParsed data:listParameterValue) {
				mapParameterValue.put(data.getFirstBox(), data.getSecondBox());
			}
			
			//List with NameClass and DataProperties
			List<DataParsed> listClassDataP  = tools.joinClassAndDataProperty();
			Map<String, String> mapClassDataP = new HashMap<String, String>();
			for(DataParsed data:listClassDataP) {
				LOG.info("listClassDataP : "+data.getFirstBox()+" / "+data.getSecondBox());
				mapClassDataP.put(data.getFirstBox(), data.getSecondBox());
			}
			
			//Create and Make list with Association : "NameClass=PARAM"
			
			//File fileAssociation = tools.listOWLClasstoFile("FileAssociation.txt");
			File fileAssociation = new File("FileAssociationEnable.txt");
			
			Parser parserFileAssociation = new Parser(fileAssociation); 
			List<DataParsed> listClassParam = parserFileAssociation.fileToList();
			
			MakerDatatype makerData = new MakerDatatype(manager, ontology);
			MakerIndividual makerIndividual = new MakerIndividual(manager, ontology);
			
			String label = mapParameterValue.get("LABEL");
			
			for(DataParsed data : listClassParam) {
				
				//On selectionne dans la liste Class/Paramètre : la Class
				String nameClass = data.getFirstBox();
				LOG.info("CLASS : "+nameClass);
				//On crée un Individual
				OWLNamedIndividual individualOWL = makerIndividual.makeIndividual(nameClass, nameClass+label);
				
				//On selectionne dans la liste Class/Paramètre : le paramètre
				String nameParam = data.getSecondBox();
				//On selectionne dans la liste Paramètre/Valeur : la valeur
				String dataValue = mapParameterValue.get(nameParam);
				//On selectionne dans la liste Class/Propriété : la propriété
				String nameDataPpt = mapClassDataP.get(nameClass);
				
				//Si la propriété existe on crée une data
				if(nameDataPpt!="" && dataValue!=null) {
					LOG.info("VALUE : "+dataValue);
					makerData.makeDataType(nameDataPpt, dataValue, individualOWL);		
				}
			}
			
			OWLOntology ontologyOutput = ontology;
			
			File outFile = new File("ExperimentOntologyTurtleData.owl");
			IRI outIRI=IRI.create(outFile);	
			saveOntology(ontologyOutput, outIRI);
			
		}
		
		private void loadOntology (File file) {
			
			try {
				this.ontology = manager.loadOntologyFromOntologyDocument(file);

			} catch (OWLOntologyCreationException e) {
				e.printStackTrace();
			}
			
		}
		
		private void saveOntology(OWLOntology ontologyOutput, IRI outIRI) {
			try {
				manager.saveOntology(ontologyOutput,outIRI);
				LOG.info("Ontology Saved.");
			} catch (OWLOntologyStorageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	 	

}

