package translator;

import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * Represent the Axiom makers.
 * Make a axiom means to add a Axiom in a ontology instance. 
 * More information about OWL axiom : @see <a href="https://www.w3.org/TR/owl2-syntax/#Axioms">
 * @author Robin Couret
 */

public abstract class MakerAxiom {
	
	protected OWLOntology ontology;
	protected OWLDataFactory factory;
	protected OWLOntologyManager manager;
	
	public MakerAxiom(OWLOntologyManager manager, OWLOntology ontology) {
		this.ontology = ontology;
		this.manager = manager;
		this.factory = this.manager.getOWLDataFactory();
	}
	
	//TODO : Gestion des erreurs
	/**
	 * Add a axiom in a ontology instance.
	 * @param {@link OWLAxiom}
	 */
	protected void addAxiom(OWLAxiom axiom) {
		AddAxiom addAxiom = new AddAxiom(ontology, axiom);
		manager.applyChange(addAxiom);
	}

}
