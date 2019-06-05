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

import org.apache.log4j.Logger;

public class FileParser extends Parser{
	
	private static final Logger LOG = Logger.getLogger(FileParser.class);// Init LOG
	private File file;// File we want to parse

    private static int numberOfEnvVar;// number of {$...}
    private List<DataParsed> listVarEnv = new ArrayList<DataParsed>();// List of  {$...}
     private final static Pattern ENV_VAR_PATTERN =
    	    Pattern.compile("\\$\\{([A-Za-z0-9_.-]+)(?::([^\\}]*))?\\}");// regex de : ${...}
	
     /**
      * Constructor of a Html Parser, Init the link of the document we want to parse
      * @param File we want to parse
      */
	public FileParser(File fileDataProperty_Parameter) { 
		super();
		this.file = fileDataProperty_Parameter; 
	}
	
	public List<DataParsed> dataToList(){
		
		String line;// Ligne du buffer
		String[] tmp = null;// Variable pour split
		String reg = "=";// regex	  
		List<DataParsed> list = new ArrayList<DataParsed>();// Liste qu'on return
		 
		// Lecture du fichier
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));// Buffer qui permet de lire le fichier
			// Parcours du fichier par ligne
			while ( (line = br.readLine()) != null ) {
				line = line.replace("\42","");	
				if ( line.contains("#") != true  && line.length() > 2) {
					tmp = line.split(reg,2);
					//TODO
					if (tmp.length >= 2) {
						list.add(new DataParsed( tmp[0], tmp[1] ));
					}
					
				}	
			  }
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block Erreur LectureFichier
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block Erreur LectureLigne
			e.printStackTrace();
		}
		
		try {
			this.createEnvParameters();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setAllEnvParameters(list);
		this.replaceVarEnv(list);
		
		return list;
	}

	public List<DataParsed> htmlToList(){
		return null;
	}

	
	/**
	 * Transform a the file contained in the {@link FileParser} 
	 * at {@link List} that contains {@link DataParsed} 
	 * @return a {@link List} that contains {@link DataParsed}, 
	 * in the first box of {@link DataParsed} there is a DataProperty,
	 * in the second box there is a Parameter
	 */

	public List<DataParsed> fileAssociationToList(){
		
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
				if (line.length() > 2) {
					tmp = line.split(reg,2);
					list.add(new DataParsed( tmp[0], tmp[1] ));
				}	
			  }
			br.close();
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
	 * Creation des parametres d'environnements
	 */
	public void createEnvParameters() throws IOException{		
		
		BufferedReader br = new BufferedReader(new FileReader(this.file));// buffer pour lecture du fichier
		String line;// Ligne du buffer
		int i = 0;
		System.out.println();
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
					i++;
				}
			}
			
		}
		br.close();
		numberOfEnvVar = i;
		//this.printDataParsed(this.listVarEnv);
	}
	/*
	 * Set les variables d'envs 
	 */
	public void setAllEnvParameters(List<DataParsed> list) {
		//this.printDataParsed(listVarEnv);
        LOG.info("setAllEnvParameters");
        //this.printDataParsed(listVarEnv);
        for(DataParsed data:list) {
        	for( int i = 0;i < numberOfEnvVar; i ++) {
            	if( listVarEnv.get(i).getSecondBox().contains(data.getFirstBox()) ) {
            		setEnvParameters(data.getFirstBox(), data.getSecondBox(),i,list);
            		break;
            	}
            }
    	}
        LOG.info(listVarEnv);
	}
	/*
	 * Remplace les variables d'env par leurs valeurs
	 */
	public void setEnvParameters(String s,String res,int i,List<DataParsed> list) {
		if ( res.contains("{")) {
			Matcher m = ENV_VAR_PATTERN.matcher(res);
			if( m.find() ) {
				for( int j = 0; j< i;j++) {
					if( listVarEnv.get(j).getFirstBox().equals(m.group(0)) ) {
						res = res.replace(m.group(0), listVarEnv.get(j).getSecondBox());
					}
				}
				 
			}
		}
		listVarEnv.get(i).setSecondBox( res );
	}
	/*
	 * Remplace dans nos données les valeurs d'env par leurs valeurs
	 */
	public List<DataParsed> replaceVarEnv(List<DataParsed> list){
		int j = 0;
		 for(DataParsed data:list) {
			 for( int i = 0;i < numberOfEnvVar; i ++) {
	            	if( data.getSecondBox().contains(listVarEnv.get(i).getFirstBox()) ) {
	            		String s = data.getSecondBox();
	            		s = s.replace(listVarEnv.get(i).getFirstBox(), listVarEnv.get(i).getSecondBox());
	            		list.get(j).setSecondBox(s);
	            	}
	            }
			 j ++;
		 }
		LOG.info(list);
		return list;
	}
}

