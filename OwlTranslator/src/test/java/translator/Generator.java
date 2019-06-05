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
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * Transform OWL knowledge and raw data that can be manipulated by {@link Translator}.
 * Generate different Map, List and File.
 * @author Robin Couret
*/

public class Generator {
	
	private OWLOntology ontology;
	
	private static final Logger LOG = Logger.getLogger(Generator.class);
	
	public Generator(OWLOntology ontology) {
		this.ontology=ontology;
	}
	
	/** 
	 * List the classes contained in the Ontology loaded.
	 * @return a {@link List} that contains {@link OWLClass}.
	 */
	public List<OWLClass> toListClass(){
		
		Stream<OWLClass> streamClass = ontology.classesInSignature();
		return streamClass.collect(Collectors.toList());	
	}
	
	/**
	 * Associate in a map the OWL classes and their OWLDataProperty.
	 * @param the {@link List} that contains {@link OWLClass} about the Ontology.
	 * @return a {@link Map} collection that contains {@link OWLClass} as
	 * key and {@link OWLDataProperty} as value.
	 */
	public Map<OWLClass, List<OWLDataProperty>> toMapClass_DataProperty(List<OWLClass> listClass){
		
		Map<OWLClass, List<OWLDataProperty>> mapClass_DataProperty = new HashMap<OWLClass, List<OWLDataProperty>>();
		
		for (OWLClass cls : listClass)
			mapClass_DataProperty.put(cls, toListDataProperty(cls));
			
		return mapClass_DataProperty;
	}
	
	/**
	 * List the DataProperty at a specific Class.
	 * @param a {@link OWLClass}
	 * @return a {@link List} that contains the {@link DataProperty} about a {@link OWLClass}
	 */
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
	
	/**
	 * Write in a File the {@link OWLDataProperty}, 
	 * the File have to completed by the user with 
	 * the association between real data and {@link OWLDataProperty}.
	 * @param filePath {@link String}
	 * @param a {@link Map} that contains {@link OWLClass} as
	 * key and a list of {@link OWLDataProperty} as value.
	 * @return a File with the list of DataProperty, 
	 * the File need to be completed manually.
	 * @throws IOException
	 */
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
	
	/**
	 * Associate in a map the OWL classes and their OWLObjectProperty.
	 * @param the {@link List} that contains {@link OWLClass} about the Ontology.
	 * @return a {@link Map} collection that contains {@link OWLClass} as
	 * key and {@link OWLObjectProperty} as value.
	 */
	public Map<OWLClass, List<OWLObjectProperty>> toMapClass_ObjectProperty(List<OWLClass> listClass){
		
		Map<OWLClass, List<OWLObjectProperty>> mapClass_ObjectProperty = new HashMap<OWLClass, List<OWLObjectProperty>>();
		
		for (OWLClass cls : listClass)
			mapClass_ObjectProperty.put(cls, toListObjectProperty(cls));
			
		return mapClass_ObjectProperty;
	}
	
	/**
	 * List the ObjectProperty at a specific Class.
	 * @param a {@link OWLClass}
	 * @return a {@link List} that contains the {@link objectProperty} about a {@link OWLClass}
	 */
	public Map<OWLObjectProperty, List<OWLClass>> toMapObjectPropertyDomain_Range(List<OWLClass> listClass){
		
		Map<OWLObjectProperty, List<OWLClass>> mapObjectPropertyDomain_Range = new HashMap<OWLObjectProperty, List<OWLClass>>();
		
		Map<OWLObjectProperty, OWLClass> mapDomain = new HashMap<OWLObjectProperty, OWLClass>();
		Map<OWLObjectProperty, OWLClass> mapRange = new HashMap<OWLObjectProperty, OWLClass>();
		
		for(OWLClass cls : listClass){	
				//DOMAIN
				//Step1 : declare a OWLAxiom stream
				Stream<OWLAxiom> streamStepD1 = ontology.axioms();
				//Step2 : select the stream of OBJECT_PROPERTY_RANGE Axiom
				Stream<OWLAxiom> streamStepD2 = streamStepD1.filter(isDataObjectDomain());
				//Step3 : select the Axiom with fragment
				Stream<OWLAxiom> streamStepD3 = streamStepD2.filter(classEquals(cls));
				
				List<OWLAxiom> listDomain = streamStepD3.collect(Collectors.toList());
				for(OWLAxiom a : listDomain) {
					mapDomain.put(a.objectPropertiesInSignature().findAny().get(), a.classesInSignature().findFirst().get());
				}
				
				//RANGE
				//Step1 : declare a OWLAxiom stream
				Stream<OWLAxiom> streamStepR1 = ontology.axioms();
				//Step2 : select the stream of DATA_PROPERTY_RANGE Axiom
				Stream<OWLAxiom> streamStepR2 = streamStepR1.filter(isDataObjectRange());
				//Step3 : select the Axiom with fragment
				Stream<OWLAxiom> streamStepR3 = streamStepR2.filter(classEquals(cls));
				
				List<OWLAxiom> listRange = streamStepR3.collect(Collectors.toList());
				for(OWLAxiom a : listRange) {
					mapRange.put(a.objectPropertiesInSignature().findAny().get(), a.classesInSignature().findFirst().get());
				}
		}
		
		Set<OWLObjectProperty> setOWLObject = mapDomain.keySet();
		
		for(OWLObjectProperty ppt : setOWLObject) {
			List<OWLClass> listDomain_Range = new ArrayList<OWLClass>();
			listDomain_Range.add(mapDomain.get(ppt));
			listDomain_Range.add(mapRange.get(ppt));
			mapObjectPropertyDomain_Range.put(ppt, listDomain_Range);
		}
			
		
		return mapObjectPropertyDomain_Range;
	}
	
