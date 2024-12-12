package SAE;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class ImporteurProjet {
    public String repertoire;

    public ImporteurProjet(String repertoire){
        this.repertoire = repertoire;
    }

    public List<Classe> importerProjet(String chemin){
        File dossier = new File(chemin);
        List<Classe> classes = new ArrayList<>();
        if(dossier.isDirectory()){
            File[] fichiers = dossier.listFiles();
            if(fichiers!=null){
                for(File fichier : fichiers){
                    if(fichier.isFile()){
                        classes.add(importerFichier(fichier.getAbsolutePath()));
                    }
                }
            }
            else{
                System.out.println("Il n'y a aucun fichier java à importer dans votre répertoire");
            }
        }
        return classes;
    }

    public Classe importerFichier(String chemin){
        try{
            Class<?> c = Class.forName(chemin);
            Method[] methodes = c.getDeclaredMethods();
            int m = c.getModifiers();
            int acces = 0;
            if(Modifier.isProtected(m))
                acces = 1;
            if(Modifier.isPrivate(m))
                acces = 2;

            Classe classe = new Classe(c.getName(),acces);
            for(Method methode : methodes){
                Class<?> p = methode.getReturnType();
                List<Attribut> parametres = new ArrayList<>();
                for(Parameter param : methode.getParameters()){
                    parametres.add(new Attribut(param.getName(),param.getType()));
                }
                Methode classMet = new Methode(methode.getModifiers(),methode.getName(),p,parametres);
                classe.addMethode(classMet);
            }
            Field[] attributs = c.getDeclaredFields();
            for(Field attribut : attributs){
                Class<?> p2 = attribut.getType();
                Attribut classAttr = new Attribut(attribut.getName(),p2);
                classe.addAttribut(classAttr);
            }
            return classe;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
