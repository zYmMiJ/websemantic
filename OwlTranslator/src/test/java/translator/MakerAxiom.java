package translator;

import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

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
	protected void addAxiom(OWLAxiom axiom) {
		AddAxiom addAxiom = new AddAxiom(ontology, axiom);
		manager.applyChange(addAxiom);
		//System.out.println(axiom+" : "+manager.applyChange(addAxiom));
	}

}
