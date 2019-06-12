package translator;

import java.io.*;

public class CleanFile {

    private File directory;

    public CleanFile(String directoryName){
        directory = new File(directoryName);
        System.out.println(	"directoryName ? "+directory.isDirectory());

    }

    public void cleanAll() throws IOException {

        File[] files = directory.listFiles();
        for(int i = 0; i < files.length ; i++){
            String dataFileName = directory.getCanonicalPath();
            clean(files[i]);
            //System.out.println(files[i]);
        }

    }

    public void clean(File file){

        File sortie = new File("tmp.ttl");
        System.out.println(file);
        try {
            String line;// Buffer Line

            BufferedReader br = new BufferedReader(new FileReader(file));// Buffer take the file
            BufferedWriter bw = new BufferedWriter(new FileWriter(sortie));


            Boolean Individuals = false;// To delete ontology in  datas.ttl
            String precedentline = "";
            while ( (line = br.readLine()) != null ) { // read line by line


                line = line.replace("<xpd","xpd");
                line = line.replace("NOOR>","NOOR");



                if ( line.contains("Individuals")  ) {

                    line = "\n# #################################################################" + "\n" +  "# #"+ "\n" + line;
                    //System.out.println(line);
                    Individuals = true;
                }

                if( Individuals || line.contains("prefix") ){
                    //System.out.println(line);
                    bw.write(line+"\n");
                    //System.out.println(line);
                    bw.flush();
                }
                precedentline = line;
            }
            bw.close();
            br.close();

            String name = file.getName();
            System.out.println(file.getName());
            sortie.renameTo( new File(name) );


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block Erreur LectureFichier
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block Erreur LectureLigne
            e.printStackTrace();
        }
    }




}
