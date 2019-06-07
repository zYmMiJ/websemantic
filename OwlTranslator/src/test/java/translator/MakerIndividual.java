package translator;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.apache.log4j.Logger;

/**
 * is a specific {@link MakerAxiom}, 
 * for OWLIndividual {@link https://www.w3.org/TR/owl-ref/#Individual}
 * @author Robin Couret
 */

public class MakerIndividual extends MakerAxiom{
		
	private OWLClass classOWL;
	private static final Logger LOG = Logger.getLogger(MakerIndividual.class);
	
	public MakerIndividual(OWLOntologyManager manager, OWLOntology ontology) {
		super(manager, ontology);
	}
	
	/**
	 * Make a NamedIndividual, individual of Class name, with a specific name.
	 * @param {@link ClassOWL}
	 * @param {@link String} the name of NamedIndividual
	 */
	public OWLNamedIndividual makeIndividual(OWLClass cls, String label) {
		
		this.classOWL = cls;
		
		//Manage prefix
		String nameClass = classOWL.getIRI().getShortForm();
		//String namespace = classOWL.getIRI().getNamespace();
		String namespace = "xpd:";
		
		//Make a Individual
		OWLNamedIndividual individualOWL = factory.getOWLNamedIndividual(namespace, nameClass+label);
		
		//Make the axiom related to the Individual
		OWLAxiom axiomClassAssertion = factory.getOWLClassAssertionAxiom(classOWL, individualOWL);
		OWLAxiom axiomDeclaration = factory.getOWLDeclarationAxiom(individualOWL);
		
		addAxiom(axiomClassAssertion);
		addAxiom(axiomDeclaration);
		
		LOG.info("ADD : Individual - "+individualOWL.getIRI());
		
		return individualOWL;
	}
	
}
