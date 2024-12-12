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
                int typeRetour = 0;
                if(p.equals(int.class))
                    typeRetour = 1;
                if(p.equals(String.class))
                    typeRetour = 2;
                if (p.equals(boolean.class))
                    typeRetour = 3;

                List<Attribut> parametres = new ArrayList<>();
                for(Parameter param : methode.getParameters()){
                    int typeParam=0;
                    if(param.getType().equals(int.class))
                        typeParam = 1;
                    if(param.getType().equals(String.class))
                        typeParam = 2;
                    if(param.getType().equals(boolean.class))
                        typeParam = 3;
                    parametres.add(new Attribut(param.getName(),typeParam));
                }
                Methode classMet = new Methode(methode.getModifiers(),methode.getName(),typeRetour,parametres);
                classe.addMethode(classMet);
            }
            Field[] attributs = c.getDeclaredFields();
            for(Field attribut : attributs){
                Class<?> p = attribut.getType();
                int typeAttribut = 0;
                if(p.equals(int.class))
                    typeAttribut = 1;
                if(p.equals(String.class))
                    typeAttribut = 2;
                if(p.equals(boolean.class))
                    typeAttribut = 3;
                Attribut classAttr = new Attribut(attribut.getName(),typeAttribut);
                classe.addAttribut(classAttr);
            }
            return classe;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
