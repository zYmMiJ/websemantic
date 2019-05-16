package translator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class Tools {
	
	private static OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	private static File fileOwl = new File("prototype1.owl");
	private static OWLOntology ontology;
	private static final Logger LOG = Logger.getLogger(Tools.class);
	
	
	public static void main(String[] args) throws IOException {
		BasicConfigurator.configure();
		try {
			ontology  = manager.loadOntologyFromOntologyDocument(fileOwl);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		associateDataPpt();
		//listClass();
		
	}
	
	private static void listClass() throws IOException {
		FileOutputStream outputStream;
		
		outputStream = new FileOutputStream("associationTab.txt");
		LOG.info("A");
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
		
		Set<OWLClass> set = ontology.getClassesInSignature();
		
		for (OWLClass cls : set) {
			String nameClass = cls.getIRI().getFragment()+"=";
			LOG.info("AJOUT : "+nameClass);
			bw.write(nameClass);
			bw.newLine();
			
		}
		bw.close();
		
	}
	
	private static void associateDataPpt(){
		
		Set<OWLClass> set = ontology.getClassesInSignature();
		
		//Step1 : declare a OWLAxiom steam
				Stream<OWLAxiom> streamStep1 = ontology.axioms();
				streamStep1.forEach(System.out::println);
				
				//Step2 : select the DATA_PROPERTY_RANGE Axiom
				Stream<OWLAxiom> streamStep2 = streamStep1.filter(isDataPropertyDomain());
				
				//streamStep2.forEach(System.out::println);
				
				//Step3 : select the Axiom with the fragment corresponding
				//Stream<OWLAxiom> streamStep3 = streamStep2.filter(fragmentEquals(fragment));
		
	}
	
	private static Predicate<OWLAxiom> isDataPropertyDomain()
	{
	    return p -> p.isOfType(AxiomType.DATA_PROPERTY_DOMAIN);
	}
	
	//TODO : change getFragment : pour cela il faut utiliser la signature et en créer une en amont
	@SuppressWarnings("deprecation")
	private static Predicate<OWLAxiom> ClassEquals(String fragment){
		//Test if the fragment of the axiom is the same that the parameter 
		return p -> p.dataPropertiesInSignature().findFirst().get().getIRI().getFragment().compareTo(fragment)==0;
	}

}
