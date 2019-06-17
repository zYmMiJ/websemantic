package translator;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import org.apache.log4j.Logger;

/**
 * is a specific {@link MakerAxiom}, 
 * for OWL DataType @see <a href="https://www.w3.org/TR/owl-ref/#Datatype">
 * @author Robin Couret
 */

public class MakerDatatype extends MakerAxiom{
		
	public MakerDatatype(OWLOntologyManager manager, OWLOntology ontology) {
		super(manager, ontology);
	}
	
	/**
	 * Add a data property at a Individual. 
	 * @param {@link OWLDataProperty} is the data property. 
	 * @param {@link OWLDataRange}, OWL specification about data type. 
	 * @param {@link String} value. 
	 * @param {@link OWLNamedIndividual} individual where the data is linked.
	 */
	public void makeDataType(OWLDataProperty dataProperty, OWLDataRange dataRange, String data, OWLNamedIndividual individualOWL) {
		
		OWLAxiom axiomDataAssertion = null;
		/*if(dataRange.toString().contains("int"))
			axiomDataAssertion = factory.getOWLDataPropertyAssertionAxiom(dataProperty, individualOWL, Integer.parseInt(data));
		else*/
			axiomDataAssertion = factory.getOWLDataPropertyAssertionAxiom(dataProperty, individualOWL, data);
			
		//Make the axiom related to the DataType
		OWLDataPropertyRangeAxiom axiomRange = factory.getOWLDataPropertyRangeAxiom(dataProperty, dataRange);
		OWLAxiom axiomDeclaration = factory.getOWLDeclarationAxiom(individualOWL);
		
		addAxiom(axiomRange);
		addAxiom(axiomDataAssertion);
		addAxiom(axiomDeclaration);
	}
	
}
