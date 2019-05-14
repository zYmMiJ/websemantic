package translator;

import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

public class MakerIndividual extends MakerAxiom{
		
	private OWLClass classOWL;
	
	
	public MakerIndividual(OWLOntologyManager manager, OWLOntology ontology) {
		super(manager, ontology);
	}
	
	public void makeIndividual(String nameClass, String nameIndividual) {
		
		this.classOWL = findAndMakeClass(nameClass);
		
		IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
		PrefixManager pm = new DefaultPrefixManager(null, null, ontologyIRI.toString());
		
		OWLNamedIndividual individualOWL = factory.getOWLNamedIndividual(":"+nameIndividual, pm);
		
		OWLAxiom axiomClassAssertion = factory.getOWLClassAssertionAxiom(classOWL, individualOWL);
		OWLAxiom axiomDeclaration = factory.getOWLDeclarationAxiom(individualOWL);
		
		//TODO : make a list 
		AddAxiom addAxiomClassAssertion = new AddAxiom(ontology, axiomClassAssertion);
		AddAxiom addAxiomDeclaration = new AddAxiom(ontology, axiomDeclaration);
		
		//TODO : analyse OWLOntologyChange in addAxiom(OWLOntologyChange)
		manager.applyChange(addAxiomClassAssertion);
		manager.applyChange(addAxiomDeclaration);
		
	}
	
	//TODO : update the deprecation methods --> use stream Method
		//use Searcher ? : http://owlcs.github.io/owlapi/apidocs_5/org/semanticweb/owlapi/search/Searcher.html
		private OWLClass findAndMakeClass(String fragment) {
			
			for (OWLClass cls :  ontology.getClassesInSignature()) {
				if (cls.getIRI().getFragment().compareTo(fragment)==0){
					OWLClass theClass = cls;
					return theClass;
				}
			}
			return null;
		}
}
