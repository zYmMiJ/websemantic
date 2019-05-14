package translator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
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
	
	

}
