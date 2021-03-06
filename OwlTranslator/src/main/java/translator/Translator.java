package translator;
 
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import uk.ac.manchester.cs.owl.owlapi.OWLDatatypeImpl;

/**
 * Translate a file (HTML or bash) in semantic data (RDF/turtle).
 * Use a OWL file to translate data.
 * @author rcouret
 *
 */

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
		private Map<OWLDataProperty, String[]> mapDataProperty_Parameter;
		//Map of OWLDataProperty with the corresponding value, join between mapDataProperty_Parameter and mapParameter_Value
		private Map<OWLDataProperty, String> mapDataProperty_Value;
		//Map of OWLClass with their OWLObjectProperties, input : Ontology
		private Map<OWLClass, List<OWLObjectProperty>> mapClass_ObjectProperty;
		//Map of OWLObjectProperty with corresponding value
		private Map<OWLObjectProperty, String[]> mapObjectProperty_Parameter;
		//Map of OWLObjectProperty with the corresponding value, join between mapObjectProperty_Parameter and mapObjectProperty_Value
		private Map<OWLObjectProperty, String> mapObjectProperty_Value;
		//Map of a ObjectProperty a list with two Class, Class 0 : Domain, Class 1 : Range 
		private Map<OWLObjectProperty, List<OWLClass>> mapObjectPropertyDomain_Range;
		//Map of Class associate at their NamedIndividual
		private Map<OWLClass, OWLNamedIndividual> mapClass_Individual;
		//Map of OWLDataProperty and OWLDataRange, used to set the type of data
		private Map<OWLDataProperty, OWLDataRange> mapDataProperty_DataRange;
		
		
		//The state of association file, true : the file is completed, false the file will be created
		private static final String nameFileAssociation = "Association";
		
		private File fileOwl;
		private File file;
		private String urlHTML;
		private String Input_Type;
		private String label;
		private String pathOutput;
		
		/**
		 * Transform OWL file and HTML data to RDF dataset.
		 * @param {@link String} represents OWL file pointing path.
		 * @param {@link String} represents HTML file pointing path.
		 * @param {@link String} define if the input is HTML or a File
		 * @param {@link String} the output file.
		 */
		
		public Translator(String fileOwlName, String targetFile, String type, String pathOutput) {
			this.manager = OWLManager.createOWLOntologyManager();
			this.fileOwl = new File(fileOwlName);

			if( fileOwl.exists() ) {
				try {
					loadOntology(fileOwl);
				}catch(Exception e) {
					System.out.println("Erreur Ontologie incorrecte ou invalide");
					System.exit(0);
				}
			}else {
				System.out.println("Ontology Introuvable, ou nom incorrecte, nom obligatoire(ExperimentOntology.owl) ");
				System.exit(0);
			}
			
			
			this.pathOutput=pathOutput;
			File param = null;
			if( type.equals("HTML") ) {
				this.urlHTML = targetFile;
				this.label = labelHtml(targetFile);
			}else if( type.equals("FILE") ) {
				if( ! new File(targetFile).exists() ) {
					System.out.println("TargetFile Inexistant");
					System.exit(0);
				}else {
					param = new File(targetFile);
				}
				this.file = new File(targetFile);
				File dirNameParam = new File(param.getParent());
				this.label = labelFile(dirNameParam.getName());
			}
			else {
				this.label = labelHtml(targetFile);
			}
			
			this.Input_Type = type; // Initialize type of input parsing
			//Initialize the makers
			makerData = new MakerDatatype(manager, ontology);
			makerIndividual = new MakerIndividual(manager, ontology);
			makerProperty = new MakerProperty(manager, ontology);
			
		}
		
		/**
		 * Run the translator with the parameter initialized in the constructor
		 * @param {@link boolean} true to save turtle data else false
		 * @return the destination where the file can be save
		 */
		
		public File run(boolean save) {

			try {
				translate();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			File outFile = new File(pathOutput+"/ExperimentOntologyTurtleData"+label+".ttl");
			//SAVE the new Ontology
			if(save) {		
				OWLOntology ontologyOutput = ontology;
				IRI outIRI=IRI.create(outFile);
				saveOntology(ontologyOutput, outIRI);
			}
			
			return outFile;
		}
		

		private void translate() throws IOException {
					
			Generator generator = new Generator(ontology);
	
			//CLASS initialization
			listClass = generator.toListClass();
			
			generator.toMapObjectPropertyDomain_Range(listClass);
			
			//DATA_PROPERTY initialization
			mapClass_DataProperty = generator.toMapClass_DataProperty(listClass);

			HtmlParser parserHTML = null;
			FileParser parserFILE = null;
			if( this.Input_Type!= null &&  Input_Type.equals("FILE") ) {
				 parserFILE = new FileParser(this.file);
				 initMapParameter_Value(parserFILE); 
			}
			if( this.Input_Type!= null && this.Input_Type.equals("HTML")) {
				 parserHTML = new HtmlParser(this.urlHTML);
				 initMapParameter_Value(parserHTML); 
			}
			
				//File of OWLDataProperty with the corresponding parameter, need to complete manually or not
			File fileDataProperty_Parameter = new File(nameFileAssociation+"DataProperty.txt");
				//Parse of the File DataProperty_ParameterCompleted
			FileParser parserFileAssociation = new FileParser(fileDataProperty_Parameter);
			
			initMapDataProperty_Parameter(parserFileAssociation);
			
			if(mapDataProperty_Value==null)
				initMapDataProperty_Value();

				
			
				//
			Set<OWLDataProperty> setDataProperty = mapDataProperty_Parameter.keySet();
			List<OWLDataProperty> listDataProperty = new ArrayList<OWLDataProperty>();
			listDataProperty.addAll(setDataProperty);
			mapDataProperty_DataRange = generator.toMapDataProperty_DataRange(listDataProperty);
			
			//OBJECT_PROPERTY initialization WITH VALUE in fileAssociation
			mapClass_ObjectProperty = generator.toMapClass_ObjectProperty(listClass);
			
				//File of OWLObjectProperty with the corresponding parameter, need to complete manually or not
			File fileObjectProperty_Parameter = new File(nameFileAssociation+"ObjectProperty.txt");
			
				//Parse of the File ObjectProperty_ParameterCompleted
			FileParser parserFileAssociationObject = new FileParser(fileObjectProperty_Parameter);
			
			initMapObjectProperty_Parameter(parserFileAssociationObject);
			
			if(mapObjectProperty_Value==null)
				initMapObjectProperty_Value();
			
			//OBJECT_PROPERTY initialization WITHOUT VALUE in fileAssociation
			mapObjectPropertyDomain_Range = generator.toMapObjectPropertyDomain_Range(listClass);
			mapClass_Individual = new HashMap<OWLClass, OWLNamedIndividual>();
			
			//INSTANCIATED ontology
			
				//Give a label at the NamedIndiviual
			OWLDataFactory factory = manager.getOWLDataFactory();
			
				//For each Class : Instanced ontology
			for(OWLClass cls : listClass) {
				
				//Make a Instance with the class
				OWLNamedIndividual individualOWL = makerIndividual.makeIndividual(cls, label);
				mapClass_Individual.put(cls, individualOWL);
				
				//Building of dataProperty associated at the current Individual
				for(OWLDataProperty ppt : mapClass_DataProperty.get(cls)) {
						String data = mapDataProperty_Value.get(ppt);
						OWLDataRange dataRange = mapDataProperty_DataRange.get(ppt);
						if( data!=null ) {
							makerData.makeDataType(ppt, dataRange, data, individualOWL);
						}									
				}
			}
			
			//OBJECT_PROPERTY instanced WITHOUT VALUE
			for(OWLClass cls : listClass) {
				for(OWLObjectProperty ppt : mapClass_ObjectProperty.get(cls)) {
					
					String value = mapObjectProperty_Value.get(ppt);
					//If the value is null itn't a particular case
					if(value==null) {
						OWLClass classDomain = mapObjectPropertyDomain_Range.get(ppt).get(0);
						OWLNamedIndividual individualDomain = mapClass_Individual.get(classDomain);
						
						OWLClass classRange = mapObjectPropertyDomain_Range.get(ppt).get(1);
						OWLNamedIndividual individualRange = mapClass_Individual.get(classRange);
						
						if(classRange!=null)
							makerProperty.makeProperty(ppt, individualDomain, individualRange);
					}
					else if(ppt.getIRI().getShortForm().equals("variationOf")){
						
						//Create IRI with the nameSpace of the current Class and ShortForm is Experiment
						IRI experimentIRI = IRI.create(cls.getIRI().getNamespace()+"Experiment");
						OWLClass experiment = factory.getOWLClass(experimentIRI);
						OWLNamedIndividual experimentVariation = makerIndividual.makeIndividual(experiment, "_"+mapObjectProperty_Value.get(ppt));
						
						OWLClass classDomain = mapObjectPropertyDomain_Range.get(ppt).get(0);
						OWLNamedIndividual individualDomain = mapClass_Individual.get(classDomain);
						makerProperty.makeProperty(ppt, individualDomain, experimentVariation);
					}
					else {
						//make ObjectProperty about Person
						IRI personIRI = IRI.create("http://xmlns.com/foaf/0.1/Person");
						OWLClass person = factory.getOWLClass(personIRI);
						OWLNamedIndividual personInstance = makerIndividual.makeIndividual(person, "_"+mapObjectProperty_Value.get(ppt));
		
						
						OWLClass classDomain = mapObjectPropertyDomain_Range.get(ppt).get(0);
						OWLNamedIndividual individualDomain = mapClass_Individual.get(classDomain);
						
						//if(individualDomain!=null && individualRange!=null)
							makerProperty.makeProperty(ppt, individualDomain, personInstance);
						
						//make DataProperty about Person
						IRI nameIRI = IRI.create("http://xmlns.com/foaf/0.1/name");
						OWLDataProperty name = factory.getOWLDataProperty(nameIRI);
						
						IRI dataIRI = IRI.create("xsd:string");
						OWLDatatypeImpl dataRange = new OWLDatatypeImpl(dataIRI); 
							makerData.makeDataType(name,  dataRange, value, personInstance);
					}
				}
			}
		}
		
		/**
		 * Generate the association files (DataProperty.txt and ObjectProperty.txt).
		 * A association file need to complete manually, it's the logic correspondence between OWL properties and file properties.
		 * @throws IOException
		 */
		
		public void associationFile() throws IOException {
			
			@SuppressWarnings("unused")
			File fileDataProperty_Parameter;
			Generator generator = new Generator(ontology);
			
			listClass = generator.toListClass();
			mapClass_DataProperty = generator.toMapClass_DataProperty(listClass);
			mapClass_ObjectProperty = generator.toMapClass_ObjectProperty(listClass);
			
			String[] ppt = {"DataProperty.txt", "ObjectProperty.txt"};	
			
			if( !new File(nameFileAssociation+ppt[0]).exists() ) {
				new File(nameFileAssociation+ppt[0]).createNewFile();
				new File(nameFileAssociation+ppt[1]).createNewFile();
			}

			if(fileAssociationView(nameFileAssociation+ppt[0])) 
				fileDataProperty_Parameter = generator.toFileOWLDataProperty(nameFileAssociation+ppt[0], mapClass_DataProperty);
			if(fileAssociationView(nameFileAssociation+ppt[1])) 
				fileDataProperty_Parameter = generator.toFileOWLObjectProperty(nameFileAssociation+ppt[1], mapClass_ObjectProperty);
					
		}
		
		/**
		 * Getter
		 * @return {@link Map} collection that contains {@link OWLDataProperty} as key and {@link String} as value which corresponding at the value from the data file.
		 */
		public Map<OWLDataProperty, String> getMapDataProperty_Value() {
			return mapDataProperty_Value;
		}
		
		/**
		 * Setter
		 * @param {@link Map} collection that contains {@link OWLDataProperty} as key and {@link String} as value which corresponding at the value from the data file.
		 */
		public void setMapDataProperty_Value(Map<OWLDataProperty, String> mapDataProperty_Value) {
			this.mapDataProperty_Value = mapDataProperty_Value;
		}
		
		/**
		 * Getter
		 * @return {@link Map} collection that contains {@link OWLObjectProperty} as key and {@link String} as value which corresponding at the value from the data file.
		 */
		public Map<OWLObjectProperty, String> getMapObjectProperty_Value() {
			return mapObjectProperty_Value;
		}
		
		/**
		 * Setter
		 * @param {@link Map} collection that contains {@link OWLObjectProperty} as key and {@link String} as value which corresponding at the value from the data file.
		 */
		public void setMapObjectProperty_Value(Map<OWLObjectProperty, String> mapObjectProperty_Value) {
			this.mapObjectProperty_Value = mapObjectProperty_Value;
		}
		
		/**
		 * Getter
		 * @return {@link Map} collection that contains {@link OWLDataProperty} as key and {@link String[]} as value which corresponding at the parameters from the association file.
		 */
		public Map<OWLDataProperty, String[]> getMapDataProperty_Parameter(){
			return mapDataProperty_Parameter;
		}
		
		/**
		 * Initialize {@link Map} that contains {@link String} that represent a parameter from {@link File} as
		 * key and {@link String} that represent a value from {@link File} as value. 
		 * @param parserHTML, the parser corresponding at the HTML file input.
		 */
		private void initMapParameter_Value(Parser parser) {
			mapParameter_Value = new HashMap<String, String>();
			
			List<DataParsed> listParameter_Value = parser.dataToList();

			for(DataParsed data:listParameter_Value) {
				mapParameter_Value.put(data.getFirstBox(), data.getSecondBox());
			}
		}
		
		/**
		 * Transform a {@link File} in a {@link Map} that contains {@link OWLDataProperty} as
		 * key and {@link String} as value. 
		 * @param a {@link Parser} that contains a {@link File} with {@link OWLDataProperty} and the {@link String} parameter associated.
		 */
		private void initMapDataProperty_Parameter(FileParser parserFileAssociation) {
			mapDataProperty_Parameter = new HashMap<OWLDataProperty, String[]>();

			List<DataParsed> listDataProperty_Parameter = parserFileAssociation.fileAssociationToList();
			
			for(DataParsed data : listDataProperty_Parameter) {
				OWLDataFactory factory = manager.getOWLDataFactory();
				mapDataProperty_Parameter.put(factory.getOWLDataProperty(data.getFirstBox()), data.getSecondBox().split("\\|"));
			}
		}
		
		/**
		 * Link in {@link Map} the {@link OWLDataProperty} contained in mapDataProperty_Parameter
		 * as key and the {@link String} as value contained in mapParameter_Value.
		 */
		private void initMapDataProperty_Value() {
			mapDataProperty_Value = new HashMap<OWLDataProperty, String>();
			Set<OWLDataProperty> setDataProperty = mapDataProperty_Parameter.keySet();
			for(OWLDataProperty elem : setDataProperty) {
				//Possible parameter
				String[] param = mapDataProperty_Parameter.get(elem);
				
				for(int i = 0; param.length>i; i++) {
					
					if(mapParameter_Value.get(param[i])!=null) {
						mapDataProperty_Value.put(elem, mapParameter_Value.get(param[i]));
					}
						
				}	
			}
		}
		
		/**
		 * Transform a {@link File} in a {@link Map} that contains {@link OWLObjectProperty} as
		 * key and {@link String} as value. 
		 * @param a {@link Parser} that contains a {@link File} with {@link OWLObjectProperty} and the {@link String} parameter associated.
		 */
		private void initMapObjectProperty_Parameter(FileParser parserFileAssociationObject) {
			mapObjectProperty_Parameter = new HashMap<OWLObjectProperty, String[]>();
			List<DataParsed> listObjectProperty_Parameter = parserFileAssociationObject.fileAssociationToList();
			for(DataParsed data : listObjectProperty_Parameter) {
				OWLDataFactory factory = manager.getOWLDataFactory();
				mapObjectProperty_Parameter.put(factory.getOWLObjectProperty(data.getFirstBox()), data.getSecondBox().split("\\|"));
			}
			
		}
		
		/**
		 * Link in {@link Map} the {@link OWLObjectProperty} contained in mapObjectProperty_Parameter
		 * as key and the {@link String} value contained in mapParameter_Value.
		 */
		private void initMapObjectProperty_Value() {
			mapObjectProperty_Value = new HashMap<OWLObjectProperty, String>();
			Set<OWLObjectProperty> setObjectProperty = mapObjectProperty_Parameter.keySet();
			for(OWLObjectProperty elem : setObjectProperty) {
				
				String[] param = mapObjectProperty_Parameter.get(elem);
				
				for(int i = 0; param.length>i; i++) {
					if(mapParameter_Value.get(param[i])!=null)
						mapObjectProperty_Value.put(elem, mapParameter_Value.get(param[i]));
				}	
			}
			
		}
		
		/**
		 * Load a ontology described in a OWL file with the OWL API manager 
		 * @param {@link File} that contains a ontology
		 */
		private void loadOntology (File file) {
			
			try {
				this.ontology = manager.loadOntologyFromOntologyDocument(file);

			} catch (OWLOntologyCreationException e) {
				e.printStackTrace();
			}
			
		}
		
		/**
		 * Save the ontology in a out file.
		 * @param a {@link OWLOntology} to save  
		 * @param a {@link IRI} about the ontology saved
		 */
		private void saveOntology(OWLOntology ontologyOutput, IRI outIRI) {
			try {
				manager.saveOntology(ontologyOutput,outIRI);
			} catch (OWLOntologyStorageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		private String labelHtml(String link) {
			return "_"+link.substring(link.lastIndexOf("/") + 1);
		}
		
		private String labelFile(String ch) {

			return "_"+ch;
		}
		
		
		private boolean fileAssociationView(String nameFile) {
			System.out.println(nameFile+" will be replaced, continue ? (y/n) ");

			Scanner sc = new Scanner(System.in);
			String str=sc.nextLine();
			
			if( str.contentEquals("y") || str.contentEquals("yes") || str.contentEquals("") ) {
				System.out.println(nameFile+" is replace.");
				return true;
			}
				
			else
				return false;
		}
}

