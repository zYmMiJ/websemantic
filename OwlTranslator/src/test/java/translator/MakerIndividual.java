package translator;

import java.util.Optional;
import java.util.stream.Stream;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.apache.log4j.Logger;

public class MakerIndividual extends MakerAxiom{
		
	private OWLClass classOWL;
	private static final Logger LOG = Logger.getLogger(MakerIndividual.class);
	
	public MakerIndividual(OWLOntologyManager manager, OWLOntology ontology) {
		super(manager, ontology);
	}
	
	/**
	 * Make a NamedIndividual, individual of Class name, with a specific name.
	 * @param nameClass
	 * @param nameIndividual
	 */
	public void makeIndividual(String nameClass, String nameIndividual) {
		
		this.classOWL = getClass(nameClass);
		
		//Manage prefix
		String classIRI = classOWL.getIRI().getNamespace();
		PrefixManager pm = new DefaultPrefixManager(null, null, classIRI);
		
		//TODO : tester si l'individual n'existe pas déjà
		
		//Make a Individual
		OWLNamedIndividual individualOWL = factory.getOWLNamedIndividual(":"+nameIndividual, pm);
		
		//Make the axiom related to the Individual
		OWLAxiom axiomClassAssertion = factory.getOWLClassAssertionAxiom(classOWL, individualOWL);
		OWLAxiom axiomDeclaration = factory.getOWLDeclarationAxiom(individualOWL);
		
		addAxiom(axiomClassAssertion);
		addAxiom(axiomDeclaration);
	}
	
	/**
	 * Find and return the class corresponding to the fragment in ontology
	 * @param fragment
	 * @return OWLClass finded
	 */
	//TODO : Gestion des erreurs
	private OWLClass getClass(String fragment) {
		//Declare a stream with the ontology's classes
		Stream<OWLClass> stream = ontology.classesInSignature();
		//Filter th stream to find the fragment
		@SuppressWarnings("deprecation")
		Optional<OWLClass> result = stream.filter(p -> p.getIRI().getFragment().compareTo(fragment)==0).findFirst();
		
		LOG.info("FOUND : "+result.get());
		
		return result.get();
	}
	
}
