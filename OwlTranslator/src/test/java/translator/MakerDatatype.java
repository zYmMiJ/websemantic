package translator;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.util.function.Predicate;

import org.apache.log4j.Logger;

public class MakerDatatype extends MakerAxiom{
	
	private static final Logger LOG = Logger.getLogger(MakerDatatype.class);

	public MakerDatatype(OWLOntologyManager manager, OWLOntology ontology) {
		super(manager, ontology);
	}
	
	/**
	 * Complete a dataType with the data
	 * @param dataProperty
	 * @param data
	 */
	public void makeDataType(OWLDataProperty dataProperty, OWLDataRange dataRange, String data, OWLNamedIndividual individualOWL) {
		
		//Make the axiom related to the DataType
		OWLAxiom axiomDataAssertion = factory.getOWLDataPropertyAssertionAxiom(dataProperty, individualOWL, data);
		OWLAxiom axiomDeclaration = factory.getOWLDeclarationAxiom(individualOWL);
		OWLDataPropertyRangeAxiom axiomRange = factory.getOWLDataPropertyRangeAxiom(dataProperty, dataRange);
		
		addAxiom(axiomRange);
		addAxiom(axiomDataAssertion);
		addAxiom(axiomDeclaration);
		
		//ontology.dataPropertyAssertionAxioms(individualOWL).forEach(System.out::println);
		
		
		LOG.info("ADD : OWLDataProperty - "+dataProperty.toString()+" : "+data);
	}
	
	private Predicate<OWLDataProperty> dataPropertyEquals(OWLDataProperty ppt){
		return p -> p.dataPropertiesInSignature().findFirst().get().equals(ppt);
	}
}
