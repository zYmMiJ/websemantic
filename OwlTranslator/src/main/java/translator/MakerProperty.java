package translator;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * is a specific {@link MakerAxiom}, 
 * for OWL Property {@link https://www.w3.org/TR/owl-ref/#Property}
 * @author Robin Couret
 */

public class MakerProperty extends MakerAxiom{
	
	private static final Logger LOG = Logger.getLogger(MakerProperty.class);
	
	public MakerProperty(OWLOntologyManager manager, OWLOntology ontology) {
		super(manager, ontology);
		
	}
	
	/**
	 * Add a property between two Individuals. 
	 * @param {@link OWLObjectPropertyExpression} is the property.
	 * @param {@link OWLNamedIndividual} is the subject.
	 * @param {@link OWLIndividual} is the object.
	 */
	public void makeProperty(OWLObjectPropertyExpression property, OWLNamedIndividual individualOWL, OWLIndividual object) {
		
		
		OWLAxiom axiomObjectProperty = factory.getOWLObjectPropertyAssertionAxiom(property, individualOWL, object);
		OWLAxiom axiomDeclaration = factory.getOWLDeclarationAxiom(individualOWL);
				
		addAxiom(axiomObjectProperty);
		addAxiom(axiomDeclaration);
				
		LOG.info("ADD : OWLDataProperty - "+property.toString()+" : "+individualOWL+" --> "+object);
	}

}
	


