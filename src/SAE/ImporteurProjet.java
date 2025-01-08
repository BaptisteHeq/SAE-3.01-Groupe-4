package SAE;

import java.io.File;
import java.lang.reflect.*;
import java.net.URL;
import java.net.URLClassLoader;
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
    public void importerProjet(String chemin) throws Exception {
        File dossier = new File(chemin);

        if (!dossier.exists() || !dossier.isDirectory()) {
            throw new IllegalArgumentException("Le chemin spécifié n'existe pas ou n'est pas un dossier !");
        }

        URLClassLoader classLoader = new URLClassLoader(new URL[]{dossier.toURI().toURL()});

        // Parcourir récursivement le dossier pour trouver les fichiers .class
        parcourirEtChargerClasses(dossier, classLoader, "");

        this.gestionnaire.nettoyerDoublons();
    }

    /**
     * Importe une seule classe Java à partir d'un fichier
     * @param c Le fichier de la classe
     */
    public void importerFichier(Class<?> c) {
        try {


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
                int modifAttr = attribut.getModifiers();
                int accesAttr = 0;
                if (Modifier.isProtected(modifAttr)) accesAttr = 1;
                else if (Modifier.isPrivate(modifAttr)) accesAttr = 2;
                Class<?> typeAttribut = attribut.getType();
                Attribut classAttr = new Attribut(attribut.getName(), typeAttribut,accesAttr);
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
        } catch (Exception e) {
            System.err.println("Erreur lors de l'importation du fichier : " + e.getMessage());
        }
    }

    private void parcourirEtChargerClasses(File dossier, URLClassLoader classLoader, String packageCourant) throws Exception {
        for (File fichier : dossier.listFiles()) {
            if (fichier.isDirectory()) {
                // Récursivement traiter les sous-dossiers
                String nouveauPackage = packageCourant.isEmpty() ? fichier.getName() : packageCourant + "." + fichier.getName();
                parcourirEtChargerClasses(fichier, classLoader, nouveauPackage);
            } else if (fichier.getName().endsWith(".class") && !fichier.getName().contains("module-info")) {
                try {
                    // Charger une classe
                    System.out.println("Chargement de la classe : " + fichier.getName());
                    String nomClasse;
                    if (packageCourant.isEmpty()){
                        nomClasse = fichier.getName().replace(".class", "");
                    } else {
                        nomClasse = packageCourant + "." + fichier.getName().replace(".class", "");
                    }
                    Class<?> c = classLoader.loadClass(nomClasse);
                    importerFichier(c);
                    System.out.println("Classe chargée : " + c.getName());
                } catch (NoClassDefFoundError | ClassNotFoundException e) {
                    // Afficher un message d'erreur pour la classe non chargée
                    System.err.println("Impossible de charger la classe : " + fichier.getName() + " (" + e.getMessage() + ")");
                }
            }
        }
    }
}