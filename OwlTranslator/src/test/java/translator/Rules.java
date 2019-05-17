package translator;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Rules {
	
	private static int experimentParameters = 0;
	private static int experimentInfoParameters = 1;
	private static int documentationParameters = 2;
	private static int experimentVariation = 3;
	private static int defaultValues = 4;
	
	// Liste des paramètres provenant du parser
	private List<Parameter> listdataparser;
	
	// Liste des classes  à créer
	private List<String> listnomclasse;
	
	public Rules(List<Parameter> l) {
		this.listdataparser = l;
	}
	
	/*
	 * Association simples entres les classes owl et nos données d'experience
	 */
	public List<Map<String, String>> association() {
		
		List<Map<String, String>> res = null;
		
		// Règles Hypothèses
		Map<String,String> hyp = Collections.singletonMap("HypothesisFormulation",listdataparser.get(experimentVariation).getOneParam("Hypothesis") );
		res.add( hyp ); 

		return res;
	}

}
