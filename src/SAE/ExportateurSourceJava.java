package SAE;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportateurSourceJava {
    public String exporterSource(GestionnaireClasses gestionnaireClasses) {
        StringBuilder sb = new StringBuilder();
        for (Classe classe : gestionnaireClasses.getClasses()) {
            exporterClasse(classe, sb);
        }
        return sb.toString();
    }

    public HashMap<String, String> exporterSourceList(GestionnaireClasses gestionnaireClasses) {
        HashMap rendu = new HashMap<String, List<String>>();
        for (Classe classe : gestionnaireClasses.getClasses()) {
            StringBuilder sb = new StringBuilder();
            exporterClasse(classe, sb);
            rendu.put(classe.getNom(), sb.toString());
        }
        return rendu;
    }

    private String getJavaAcces(int acces) {
        switch (acces) {
            case 0: return "public";
            case 1: return "protected";
            case 2: return "private";
            default: return "";
        }
    }

    private void exporterClasse(Classe classe, StringBuilder sb) {
        //En-tête de la classe
        sb.append(getJavaAcces(classe.getAcces())).append(" class ").append(classe.getNom()).append(" {\n");

        //Attributs de la classe
        for (Attribut attribut : classe.getAttributs()) {
            sb.append("\t").append(getJavaAcces(attribut.getAcces())).append(" ")
                    .append(attribut.getType().getSimpleName()).append(" ")
                    .append(attribut.getNom()).append(";\n");
        }

        //Méthodes de la classe
        for (Methode methode : classe.getMethodes()) {
            sb.append("\t").append(getJavaAcces(methode.getAcces())).append(" ")
                    .append(methode.getTypeRetour().getSimpleName()).append(" ")
                    .append(methode.getNom()).append("(");

            //Paramètres de la méthode
            for (int i = 0; i < methode.getParametres().size(); i++) {
                Attribut parametre = methode.getParametres().get(i);
                sb.append(parametre.getType().getSimpleName()).append(" ")
                        .append(parametre.getNom());
                if (i < methode.getParametres().size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append(") {}\n");
        }
        sb.append("}\n\n");
    }

    public void genererFichierTxt(String chemin, GestionnaireClasses gestionnaireClasses) {
        String contenu = exporterSource(gestionnaireClasses);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(chemin))) {
            writer.write(contenu);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture du fichier : " + e.getMessage());
        }
    }
}