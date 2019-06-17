package translator;

import java.io.*;

public class CleanFile {

    private File directory;

    public CleanFile(String directoryName){
        directory = new File(directoryName);
        System.out.println(	"directoryName ? "+directory.isDirectory());
    }
    
    public CleanFile(){
    }

    public void cleanAll() throws IOException {
        File[] files = directory.listFiles();
        for(int i = 0; i < files.length ; i++){
            clean(files[i], "DataTurtleOutput");
        }
    }

    public void clean(File file, String pathOut){

        File sortie = new File("tmp.ttl"); 	
        try {
            String line;// Buffer Line

            BufferedReader br = new BufferedReader(new FileReader(file));// Buffer take the file
            BufferedWriter bw = new BufferedWriter(new FileWriter(sortie));
            
            bw.write("@prefix xpd: <#> ."+"\n");
            bw.flush();
          

            Boolean Individuals = false;// To delete ontology in  datas.ttl
            @SuppressWarnings("unused")
			String precedentline = "";
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
                precedentline = line;
            }
            bw.close();
            br.close();

            String name = "/"+file.getName();
            sortie.renameTo( new File(pathOut+name) );


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block Erreur LectureFichier
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block Erreur LectureLigne
            e.printStackTrace();
        }
    }




}
