package translator;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
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
	public void makeDataType(OWLDataProperty dataProperty, String data, OWLNamedIndividual individualOWL) {
		
		//Make the axiom related to the DataType
		OWLAxiom axiomDataAssertion = factory.getOWLDataPropertyAssertionAxiom(dataProperty, individualOWL, data);
		OWLAxiom axiomDeclaration = factory.getOWLDeclarationAxiom(individualOWL);
		
		addAxiom(axiomDataAssertion);
		addAxiom(axiomDeclaration);
		
		LOG.info("ADD : OWLDataProperty - "+dataProperty.toString()+" : "+data);
	}
}
