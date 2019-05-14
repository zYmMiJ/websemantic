package translator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

public class MakerDatatype extends MakerAxiom{

	public MakerDatatype(OWLOntologyManager manager, OWLOntology ontology) {
		super(manager, ontology);
	}
	
	public void makeDataType(String nameData, String data) {
		
		OWLDataProperty dataOWL = findDataProprety(nameData);
		
		IRI ontologyIRI = ontology.getOntologyID().getOntologyIRI().get();
		PrefixManager pm = new DefaultPrefixManager(null, null, ontologyIRI.toString());
		
		OWLNamedIndividual individualOWL = factory.getOWLNamedIndividual(":"+nameData, pm);
		
		OWLAxiom axiomDataAssertion = factory.getOWLDataPropertyAssertionAxiom(dataOWL, individualOWL, data);
		OWLAxiom axiomDeclaration = factory.getOWLDeclarationAxiom(individualOWL);
		
		//TODO : make a list 
		AddAxiom addAxiomDataAssertion = new AddAxiom(ontology, axiomDataAssertion);
		AddAxiom addAxiomDeclaration = new AddAxiom(ontology, axiomDeclaration);
		
		//TODO : analyse OWLOntologyChange in addAxiom(OWLOntologyChange)
		manager.applyChange(addAxiomDataAssertion);
		manager.applyChange(addAxiomDeclaration);
	}
	
	//TODO : update the deprecation methods --> use stream Method
	//use Searcher ? : http://owlcs.github.io/owlapi/apidocs_5/org/semanticweb/owlapi/search/Searcher.html
	private OWLDataProperty findDataProprety(String fragment) {
					
		for (OWLAxiom dt :  ontology.getAxioms()) {		
				if (dt.isOfType(AxiomType.DATA_PROPERTY_RANGE)) {
							
					Set set = dt.getDataPropertiesInSignature();
					List<OWLDataProperty> list = new ArrayList<OWLDataProperty>(set);
					OWLDataProperty elem = list.get(0);
							
					if (elem.getIRI().getFragment().compareTo(fragment)==0)
							return elem;	
						}
					}
				return null;
			}
}
