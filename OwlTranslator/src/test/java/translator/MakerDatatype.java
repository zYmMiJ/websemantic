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

/**
 * Specific {@link MakerAxiom} for dataProperty.
 * @param manager
 * @param ontology
 */

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
		
		OWLAxiom axiomDataAssertion = null;
		if(dataRange.toString().contains("int"))
			axiomDataAssertion = factory.getOWLDataPropertyAssertionAxiom(dataProperty, individualOWL, Integer.parseInt(data));
		else
			axiomDataAssertion = factory.getOWLDataPropertyAssertionAxiom(dataProperty, individualOWL, data);
			
		//Make the axiom related to the DataType
		OWLDataPropertyRangeAxiom axiomRange = factory.getOWLDataPropertyRangeAxiom(dataProperty, dataRange);
		OWLAxiom axiomDeclaration = factory.getOWLDeclarationAxiom(individualOWL);
		
		addAxiom(axiomRange);
		addAxiom(axiomDataAssertion);
		addAxiom(axiomDeclaration);
		
		
		
		
		LOG.info("ADD : OWLDataProperty - "+dataProperty.toString()+" : "+data);
	}
	
	private Predicate<OWLDataProperty> dataPropertyEquals(OWLDataProperty ppt){
		return p -> p.dataPropertiesInSignature().findFirst().get().equals(ppt);
	}
}
