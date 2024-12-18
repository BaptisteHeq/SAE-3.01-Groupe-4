package SAE;

import java.io.File;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

public class ImporteurProjet {

    private GestionnaireClasses gestionnaire;

    public ImporteurProjet(GestionnaireClasses gestionnaire) {
        this.gestionnaire = gestionnaire;
    }

    /**
     * Importe toutes les classes Java d'un dossier donné
     * @param chemin Le chemin du dossier à analyser
     */
    public void importerProjet(String chemin) {
        File dossier = new File(chemin);

        if (dossier.isDirectory()) {
            File[] fichiers = dossier.listFiles();

            if (fichiers != null) {
                for (File fichier : fichiers) {
                    if (fichier.isFile() && fichier.getName().endsWith(".java")) { // Vérifie si c'est un fichier Java
                        importerFichier(fichier);
                    }
                }
            } else {
                System.out.println("Il n'y a aucun fichier à importer dans votre répertoire.");
            }
        } else {
            System.out.println("Le chemin indiqué n'est pas un répertoire.");
        }

        this.gestionnaire.nettoyerDoublons();
    }

    /**
     * Importe une seule classe Java à partir d'un fichier
     * @param fichier Le fichier de la classe
     */
    public void importerFichier(File fichier) {
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

            // Crée l'objet Classe
            Classe classe = new Classe(c.getName(), acces);
            gestionnaire.ajouterClasse(classe);
            // Gestion de l'héritage
            Class<?> superClasse = c.getSuperclass();
            if (superClasse != null && !superClasse.getName().equals("java.lang.Object")) {
                Classe classeMere = new Classe(superClasse.getName(), 0); // On suppose que la classe mère est publique
                gestionnaire.ajouterClasse(classeMere);
                Heritage heritage = new Heritage(classeMere, classe);
                gestionnaire.ajouterHeritage(heritage);
            }

            // Gestion des interfaces implémentées
            Class<?>[] interfaces = c.getInterfaces();
            for (Class<?> inter : interfaces) {
                Classe classeInterface = new Classe(inter.getName(), 0); // On suppose que l'interface est publique
                gestionnaire.ajouterClasse(classeInterface);
                Implementation implementation = new Implementation(classeInterface, classe);
                gestionnaire.ajouterImplementation(implementation);
            }

            // Récupère les méthodes de la classe
            Method[] methodes = c.getDeclaredMethods();
            for (Method methode : methodes) {
                Class<?> typeRetour = methode.getReturnType();
                List<Attribut> parametres = new ArrayList<>();

                int modificateursMethode = methode.getModifiers();
                int accesMethode = 0;
                if (Modifier.isProtected(modificateursMethode)) accesMethode = 1;
                else if (Modifier.isPrivate(modificateursMethode)) accesMethode = 2;

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

                // Gestion des associations
                if (!typeAttribut.isPrimitive() && !typeAttribut.getName().startsWith("java.")) {
                    Classe classeAssociee = new Classe(typeAttribut.getName(), 0);
                    gestionnaire.ajouterClasse(classeAssociee);
                    Association association = new Association(classe, classeAssociee, attribut.getName(), "1..1");
                    gestionnaire.ajouterAssociation(association);
                }
                //On vérifie si l'attribut est une collection
                if (Collection.class.isAssignableFrom(typeAttribut)){
                    // Obtenir le type générique
                    Type genericType = attribut.getGenericType();
                    if (genericType instanceof ParameterizedType){
                        ParameterizedType parameterizedType = (ParameterizedType) genericType;
                        Type[] typeArguments = parameterizedType.getActualTypeArguments();
                        if (typeArguments.length > 0 && typeArguments[0] instanceof Class<?>){
                            Class<?> typeElement = (Class<?>) typeArguments[0];
                            Classe classeAssociee = new Classe(typeElement.getName(), 0);
                            gestionnaire.ajouterClasse(classeAssociee);
                            gestionnaire.ajouterAssociation(new Association(classe, classeAssociee, attribut.getName(), "0..*"));
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Classe non trouvée : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur lors de l'importation du fichier : " + e.getMessage());
        }
    }
}