package SAE;

import java.io.File;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class ImporteurProjet {

    public ImporteurProjet(){
    }

    /**
     * Importe toutes les classes Java d'un dossier donné
     * @param chemin Le chemin du dossier à analyser
     * @return La liste des classes importées
     */
    public List<Classe> importerProjet(String chemin) {
        File dossier = new File(chemin);
        List<Classe> classes = new ArrayList<>();

        if (dossier.isDirectory()) {
            File[] fichiers = dossier.listFiles();

            if (fichiers != null) {
                for (File fichier : fichiers) {
                    if (fichier.isFile() && fichier.getName().endsWith(".java")) { // Vérifie si c'est un fichier Java
                        Classe classeImportee = importerFichier(fichier);
                        if (classeImportee != null) {
                            classes.add(classeImportee);
                        }
                    }
                }
            } else {
                System.out.println("Il n'y a aucun fichier à importer dans votre répertoire.");
            }
        } else {
            System.out.println("Le chemin indiqué n'est pas un répertoire.");
        }

        return classes;
    }

    /**
     * Importe une seule classe Java à partir d'un fichier
     * @param fichier Le fichier de la classe
     * @return Un objet Classe correspondant au fichier
     */
    public Classe importerFichier(File fichier) {
        try {
            // On convertit le chemin du fichier en nom de classe
            String cheminClasse = fichier.getAbsolutePath()
                    .replace(File.separator, ".")
                    .replace(".java", "")
                    .replaceFirst("^.*src\\.", ""); // Suppose que le chemin commence par src/ pour obtenir le bon nom de package

            // Charge la classe à partir de son nom complet
            Class<?> c = Class.forName(cheminClasse);

            // Récupère les modificateurs de la classe (public, protected, etc.)
            int modificateurs = c.getModifiers();
            int acces = 0;
            if (Modifier.isProtected(modificateurs)) acces = 1;
            else if (Modifier.isPrivate(modificateurs)) acces = 2;

            // Crée l'objet Classe avec le nom et le niveau d'accès
            Classe classe = new Classe(c.getName(), acces);

            // Récupère les méthodes de la classe
            Method[] methodes = c.getDeclaredMethods();
            for (Method methode : methodes) {
                Class<?> typeRetour = methode.getReturnType();
                List<Attribut> parametres = new ArrayList<>();

                // Récupération de l'accès de la méthode
                int modificateursMethode = methode.getModifiers();
                int accesMethode = 0; // Par défaut, on suppose "public"
                if (Modifier.isPublic(modificateursMethode)) accesMethode = 0; // Public (reste 0)
                else if (Modifier.isProtected(modificateursMethode)) accesMethode = 1; // Protected
                else if (Modifier.isPrivate(modificateursMethode)) accesMethode = 2; // Private

                // Ajout des paramètres de la méthode
                for (Parameter param : methode.getParameters()) {
                    parametres.add(new Attribut(param.getName(), param.getType()));
                }

                Methode classMet = new Methode(accesMethode, methode.getName(), typeRetour, parametres);
                classe.addMethode(classMet);
            }

            // Récupère les champs (attributs) de la classe
            Field[] attributs = c.getDeclaredFields();
            for (Field attribut : attributs) {
                Class<?> typeAttribut = attribut.getType();
                Attribut classAttr = new Attribut(attribut.getName(), typeAttribut);
                classe.addAttribut(classAttr);
            }

            return classe;

        } catch (ClassNotFoundException e) {
            System.err.println("Classe non trouvée : " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Erreur lors de l'importation du fichier : " + e.getMessage());
            return null;
        }
    }
}