package SAE;

import java.util.List;

public class ExportateurSourcePlantUML {

    /**
     * Exporte toutes les classes, les relations d'héritage, d'association et d'implémentation en PlantUML
     * @param gestionnaire Le gestionnaire de classes contenant les classes et les relations
     * @return Le code source PlantUML
     */
    public String exporterDiagramme(GestionnaireClasses gestionnaire) {
        StringBuilder sb = new StringBuilder();
        sb.append("@startuml\n\n");

        // Exporter les classes
        for (Classe classe : gestionnaire.getClasses()) {
            exporterClasse(classe, sb);
        }

        // Exporter les relations d'héritage
        for (Heritage heritage : gestionnaire.getHeritages()) {
            sb.append(heritage.getClasseMere().getNom())
                    .append(" <|-- ")
                    .append(heritage.getClasseFille().getNom())
                    .append("\n");
        }

        // Exporter les relations d'implémentation
        for (Implementation implementation : gestionnaire.getImplementations()) {
            sb.append(implementation.getClasseInterface().getNom())
                    .append(" <|.. ")
                    .append(implementation.getClasseImplementation().getNom())
                    .append("\n");
        }

        // Exporter les relations d'association
        for (Association association : gestionnaire.getAssociations()) {
            sb.append(association.getClasse1().getNom())
                    .append(" --> ")
                    .append(association.getClasse2().getNom())
                    .append(" : ")
                    .append(association.getNom())
                    .append(" [")
                    .append(association.getCardinalite())
                    .append("]\n");
        }

        sb.append("\n@enduml");
        return sb.toString();
    }

    /**
     * Exporte la déclaration d'une classe au format PlantUML
     * @param classe La classe à exporter
     * @param sb Le StringBuilder dans lequel ajouter le texte
     */
    private void exporterClasse(Classe classe, StringBuilder sb) {
        sb.append("class ").append(classe.getNom()).append(" {\n");

        // Exporter les attributs de la classe
        for (Attribut attribut : classe.getAttributs()) {
            String visibilite = getSymboleAcces(attribut.getAcces());
            sb.append("  ").append(visibilite).append(" ")
                    .append(attribut.getNom()).append(" : ")
                    .append(attribut.getType().getSimpleName()).append("\n");
        }

        // Exporter les méthodes de la classe
        for (Methode methode : classe.getMethodes()) {
            String visibilite = getSymboleAcces(methode.getAcces());
            sb.append("  ").append(visibilite).append(" ")
                    .append(methode.getNom()).append("(");

            // Exporter les paramètres de la méthode
            for (int i = 0; i < methode.getParametres().size(); i++) {
                Attribut parametre = methode.getParametres().get(i);
                sb.append(parametre.getType().getSimpleName());
                if (i < methode.getParametres().size() - 1) {
                    sb.append(", ");
                }
            }

            // Exporter le type de retour de la méthode
            String typeRetour = methode.getTypeRetour() != null
                    ? methode.getTypeRetour().getSimpleName()
                    : "void";

            sb.append(") : ").append(typeRetour).append("\n");
        }

        sb.append("}\n\n");
    }

    /**
     * Retourne le symbole de visibilité (+, #, -) en fonction du niveau d'accès
     * @param acces Le niveau d'accès (0 = public, 1 = protected, 2 = private)
     * @return Le symbole de visibilité en PlantUML
     */
    private String getSymboleAcces(int acces) {
        switch (acces) {
            case 0: return "+";
            case 1: return "#";
            case 2: return "-";
            default: return "~"; // Par défaut, package-private
        }
    }
}