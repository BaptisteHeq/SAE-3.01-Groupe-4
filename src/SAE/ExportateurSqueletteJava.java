package SAE;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.HashSet;
import java.util.Set;

public class ExportateurSqueletteJava {
    /**
     * Méthode genererSource
     * @param nomClasse nom de la classe
     * @param nomFichier nom du fichier de retour (= le fichier généré)
     * @throws ClassNotFoundException
     * Génère un fichier source Java contenant les attributs et méthodes de la classe passée en paramètre
     */
    public static void GenererSource(String nomClasse, String nomFichier) throws ClassNotFoundException{
        //On récupère les infos de la classe
        Class<?> classe = Class.forName(nomClasse);
        String nom = classe.getSimpleName();
        Field[] attributs = classe.getDeclaredFields();
        Method[] methodes = classe.getDeclaredMethods();
        Set<String> imports = new HashSet<>();

        //Création du fichier source
        try{
            FileWriter fw = new FileWriter(nomFichier + ".txt");
            BufferedWriter bw = new BufferedWriter(fw);

            //Ecriture du package
            bw.write("package " + classe.getPackage().getName() + ";\n\n");

            //Ecriture de la classe
            bw.write("public class " + nom);

            //Ecriture des éventuels extends et implements
            if (classe.getSuperclass() != null && !classe.getSuperclass().equals(Object.class)) {
                bw.write(" extends " + classe.getSuperclass().getSimpleName());
            }
            Class<?>[] interfaces = classe.getInterfaces();
            if (interfaces.length > 0) {
                bw.write(" implements ");
                for (int i = 0; i < interfaces.length; i++) {
                    bw.write(interfaces[i].getSimpleName());
                    if (i < interfaces.length - 1) {
                        bw.write(", ");
                    }
                }
            }
            bw.write(" {\n");

            //Ecriture des attributs
            for(Field attribut : attributs){
                bw.write("\t" + Modifier.toString(attribut.getModifiers()) + " " + attribut.getType().getSimpleName() + " " + attribut.getName() + ";\n");
            }

            //Ecriture des méthodes
            for(Method methode : methodes){
                bw.write("\t" + Modifier.toString(methode.getModifiers()) + " " + methode.getReturnType().getSimpleName() + " " + methode.getName() + "(");
                Parameter[] parametres = methode.getParameters();
                for(int i = 0; i < parametres.length; i++){
                    bw.write(parametres[i].getType().getSimpleName() + " " + parametres[i].getName());
                    if(i < parametres.length - 1){
                        bw.write(", ");
                    }
                }
                bw.write("){}\n");
            }
            bw.write("}");
            bw.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}