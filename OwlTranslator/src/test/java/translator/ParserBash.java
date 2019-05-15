package translator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserBash extends Parser{
	
	private static Pattern pattern;
    private static Matcher matcher;
    
    private final static Pattern ENV_VAR_PATTERN =
    	    Pattern.compile("\\$\\{([A-Za-z0-9_.-]+)(?::([^\\}]*))?\\}");
	
	public ParserBash(File file) {
		super(file);
		// TODO Auto-generated constructor stub
	}

	public void printDataParsed(List<Parameter> list) {
		Iterator<Parameter> it = list.iterator();
		 
		while (it.hasNext()) {
			Parameter s = it.next();
			System.out.println(s.getNom());
			System.out.println(s.getNomParam());
			System.out.println(s.getValeur());
			System.out.println("-------------------");
		}
	}
	
	public List<Parameter> fileToList(){
		
		// Ligne du buffer
		String line; 
		// Variable pour split
		String[] tmp = null;
		
		// Pour savoir quands parser
		 Boolean b = true;
		 // regex
		 String reg = "=";
		  
		 // Liste qu'on return
		 List<Parameter> list = new ArrayList<Parameter>();
		 
		// Lecture du fichier
		try {
			// Buffer qui permet de lire le fichier
			BufferedReader br = new BufferedReader(new FileReader(file));
			// compteur des lignes
			int count = 0;
			// 
			String nom = "ERREUR";
			// Liste temporaire
			 List<String> tmpLvaleur= new ArrayList<String>();
			 List<String> tmpLdata= new ArrayList<String>();
			 
			// Parcours du fichier par ligne
			while ( (line = br.readLine()) != null ) { 
				line = line.replace('"', ' ');
				if ( line.contains("#") == true) {
					nom = line;
				}
				if ( line.contains("#") == false && count > 3 && line.length() > 2) {
					// Split Donnée/Valeur
					
					tmp = line.split(reg);
					// Ajout des Données List Temporaire
					tmpLdata.add(tmp[1]);
					tmpLvaleur.add(tmp[0]);
				}
				if ( line.contains("#") == true && count > 3 && b == true) {
					// Création d'une nouvelle liste 
					List<String> Ldata= new ArrayList<String>();
					List<String> Lvaleur= new ArrayList<String>();
					// Copie de la liste
					Ldata.addAll(tmpLdata);
					Lvaleur.addAll(tmpLvaleur);
					// Création des données à parser
					Parameter d = new Parameter(nom,Ldata,Lvaleur);
					// Ajout des données parser 
					list.add(d);
					// Clear de la liste temporaire 
					tmpLvaleur.clear();
					tmpLdata.clear();
					// On peut recommencer à remplir cette liste temporaire
					b = true;
				}
				// compte les lignes
				count ++;
				
			  }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block Erreur LectureFichier
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block Erreur LectureLigne
			e.printStackTrace();
		}
		
		return list;
	}

}

/*
 		String test = "OPT=\"-DnbAgents=${NBAGENTS} -DnbIterations=${NBITERATIONS} -DnbRuns=${NBRUNS} -DreportPrecRec\"";
    	
    	//Pattern p = Pattern.compile(ENV_VAR_PATTERN); // Capture du contenu entre <b> et </b> (groupe 1)
		Matcher m = ENV_VAR_PATTERN.matcher(test);
		while(m.find())
		{
			System.out.println(m.group());  // Tout le motif
			System.out.println(m.group(1)); // Le contenu entre <b> et </b>
		}
*/
