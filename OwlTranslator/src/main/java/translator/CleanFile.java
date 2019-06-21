package translator;

import java.io.*;
/**
 *Cleans files that are parsing of unwanted characters.
 * @author javae
*/
public class CleanFile {

    private File directory;

    public CleanFile(String directoryName){
        this.directory = new File(directoryName);
    }
    
    public CleanFile(){
    }
    /**
	 * Cleans files that are parsing of unwanted characters from a folders
	 */
    public void cleanAll() throws IOException {
        File[] files = directory.listFiles();
        for(int i = 0; i < files.length ; i++){
            clean(files[i], "DataTurtleOutput");
        }
    }
    
    /**
	 * Cleans files that are parsing of unwanted characters
	 * @param a {@link File} that contains the data to clean 
	 *  @param a {@link String} that contains the path of file
	 */
    public void clean(File file, String pathOut){

        File sortie = new File(pathOut+"/tmp.ttl"); 	
        try {
            String line;// Buffer Line

            BufferedReader br = new BufferedReader(new FileReader(file));// Buffer take the file
            BufferedWriter bw = new BufferedWriter(new FileWriter(sortie));
            
            bw.write("@prefix xpd: <#> ."+"\n");
            bw.flush();
          

            Boolean Individuals = false;// To delete ontology in  datas.ttl
            while ( (line = br.readLine()) != null ) { // read line by line


                line = line.replace("<xpd","xpd");
                line = line.replace("NOOR>","NOOR");
                
                if ( line.contains("Person") ) {
                	line = line.replace(">","");
                	line = line.replace("(","");
                	line = line.replace(")","");
                }



                if ( line.contains("Individuals")  ) {
                    line = "\n# #################################################################" + "\n" +  "# #"+ "\n" + line;
                    Individuals = true;
                }

                if( Individuals || line.contains("prefix") ){
                    bw.write(line+"\n");
                    bw.flush();
                }
            }
            bw.close();
            br.close();

            String name = file.getName();     
            file.delete();
            sortie.renameTo(  new File(pathOut+"/"+name) );


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block Erreur LectureFichier
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block Erreur LectureLigne
            e.printStackTrace();
        }
    }




}
