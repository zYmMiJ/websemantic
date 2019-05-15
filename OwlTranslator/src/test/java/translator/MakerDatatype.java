package translator;

import java.util.function.Predicate;
import java.util.stream.Stream;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.apache.log4j.Logger;

public class MakerDatatype extends MakerAxiom{
	
	private static final Logger LOG = Logger.getLogger(MakerDatatype.class);

	public MakerDatatype(OWLOntologyManager manager, OWLOntology ontology) {
		super(manager, ontology);
	}
	
	/**
	 * Complete a dataType with the data
	 * @param nameData
	 * @param data
	 */
	public void makeDataType(String nameData, String data) {
		
		OWLDataProperty dataOWL = getDataProprety(nameData);
		
		//Manage prefix
		String dataIRI = dataOWL.getIRI().getNamespace();;
		PrefixManager pm = new DefaultPrefixManager(null, null, dataIRI);
		
		//Make a Individual
		OWLNamedIndividual individualOWL = factory.getOWLNamedIndividual(":"+nameData, pm);
		
		//Make the axiom related to the DataType
		OWLAxiom axiomDataAssertion = factory.getOWLDataPropertyAssertionAxiom(dataOWL, individualOWL, data);
		OWLAxiom axiomDeclaration = factory.getOWLDeclarationAxiom(individualOWL);
		
		addAxiom(axiomDataAssertion);
		addAxiom(axiomDeclaration);
		
	}
	
	
	/**
	 * Find and return the OWLDataProperty corresponding to the fragment in ontology
	 * Use different step to find it.
	 * @param fragment
	 * @return
	 */
	private OWLDataProperty getDataProprety(String fragment) {
		
		//Step1 : declare a OWLAxiom steam
		Stream<OWLAxiom> streamStep1 = ontology.axioms();
		//Step2 : select the DATA_PROPERTY_RANGE Axiom
		Stream<OWLAxiom> streamStep2 = streamStep1.filter(isDataProperty());
		//Step3 : select the Axiom with the fragment corresponding
		Stream<OWLAxiom> streamStep3 = streamStep2.filter(fragmentEquals(fragment));
		//Step4 : select the interesting part in the stream
		OWLDataProperty result = (OWLDataProperty) streamStep3.findFirst().get().signature().findFirst().get();
		
		LOG.info("FOUND : "+result);
		
		return result;
		
	}
	
	private Predicate<OWLAxiom> isDataProperty()
	{
	    return p -> p.isOfType(AxiomType.DATA_PROPERTY_RANGE);
	}
	
	//TODO : change getFragment : pour cela il faut utiliser la signature et en créer une en amont
	@SuppressWarnings("deprecation")
	private Predicate<OWLAxiom> fragmentEquals(String fragment){
		//Test if the fragment of the axiom is the same that the parameter 
		return p -> p.dataPropertiesInSignature().findFirst().get().getIRI().getFragment().compareTo(fragment)==0;
	}
	
	
}
