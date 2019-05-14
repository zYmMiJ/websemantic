package translator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParserBash extends Parser{
	
	public ParserBash(File file) {
		super(file);
		// TODO Auto-generated constructor stub
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
