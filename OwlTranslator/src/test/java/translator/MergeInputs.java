package translator;

import java.util.HashMap;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public class MergeInputs {
	
	public MergeInputs() {
		
	}
	
	public static void merge(String fileOwlName, String targetFile, String targetHtml) {
		
		Translator translatorFile = new Translator(fileOwlName, targetFile, "FILE");
		Translator translatorHtml = new Translator(fileOwlName, targetHtml, "HTML");
		Translator translatorMerged = new Translator(fileOwlName, null, null);
		
		translatorFile.run(false);
		translatorHtml.run(false);
		
		HashMap<OWLDataProperty, String> mapDataFile = new HashMap<OWLDataProperty, String>(translatorFile.getMapDataProperty_Value());
		HashMap<OWLDataProperty, String> mapDataHtml = new HashMap<OWLDataProperty, String>(translatorHtml.getMapDataProperty_Value());
		HashMap<OWLDataProperty, String> mapDataMerged = new HashMap<OWLDataProperty, String>();
		
		HashMap<OWLObjectProperty, String> mapObjectFile = new HashMap<OWLObjectProperty, String>(translatorFile.getMapObjectProperty_Value());
		HashMap<OWLObjectProperty, String> mapObjectHtml = new HashMap<OWLObjectProperty, String>(translatorHtml.getMapObjectProperty_Value());
		HashMap<OWLObjectProperty, String> mapObjectMerged = new HashMap<OWLObjectProperty, String>();
		
		
		if(true) //priorité
		{
			Set<OWLDataProperty> setDataFile = translatorFile.getMapDataProperty_Parameter().keySet();
			
			//Initialize MapDataMerged with mapDataFile and mapDataHtml
			for(OWLDataProperty ppt :setDataFile) {
				
				if(mapDataFile.get(ppt)==null) {
					System.out.println("Data Html: "+ppt+ " : "+mapDataHtml.get(ppt));
					mapDataMerged.put(ppt, mapDataHtml.get(ppt));
				}
					
				else {
					System.out.println("Data File : "+ppt+ " : "+mapDataFile.get(ppt));
					mapDataMerged.put(ppt, mapDataFile.get(ppt));
				}
					
			}
			
			translatorMerged.setMapDataProperty_Value(mapDataMerged);
			
			Set<OWLObjectProperty> setObjectFile = mapObjectFile.keySet();
			
			//Initialize ObjectDataMerged with mapDataFile and mapDataHtml
			for(OWLObjectProperty ppt : setObjectFile) {
				if(mapObjectFile.get(ppt)==null)
					mapObjectMerged.put(ppt, mapObjectHtml.get(ppt));
				else
					mapObjectMerged.put(ppt, mapObjectFile.get(ppt));
			}
			
			translatorMerged.setMapObjectProperty_Value(mapObjectMerged);
			
			translatorMerged.run(true);
			
		}
			
	}

}