	/**
	 * Write in a File the {@link OWLObjectProperty}, 
	 * the File have to completed by the user with 
	 * the association between real data and {@link OWLObjectProperty}.
	 * @param filePath {@link String}
	 * @param a {@link Map} that contains {@link OWLClass} as
	 * key and a list of {@link OWLObjectProperty} as value.
	 * @return a File with the list of DataProperty, 
	 * the File need to be completed manually.
	 * @throws IOException
	 */
	public File toFileOWLObjectProperty(String filePath, Map<OWLClass, List<OWLObjectProperty>> mapClass_ObjectProperty) throws IOException {
		
		FileOutputStream outputStream;
		outputStream = new FileOutputStream(filePath);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
		
		Set<OWLClass> setOWLClass = mapClass_ObjectProperty.keySet();
		
		for(OWLClass cls : setOWLClass) {
			for(OWLObjectProperty objectProperty : mapClass_ObjectProperty.get(cls)) {
				String line = objectProperty.getIRI()+"=";
				bw.write(line);
				bw.newLine();
			}
		}
		bw.close();
		
		LOG.info("File created : "+filePath);
		
		return new File(filePath);
	}
	
	public Map<OWLDataProperty, OWLDataRange> toMapDataProperty_DataRange(List<OWLDataProperty> listDataProperty){
		Map<OWLDataProperty, OWLDataRange> MapDataProperty_DataRange = new HashMap<OWLDataProperty, OWLDataRange>();
		
		for(OWLDataProperty ppt : listDataProperty) {
			System.out.print(ppt+" : ");
			
			
			//Step1 : declare a OWLAxiom stream
			Stream<OWLAxiom> streamStep1 = ontology.axioms();
			//Step2 : select the stream of DATA_PROPERTY_RANGE Axiom
			Stream<OWLAxiom> streamStep2 = streamStep1.filter(isDataPropertyRange());
			//Step3 : select the OWLDataProperty
			Stream<OWLAxiom> streamStep3 = streamStep2.filter(dataPropertyEquals(ppt));
			OWLDataRange result = (streamStep3.findFirst().get().datatypesInSignature().findFirst().get());
			MapDataProperty_DataRange.put(ppt, result);
		}
		
		return MapDataProperty_DataRange;
		
		
	}
	
	private List<OWLObjectProperty> toListObjectProperty(OWLClass cls){
		
		//Step1 : declare a OWLAxiom stream
		Stream<OWLAxiom> streamStep1 = ontology.axioms();
		//Step2 : select the stream of OBJECT_PROPERTY_RANGE Axiom
		Stream<OWLAxiom> streamStep2 = streamStep1.filter(isDataObjectDomain());
		//Step3 : select the Axiom with fragment
		Stream<OWLAxiom> streamStep3 = streamStep2.filter(classEquals(cls));
		
		List<OWLAxiom> listOWLAxiom = streamStep3.collect(Collectors.toList());
		List<OWLObjectProperty> listOWLObjectProperty = new ArrayList<OWLObjectProperty>();
		
		for(OWLAxiom element : listOWLAxiom)
			listOWLObjectProperty.add(element.objectPropertiesInSignature().findFirst().get());
		
		return listOWLObjectProperty;
	}
	
	
	private Predicate<OWLAxiom> isDataPropertyDomain()
	{
	    return p -> p.isOfType(AxiomType.DATA_PROPERTY_DOMAIN);
	}
	
	private Predicate<OWLAxiom> classEquals(OWLClass cls){
		//Test if the Class in the axiom is the same that the Class in parameter 
		return p -> p.classesInSignature().findFirst().get().equals(cls);
	}
	
	private Predicate<OWLAxiom> dataPropertyEquals(OWLDataProperty ppt){
		return p -> p.dataPropertiesInSignature().findFirst().get().equals(ppt);
	}
	
	private Predicate<OWLAxiom> isDataObjectDomain()
	{
	    return p -> p.isOfType(AxiomType.OBJECT_PROPERTY_DOMAIN);
	}
	
	private Predicate<OWLAxiom> isDataObjectRange()
	{
	    return p -> p.isOfType(AxiomType.OBJECT_PROPERTY_RANGE);
	}
	
	private Predicate<OWLAxiom> isDataPropertyRange()
	{
	    return p -> p.isOfType(AxiomType.DATA_PROPERTY_RANGE);
	}

}
