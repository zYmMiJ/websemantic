package translator;

import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class MakerProperty extends MakerAxiom{
	
	private static final Logger LOG = Logger.getLogger(MakerProperty.class);
	
	public MakerProperty(OWLOntologyManager manager, OWLOntology ontology) {
		super(manager, ontology);
		
	}
	
	public void makeProperty(OWLObjectPropertyExpression property, OWLNamedIndividual individualOWL, OWLIndividual object) {
		
		
		OWLAxiom axiomObjectProperty = factory.getOWLObjectPropertyAssertionAxiom(property, individualOWL, object);
		LOG.info("axiomObjectProperty"+axiomObjectProperty);
		OWLAxiom axiomDeclaration = factory.getOWLDeclarationAxiom(individualOWL);
				
		addAxiom(axiomObjectProperty);
		addAxiom(axiomDeclaration);
				
		LOG.info("ADD : OWLDataProperty - "+property.toString()+" : "+individualOWL+" --> "+object);
	}

}
	


