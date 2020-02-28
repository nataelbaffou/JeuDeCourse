import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Hashtable;

public class IOFiles {
    public static Hashtable<String, String> getInformation(String path, String name){
        Hashtable<String, String> dico = new Hashtable<>();

        String mainPath = System.getProperty("user.dir");

        String fileName = "default";

        File f = new File(mainPath + "/res/" + path);
        String[] names = f.list();
        for(String n: names){
            if (n.equals(name)) {
                fileName = name;
                break;
            }
        }

        Path file = FileSystems.getDefault().getPath(mainPath + "/res/"+path+"/"+fileName);
        try (InputStream in = Files.newInputStream(file);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            StringBuilder res = new StringBuilder();
            String key = "";
            while ((line = reader.readLine()) != null) {
                if(line.matches("\\[(.*)\\]")){
                    if(!key.equals("") && !res.toString().equals("")){
                        dico.put(key, res.toString());
                        res = new StringBuilder();
                    }
                    key = line.substring(1, line.length()-1);
                } else if(!line.equals("")){
                    if(!res.toString().equals("")){
                        res.append("\n");
                    }
                    res.append(line);
                }
            }
            if(!key.equals("") && !res.toString().equals("")){
                dico.put(key, res.toString());
            }

        } catch (IOException x) {
            System.err.println(x);
        }
        return dico;
    }
}
