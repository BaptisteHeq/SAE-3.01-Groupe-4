package SAE;

public class MainTest extends javafx.application.Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(javafx.stage.Stage primaryStage) {
        GestionnaireClasses gestionnaire = new GestionnaireClasses();
        ImporteurProjet importeur = new ImporteurProjet(gestionnaire);
        importeur.importerProjet("src/SAE");
        //ExportateurSourcePlantUML e = new ExportateurSourcePlantUML();
        //System.out.println(e.exporterDiagramme(gestionnaire));
        System.out.println("Classes importées : ");
        for (Classe classe : gestionnaire.getClasses()) {
            System.out.println(classe.getNom());
        }
        System.out.println("Associations : ");
        for (Association association : gestionnaire.getAssociations()) {
            System.out.println(association.toString());
        }
        System.out.println("Heritages : ");
        for (Heritage heritage : gestionnaire.getHeritages()) {
            System.out.println(heritage.getNom());
        }
        System.out.println("Implementations : ");
        for (Implementation implementation : gestionnaire.getImplementations()) {
            System.out.println(implementation.getNom());
        }
        //construire le graphe
        gestionnaire.construireGrapheDependances();
        gestionnaire.trierClassesParDependances();
        System.out.println("Graphe des dépendances : ");
        System.out.println(gestionnaire.getGrapheDependances());
    }
}
