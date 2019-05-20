package translator;
 
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLOntologyDocumentTarget;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class Translator2 {
	
		private OWLOntologyManager manager;
		private OWLOntology ontology;
		
		private File fileOwl;
		private File fileBash;
		
		private static final Logger LOG = Logger.getLogger(Translator.class);
		
		public Translator2(String fileOwlName, String fileBashName) {
			
			manager = OWLManager.createOWLOntologyManager();
			this.fileOwl = new File(fileOwlName);
			loadOntology(fileOwl);

			this.fileBash = new File(fileBashName);
			
		}
		
		public void run() throws IOException {
			
			Parser parserBash = new Parser(fileBash);
			Tools2 generator = new Tools2(ontology);
			
			//List of OWLClass, input : Ontology or selected manually (File .txt)
			List<OWLClass> listClass = generator.toListClass();
			
			//List of OWLClass with their OWLDataProperties, input : Ontology
			Map<OWLClass, List<OWLDataProperty>> mapClass_DataProperty = generator.toMapClass_DataProperty(listClass);
			
			//List of Parameter with its value, input : Bash
			Map<String, String> mapParameter_Value = new HashMap<String, String>();
			List<DataParsed> listParameter_Value = parserBash.fileToList();
			//TODO : DataParsed
			for(DataParsed data:listParameter_Value) {
				mapParameter_Value.put(data.getFirstBox(), data.getSecondBox());
			}
			
			//File of OWLDataProperty with the corresponding parameter, need to complete manually
			File fileDataProperty_Parameter = generator.toFileOWLDataProperty("DataProperty_Parameter.txt", mapClass_DataProperty);
			Parser parserFileAssociation = new Parser(fileDataProperty_Parameter);
			
			//List of OWLDataProperty with the corresponding parameter, input : selected manually (File .txt)
			Map<OWLDataProperty, String> mapDataProperty_Parameter = new HashMap<OWLDataProperty, String>();
			//TODO : DataParsed
			List<DataParsed> listDataProperty_Parameter = parserFileAssociation.fileToList();
			for(DataParsed data : listDataProperty_Parameter) {
				OWLDataFactory factory = manager.getOWLDataFactory();
				//TODO : ça marche ??
				mapDataProperty_Parameter.put(factory.getOWLDataProperty(data.getFirstBox()), data.getSecondBox());
			}
			
			//List of OWLDataProperty with the corresponding value, join between mapDataProperty_Parameter and mapParameter_Value
			Map<OWLDataProperty, String> mapDataProperty_Value = new HashMap<OWLDataProperty, String>();
			Set<OWLDataProperty> setDataProperty = mapDataProperty_Parameter.keySet();
			for(OWLDataProperty elem : setDataProperty) {
				mapDataProperty_Value.put(elem, mapParameter_Value.get(mapDataProperty_Parameter.get(elem)));
			}
			
			//Initialize the makers
			MakerDatatype makerData = new MakerDatatype(manager, ontology);
			MakerIndividual makerIndividual = new MakerIndividual(manager, ontology);
			
			//Give a label at the NamedIndiviual
			String label = mapParameter_Value.get("LABEL");
			
			for(OWLClass cls : listClass) {
				
				LOG.info("CLASS : "+cls);
				List<OWLDataProperty> list = mapClass_DataProperty.get(cls);
				LOG.info("DATA PROPERTIES : "+list);
				if(!list.isEmpty())
					LOG.info("VALUE : "+list.get(0)+" = "+mapDataProperty_Value.get(list.get(0)));
				
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

