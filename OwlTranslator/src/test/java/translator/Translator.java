package translator;
 
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
		
		private MakerDatatype makerData;
		private MakerIndividual makerIndividual;
		private MakerProperty makerProperty;
		
		//List of OWLClass, input : Ontology or selected manually (File .txt)
		private List<OWLClass> listClass;
		//Map of OWLClass with their OWLDataProperties, input : Ontology
		private Map<OWLClass, List<OWLDataProperty>> mapClass_DataProperty; 
		//Map of Parameter with its value, input : Bash
		private Map<String, String> mapParameter_Value;
		//Map of OWLDataProperty with the corresponding parameter, input : selected manually (File .txt)
		private Map<OWLDataProperty, String> mapDataProperty_Parameter;
		//Map of OWLDataProperty with the corresponding value, join between mapDataProperty_Parameter and mapParameter_Value
		private Map<OWLDataProperty, String> mapDataProperty_Value;
		//Map of OWLClass with their OWLObjectProperties, input : Ontology
		private Map<OWLClass, List<OWLObjectProperty>> mapClass_ObjectProperty;
		//Map of OWLObjectProperty with corresponding value
		private Map<OWLObjectProperty, String> mapObjectProperty_Parameter;
		//Map of OWLObjectProperty with the corresponding value, join between mapObjectProperty_Parameter and mapObjectProperty_Value
		private Map<OWLObjectProperty, String> mapObjectProperty_Value;
		//Map of a Value and a Instance of Class coressponding, used with person Class
		private Map<String, OWLNamedIndividual> mapValue_Individual;
		
		
		
		private File fileOwl;
		private File fileBash;
		
		private static final Logger LOG = Logger.getLogger(Translator.class);
		
		public Translator(String fileOwlName, String fileBashName) {
			
			manager = OWLManager.createOWLOntologyManager();
			this.fileOwl = new File(fileOwlName);
			loadOntology(fileOwl);

			this.fileBash = new File(fileBashName);
			
			//Initialize the makers
			makerData = new MakerDatatype(manager, ontology);
			makerIndividual = new MakerIndividual(manager, ontology);
			makerProperty = new MakerProperty(manager, ontology);
			
		}
		
		public void run() {
			
			
			//The state of association file, true : the file is completed, false the file will be created 
			boolean parameterCompleted = true;
			//The label of the instances in the parameter
			String label = "LABEL";
			
			try {
				label = translate(parameterCompleted, label);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//SAVE the new Ontology
			OWLOntology ontologyOutput = ontology;
			File outFile = new File("ExperimentOntologyTurtleData_"+label+".ttl");
			
			IRI outIRI=IRI.create(outFile);
			saveOntology(ontologyOutput, outIRI);
		}
		
		private String translate(boolean parameterCompleted, String label) throws IOException {
			
			Parser parserBash = new Parser(fileBash);
			Generator generator = new Generator(ontology);
	
			//CLASS initialization
			listClass = generator.toListClass();
			
			generator.toMapObjectPropertyDomain_Range(listClass);
			
			//DATA_PROPERTY initialization
			mapClass_DataProperty = generator.toMapClass_DataProperty(listClass);
			initMapParameter_Value(parserBash);
			
				//File of OWLDataProperty with the corresponding parameter, need to complete manually or not
			File fileDataProperty_Parameter;
			if(parameterCompleted) 
				fileDataProperty_Parameter = new File("DataProperty_ParameterCompleted.txt");
			else 
				fileDataProperty_Parameter = generator.toFileOWLDataProperty("DataProperty_Parameter.txt", mapClass_DataProperty);
			
				//Parse of the File DataProperty_ParameterCompleted
			Parser parserFileAssociation = new Parser(fileDataProperty_Parameter);
			
			initMapDataProperty_Parameter(parserFileAssociation);
			initMapDataProperty_Value();
			
			//OBJECT_PROPERTY initialization WITH VALUE
			mapClass_ObjectProperty = generator.toMapClass_ObjectProperty(listClass);
			
				//File of OWLObjectProperty with the corresponding parameter, need to complete manually or not
			File fileObjectProperty_Parameter;
			if(parameterCompleted) 
				fileObjectProperty_Parameter = new File("ObjectProperty_ParameterCompleted.txt");
			else 
				fileObjectProperty_Parameter = generator.toFileOWLObjectProperty("ObjectProperty_Parameter.txt", mapClass_ObjectProperty);
			
				//Parse of the File ObjectProperty_ParameterCompleted
			Parser parserFileAssociationObject = new Parser(fileObjectProperty_Parameter);
			
			initMapObjectProperty_Parameter(parserFileAssociationObject);
			initMapObjectProperty_Value();
			
			//OBJECT_PROPERTY initialization WITHOUT VALUE
			//TODO : attribut
			Map<OWLObjectProperty, List<OWLClass>> mapObjectPropertyDomain_Range = generator.toMapObjectPropertyDomain_Range(listClass);
			Map<OWLClass, OWLNamedIndividual> mapClass_Individual = new HashMap<OWLClass, OWLNamedIndividual>();
			
			//INSTANCIATED ontology
			
				//Initialize the person, a same person can having several role
			managePerson();
			
				//Give a label at the NamedIndiviual
			label = "_"+mapParameter_Value.get(label);
			
				//For each Class : Instanced ontology
			for(OWLClass cls : listClass) {
				
				LOG.info("CLASS : "+cls);
				
				//Make a Instance with the class
				OWLNamedIndividual individualOWL = makerIndividual.makeIndividual(cls, label);
				mapClass_Individual.put(cls, individualOWL);
				
				//Building of ObjectProperty associated at the current Individual
				for(OWLObjectProperty ppt : mapClass_ObjectProperty.get(cls)) {
					String value = mapParameter_Value.get(mapObjectProperty_Parameter.get(ppt));
					if(value!=null)
						makerProperty.makeProperty(ppt, individualOWL, mapValue_Individual.get(value));
				}
				
				//Building of dataProperty associated at the current Individual
				for(OWLDataProperty ppt : mapClass_DataProperty.get(cls)) {
						String data=mapDataProperty_Value.get(ppt);
						if(data!=null)
							makerData.makeDataType(ppt, data, individualOWL);								
				}
			}
			
			//OBJECT_PROPERTY instanced WITHOUT VALUE
			for(OWLClass cls : listClass) {
				for(OWLObjectProperty ppt : mapClass_ObjectProperty.get(cls)) {
					String value = mapParameter_Value.get(mapObjectProperty_Parameter.get(ppt));
					//If the value is null itn't a particular case
					if(value==null) {
						OWLClass classDomain = mapObjectPropertyDomain_Range.get(ppt).get(0);
						OWLNamedIndividual individualDomain = mapClass_Individual.get(classDomain);
						
						OWLClass classRange = mapObjectPropertyDomain_Range.get(ppt).get(1);
						OWLNamedIndividual individualRange = mapClass_Individual.get(classRange);
						
						makerProperty.makeProperty(ppt, individualDomain, individualRange);
					}	
				}
			}
			
			return label;
		}
		
		private void managePerson() {
			//Add in the listValueProperty the different and unique Value corresponding at the property
			Set<OWLObjectProperty> setProperty =  mapObjectProperty_Parameter.keySet();
			List<String> listValueProperty = new ArrayList<String>();
			
			for(OWLObjectProperty ppt: setProperty) {
				String param = mapObjectProperty_Parameter.get(ppt);
				
				if(!param.isEmpty()) {
					String value = mapParameter_Value.get(param);
					//Test if the value is present in the list
					if(!listValueProperty.stream().filter(it -> it.contains(value)).findFirst().isPresent()) 
						listValueProperty.add(value);
				}	
			}

			mapValue_Individual = new HashMap<String, OWLNamedIndividual>();
			
			for(String fullname : listValueProperty) {
				String personIRI = "Person";

				//TODO : gestion erreur
				//Assign the Class person
				OWLClass person = listClass.parallelStream().filter(p -> p.getIRI().getShortForm().equals(personIRI)).findFirst().get();
				OWLNamedIndividual individualOWL = makerIndividual.makeIndividual(person, "_"+fullname);
				mapValue_Individual.put(fullname, individualOWL);
				
				//Decompose fullname : firstname+familyname
				String[] tmp= new String[2];
				if(fullname.equals("JEuz")) {
					tmp[0]="Jérome";
					tmp[1]="Euzenat";
				}
				else	
				tmp=fullname.split("\\s+",2);

				for(OWLDataProperty ppt : mapClass_DataProperty.get(person)) {
					
					if(ppt.getIRI().getShortForm().equals("firstName")) 
						makerData.makeDataType(ppt, tmp[0], individualOWL);
					
					if(ppt.getIRI().getShortForm().equals("familyName"))
						makerData.makeDataType(ppt, tmp[1], individualOWL);
				}
			}
		}
		
		private void initMapParameter_Value(Parser parserBash) {
			mapParameter_Value = new HashMap<String, String>();
			
			List<DataParsed> listParameter_Value = parserBash.fileToList();
			for(DataParsed data:listParameter_Value) {
				mapParameter_Value.put(data.getFirstBox(), data.getSecondBox());
			}
		}
		
		private void initMapDataProperty_Parameter(Parser parserFileAssociation) {
			mapDataProperty_Parameter = new HashMap<OWLDataProperty, String>();
			List<DataParsed> listDataProperty_Parameter = parserFileAssociation.fileAssociationToList();
			for(DataParsed data : listDataProperty_Parameter) {
				OWLDataFactory factory = manager.getOWLDataFactory();
				mapDataProperty_Parameter.put(factory.getOWLDataProperty(data.getFirstBox()), data.getSecondBox());
			}
		}
		
		private void initMapDataProperty_Value() {
			mapDataProperty_Value = new HashMap<OWLDataProperty, String>();
			Set<OWLDataProperty> setDataProperty = mapDataProperty_Parameter.keySet();
			for(OWLDataProperty elem : setDataProperty) {
				mapDataProperty_Value.put(elem, mapParameter_Value.get(mapDataProperty_Parameter.get(elem)));
			}
		}
		
		private void initMapObjectProperty_Parameter(Parser parserFileAssociationObject) {
			mapObjectProperty_Parameter = new HashMap<OWLObjectProperty, String>();
			List<DataParsed> listObjectProperty_Parameter = parserFileAssociationObject.fileAssociationToList();
			for(DataParsed data : listObjectProperty_Parameter) {
				OWLDataFactory factory = manager.getOWLDataFactory();
				mapObjectProperty_Parameter.put(factory.getOWLObjectProperty(data.getFirstBox()), data.getSecondBox());
			}
		}
		
		private void initMapObjectProperty_Value() {
			mapObjectProperty_Value = new HashMap<OWLObjectProperty, String>();
			Set<OWLObjectProperty> setObjectProperty = mapObjectProperty_Parameter.keySet();
			for(OWLObjectProperty elem : setObjectProperty) {
				mapObjectProperty_Value.put(elem, mapParameter_Value.get(mapObjectProperty_Parameter.get(elem)));
			}
			
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

