package IOEngine;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IOFiles {

    /*
        input :
            * path : chemin vers le dossier contenant le fichier à regarder
              ATTENTION :
                    . on considère la racine au dossier res
                    . le '/' n'est pas obligatoire à la fin du path
            * name : nom du fichier

        output :
            * un dictionnaire (HashTable) de type <String, String>
                    . keys : titres du fichier écrit entre crochets (ex : [title])
                    . values : String listant les informations entre 2 titres

        Exemple :

        fichier (extrait de) res/game/default :
            """
            [title]
            GameEngine.Game par défaut
            [map]
            0
            """

        input : ("game", "default") or ("game/", "default")
        output : {"title" : "GameEngine.Game par défault", "map" : "0"}

        Informations complémentaires:
            * Si le fichier n'est pas trouvé, le fichier "default" est alors appelé
            * les lignes vides sont ignorées


     */
    public static Hashtable<String, String> getInformation(String path, String name){
        Hashtable<String, String> dico = new Hashtable<>();

        String mainPath = System.getProperty("user.dir");

        String fileName = "default";

        if(path.charAt(path.length()-1) == '/'){
            path = path.substring(0, path.length()-1);
        }

        File f = new File(mainPath + "/res/" + path);
        String[] names = f.list();

        assert names != null;
        for(String n: names){
            if (n.equals(name)) {
                fileName = name;
                break;
            }
        }
        if(!name.equals("default")&&fileName.equals("default")){
            System.out.println("Le fichier '"+name+"' n'a pas été trouvé à l'emplacement : '"+path+"'");
            System.out.println("Charging : " + path + "/" + fileName);
        }

        Path file = FileSystems.getDefault().getPath(mainPath + "/res/"+path+"/"+fileName);
        try (InputStream in = Files.newInputStream(file);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            StringBuilder res = new StringBuilder();
            String key = "";
            while ((line = reader.readLine()) != null) {
                if(!line.startsWith("#")){
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
            }
            if(!key.equals("") && !res.toString().equals("")){
                dico.put(key, res.toString());
            }

        } catch (IOException x) {
            System.err.println(x);
        }
        dico.put("filename", name);
        return dico;
    }

    public static void setInformation(Hashtable<String, String> dico, String path, String name){
        String mainPath = System.getProperty("user.dir");

        if(path.charAt(path.length()-1) == '/'){
            path = path.substring(0, path.length()-1);
        }

        Path file = FileSystems.getDefault().getPath(mainPath + "/res/"+path+"/"+name);
        try (OutputStream out = Files.newOutputStream(file);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))) {

            for(Map.Entry<String, String> entry : dico.entrySet()){
                writer.write("["+entry.getKey()+"]");
                writer.write('\n');
                writer.write(entry.getValue());
                writer.write("\n\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Set<String> listFilesUsingJavaIO(String dir) {
        return Stream.of(new File(dir).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }
}