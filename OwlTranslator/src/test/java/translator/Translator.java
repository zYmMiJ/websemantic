package translator;
 
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
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
			Generator generator = new Generator(ontology);
			
			//List of OWLClass, input : Ontology or selected manually (File .txt)
			List<OWLClass> listClass = generator.toListClass();
			
			//List of OWLClass with their OWLDataProperties, input : Ontology
			Map<OWLClass, List<OWLDataProperty>> mapClass_DataProperty = generator.toMapClass_DataProperty(listClass);
			
			//List of Parameter with its value, input : Bash
			Map<String, String> mapParameter_Value = new HashMap<String, String>();
			//TODO
			List<DataParsed> listParameter_Value = parserBash.fileToList();
			for(DataParsed data:listParameter_Value) {
				mapParameter_Value.put(data.getFirstBox(), data.getSecondBox());
			}
			
			//File of OWLDataProperty with the corresponding parameter, need to complete manually or not
			File fileDataProperty_Parameter;
			boolean statutFileDP = true;
			if(statutFileDP) 
				fileDataProperty_Parameter = new File("DataProperty_ParameterCompleted.txt");
			else 
				fileDataProperty_Parameter = generator.toFileOWLDataProperty("DataProperty_Parameter.txt", mapClass_DataProperty);
			
			//Parse of the File DataProperty_ParameterCompleted
			Parser parserFileAssociation = new Parser(fileDataProperty_Parameter);
			
			//List of OWLDataProperty with the corresponding parameter, input : selected manually (File .txt)
			Map<OWLDataProperty, String> mapDataProperty_Parameter = new HashMap<OWLDataProperty, String>();
			List<DataParsed> listDataProperty_Parameter = parserFileAssociation.fileAssociationToList();
			for(DataParsed data : listDataProperty_Parameter) {
				OWLDataFactory factory = manager.getOWLDataFactory();
				mapDataProperty_Parameter.put(factory.getOWLDataProperty(data.getFirstBox()), data.getSecondBox());
			}
			
			//List of OWLDataProperty with the corresponding value, join between mapDataProperty_Parameter and mapParameter_Value
			Map<OWLDataProperty, String> mapDataProperty_Value = new HashMap<OWLDataProperty, String>();
			Set<OWLDataProperty> setDataProperty = mapDataProperty_Parameter.keySet();
			for(OWLDataProperty elem : setDataProperty) {
				mapDataProperty_Value.put(elem, mapParameter_Value.get(mapDataProperty_Parameter.get(elem)));
			}
			
			//-----------------------------------------------------------
			
			//List of OWLClass with their OWLObjectProperties, input : Ontology
			Map<OWLClass, List<OWLObjectProperty>> mapClass_ObjectProperty = generator.toMapClass_ObjectProperty(listClass);
			
			//File of OWLObjectProperty with the corresponding parameter, need to complete manually or not
			File fileObjectProperty_Parameter;
			boolean statutFileOP = true;
			if(statutFileOP) 
				fileObjectProperty_Parameter = new File("ObjectProperty_ParameterCompleted.txt");
			else 
				fileObjectProperty_Parameter = generator.toFileOWLObjectProperty("ObjectProperty_Parameter.txt", mapClass_ObjectProperty);
			
			//Parse of the File ObjectProperty_ParameterCompleted
			Parser parserFileAssociationObject = new Parser(fileObjectProperty_Parameter);
			
			//List of OWLObjectProperty with corresponding value
			Map<OWLObjectProperty, String> mapObjectProperty_Parameter = new HashMap<OWLObjectProperty, String>();
			List<DataParsed> listObjectProperty_Parameter = parserFileAssociationObject.fileAssociationToList();
			for(DataParsed data : listObjectProperty_Parameter) {
				OWLDataFactory factory = manager.getOWLDataFactory();
				System.out.println("BOX1 : "+data.getFirstBox());
				System.out.println("BOX2 : "+data.getSecondBox());
				mapObjectProperty_Parameter.put(factory.getOWLObjectProperty(data.getFirstBox()), data.getSecondBox());
			}
			
			//List of OWLObjectProperty with the corresponding value, join between mapObjectProperty_Parameter and mapObjectProperty_Value
			Map<OWLObjectProperty, String> mapObjectProperty_Value = new HashMap<OWLObjectProperty, String>();
			Set<OWLObjectProperty> setObjectProperty = mapObjectProperty_Parameter.keySet();
			for(OWLObjectProperty elem : setObjectProperty) {
				System.out.println("ELEM : "+elem);
				System.out.println("Value : "+mapObjectProperty_Value.get(mapObjectProperty_Parameter.get(elem)));
				mapObjectProperty_Value.put(elem, mapParameter_Value.get(mapObjectProperty_Parameter.get(elem)));
			}
			
			for(OWLClass cls : listClass) {
				System.out.println("CLASS : "+cls);
				for(OWLObjectProperty ppt : mapClass_ObjectProperty.get(cls)) {
					System.out.println("PROPERTY : "+ppt);
					
					String data=mapObjectProperty_Value.get(ppt);
					if(data!=null)
						System.out.println("DATA : "+data);
				}		
			}
			
			//Recup différentes valeurs
			//Instancier les personnages
			//Param -- Property
			//Associer property à personnage
			//Make
			
			//-------------------------------------------------------------------------------------------
			
			//Initialize the makers
			MakerDatatype makerData = new MakerDatatype(manager, ontology);
			MakerIndividual makerIndividual = new MakerIndividual(manager, ontology);
			
			//Give a label at the NamedIndiviual
			String label = "_"+mapParameter_Value.get("LABEL");
			
			for(OWLClass cls : listClass) {
				
				LOG.info("CLASS : "+cls);
				
				//Make a Instance with the class
				OWLNamedIndividual individualOWL = makerIndividual.makeIndividual(cls, label);
				
				for(OWLDataProperty ppt : mapClass_DataProperty.get(cls)) {
						String data=mapDataProperty_Value.get(ppt);
						if(data!=null)
							makerData.makeDataType(ppt, data, individualOWL);
				}
			}
			
			
			//Save the new Ontology
			OWLOntology ontologyOutput = ontology;
			File outFile = new File("ExperimentOntologyTurtleData"+label+".ttl");
			
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

