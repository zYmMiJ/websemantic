package translator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLOntology;

import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;


public class Tools {
	
	private OWLOntology ontology;
	
	private static final Logger LOG = Logger.getLogger(Tools.class);
	
	public Tools(OWLOntology ontology) {
		this.ontology=ontology;
	}
	
	//Make a file with the list of Class
	public File listOWLClasstoFile(String filePath) throws IOException {
		
		FileOutputStream outputStream;
		outputStream = new FileOutputStream(filePath);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
		
		//TODO
		Set<OWLClass> set = ontology.getClassesInSignature();
		
		for (OWLClass cls : set) {
			String nameClass = cls.getIRI().getFragment()+"=";
			bw.write(nameClass);
			bw.newLine();
		}
		bw.close();
		
		LOG.info("File created : "+filePath);
		
		
		return new File(filePath);
		
	}
	
	public File listOWLDataPptToFile(String filePath) throws IOException {
		
		FileOutputStream outputStream;
		outputStream = new FileOutputStream(filePath);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
		
		//TODO
		Set<OWLDataProperty> set = ontology.getDataPropertiesInSignature();
		
		for (OWLDataProperty dataPpt : set) {
			String nameClass = dataPpt.getIRI().getFragment()+"=";
			bw.write(nameClass);
			bw.newLine();
		}
		bw.close();
		
		LOG.info("File created : "+filePath);
		
		
		return new File(filePath);
		
	}
	
	//TODO : pour plusieurs dataPPT
	public Map<String, List<String>> joinClassAndDataProperty(){
		
		HashMap<String, List<String>> map = new HashMap<String, List<String>>();
		Set<OWLClass> set = ontology.getClassesInSignature();
		
		for (OWLClass cls : set) {
			String nameClass = cls.getIRI().getFragment();
			
			List<String> data = 
					associateDataPpt(nameClass) != null ?
					associateDataPpt(nameClass) : new ArrayList();
			map.put(nameClass, data);
		}
		return map;
	}
	
	//Search a OWLDataProperty with the fragment of the Domain Class
	private List<String> associateDataPpt(String fragment){
	
		//Step1 : declare a OWLAxiom stream
		Stream<OWLAxiom> streamStep1 = ontology.axioms();
		//Step2 : select the stream of DATA_PROPERTY_RANGE Axiom
		Stream<OWLAxiom> streamStep2 = streamStep1.filter(isDataPropertyDomain());
		//Step3 : select the Axiom with fragment
		Stream<OWLAxiom> streamStep3 = streamStep2.filter(fragmentEquals(fragment));
		
		List<OWLAxiom> list = streamStep3.collect(Collectors.toList());
		List<String> listFragment = new ArrayList<String>();
		
		for(OWLAxiom axiom : list)
			listFragment.add(getFragmentInDataPpt(axiom));
		
		return listFragment;
			
		//Step 4 : return the axiom
		/*Optional<OWLAxiom> result = streamStep3.findAny();
		
		if(result.isPresent())
			return result.get().dataPropertiesInSignature().findFirst().get().getIRI().getFragment();
		else
			return null;*/	
	}
	
	private String getFragmentInDataPpt(OWLAxiom axiom) {
		return axiom.dataPropertiesInSignature().findFirst().get().getIRI().getFragment();
	}
	
	private Predicate<OWLAxiom> isDataPropertyDomain()
	{
	    return p -> p.isOfType(AxiomType.DATA_PROPERTY_DOMAIN);
	}
	
	//TODO : change getFragment : pour cela il faut utiliser la signature et en créer une en amont
	@SuppressWarnings("deprecation")
	private Predicate<OWLAxiom> fragmentEquals(String fragment){
		//Test if the fragment of the axiom is the same that the parameter 
		
		return p -> p.classesInSignature().findFirst().get().getIRI().getFragment().compareTo(fragment)==0;
	}

}
