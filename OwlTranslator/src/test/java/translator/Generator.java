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

public class Generator {
	
	private OWLOntology ontology;
	
	private static final Logger LOG = Logger.getLogger(Generator.class);
	
	public Generator(OWLOntology ontology) {
		this.ontology=ontology;
	}
	
	public List<OWLClass> toListClass(){
		
		Stream<OWLClass> streamClass = ontology.classesInSignature();
		return streamClass.collect(Collectors.toList());	
	}
	
	public Map<OWLClass, List<OWLDataProperty>> toMapClass_DataProperty(List<OWLClass> listClass){
		
		Map<OWLClass, List<OWLDataProperty>> mapClass_DataProperty = new HashMap<OWLClass, List<OWLDataProperty>>();
		
		for (OWLClass cls : listClass)
			mapClass_DataProperty.put(cls, toListDataProperty(cls));
			
		return mapClass_DataProperty;
	}
	
	private List<OWLDataProperty> toListDataProperty(OWLClass cls){
		
		//Step1 : declare a OWLAxiom stream
		Stream<OWLAxiom> streamStep1 = ontology.axioms();
		//Step2 : select the stream of DATA_PROPERTY_RANGE Axiom
		Stream<OWLAxiom> streamStep2 = streamStep1.filter(isDataPropertyDomain());
		//Step3 : select the Axiom with fragment
		Stream<OWLAxiom> streamStep3 = streamStep2.filter(classEquals(cls));
		
		List<OWLAxiom> listOWLAxiom = streamStep3.collect(Collectors.toList());
		List<OWLDataProperty> listOWLDataProperty = new ArrayList<OWLDataProperty>();
		
		for(OWLAxiom element : listOWLAxiom)
			listOWLDataProperty.add(element.dataPropertiesInSignature().findFirst().get());
		
		return listOWLDataProperty;
	}
	
	public File toFileOWLDataProperty(String filePath, Map<OWLClass, List<OWLDataProperty>> mapClass_DataProperty) throws IOException {
		
		FileOutputStream outputStream;
		outputStream = new FileOutputStream(filePath);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
		
		Set<OWLClass> setOWLClass = mapClass_DataProperty.keySet();
		
		for(OWLClass cls : setOWLClass) {
			for(OWLDataProperty dataProperty : mapClass_DataProperty.get(cls)) {
				String line = dataProperty.getIRI()+"=";
				bw.write(line);
				bw.newLine();
			}
		}
		bw.close();
		
		LOG.info("File created : "+filePath);
		
		return new File(filePath);
	}
	
	private Predicate<OWLAxiom> isDataPropertyDomain()
	{
	    return p -> p.isOfType(AxiomType.DATA_PROPERTY_DOMAIN);
	}
	
	private Predicate<OWLAxiom> classEquals(OWLClass cls){
		//Test if the Class in the axiom is the same that the Class in parameter 
		return p -> p.classesInSignature().findFirst().get().equals(cls);
	}

}
