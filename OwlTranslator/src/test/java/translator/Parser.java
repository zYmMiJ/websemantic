package translator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser{
	
	private File file;
	//
	private static Pattern pattern;
    private static Matcher matcher;
    // Liste qu'on return <varenv,nom>
    private List<DataParsed> listVarEnv = new ArrayList<DataParsed>();
    
    // regex de :${...}
    private final static Pattern ENV_VAR_PATTERN =
    	    Pattern.compile("\\$\\{([A-Za-z0-9_.-]+)(?::([^\\}]*))?\\}");
	
	public Parser(File file) {
		this.file = file; 
	}

	public void printDataParsed(List<DataParsed> list) {

		Iterator<DataParsed> it = list.iterator();
		 int i = 0;
		while (it.hasNext()) {
			DataParsed s = it.next();
			System.out.println(i+".");
			System.out.println(s.getFirstBox()+" ; "+s.getSecondBox());
			System.out.println("-------------------");
			i ++;
		}
	}
	
	public List<DataParsed> fileToList(){
		
		String line;// Ligne du buffer
		String[] tmp = null;// Variable pour split
		String reg = "=";// regex	  
		List<DataParsed> list = new ArrayList<DataParsed>();// Liste qu'on return
		 
		// Lecture du fichier
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));// Buffer qui permet de lire le fichier
			// Parcours du fichier par ligne
			while ( (line = br.readLine()) != null ) {
				line = line.replace('"',' ');	
				if ( line.contains("#") != true  && line.length() > 2) {
					tmp = line.split(reg,2);
					list.add(new DataParsed( tmp[0], tmp[1] ));
				}	
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

	/*
	 * Pas Encore Utilisable mb mais tu peut quands meme recup les donn�es avec List DataParsed
	 */
	/*
	 * Creation des parametres d'environnements
	 */
	public void createEnvParameters() throws IOException{		
		
		BufferedReader br = new BufferedReader(new FileReader(this.file));// buffer pour lecture du fichier
		String line;// Ligne du buffer
		
		while ( (line = br.readLine()) != null ) { 
			
			// Init le matcher des var env
			Matcher m = ENV_VAR_PATTERN.matcher(line);
			Boolean add = true;
			// On cherche si correspondonce
			while( m.find() ){
				Iterator<DataParsed> it = this.listVarEnv.iterator();
				while (it.hasNext()) {
					DataParsed s = it.next();
					if( m.group().equals(s.getFirstBox()) ) {
						add = false;
						break;
					}
				}
				if ( add ) {
					this.listVarEnv.add( new DataParsed( m.group(), m.group(1) )); 
				}
			}
		}
		//this.printDataParsed(this.listVarEnv);
	}
	/*
	 * Set les variables d'envs 
	 */
	public void setAllEnvParameters(List<DataParsed> list) {
		//this.printDataParsed(list);
		Iterator<DataParsed> it = listVarEnv.iterator();
		Iterator<DataParsed> it2 = list.iterator();
		int i = 0;
		System.out.println("Avant---");
		this.printDataParsed(listVarEnv);
		while (it.hasNext()) {
			it.next(); 
			i ++;
		}
		System.out.println("Lenght :"+i);
		
		while (it2.hasNext()) {
			DataParsed l = it2.next();
			for( int j = 0; j<7 ;j ++) {
				if( l.getFirstBox().contains(listVarEnv.get(j).getSecondBox()) ) {
					listVarEnv.get(j).setFirstBox(l.getSecondBox());
				}
			}
		}
		System.out.println("Après---");
		this.printDataParsed(listVarEnv);
		//System.out.println("Fin");
	}
	/*
	 * Remplace les variables d'env
	 */
	public void replaceEnvParameters(List<DataParsed> list) {
		
	}
}

