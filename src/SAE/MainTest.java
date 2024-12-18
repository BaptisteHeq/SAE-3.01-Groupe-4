package SAE;

import java.util.List;



public class MainTest extends javafx.application.Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(javafx.stage.Stage primaryStage) {
        GestionnaireClasses gestionnaire = new GestionnaireClasses();
        ImporteurProjet importeur = new ImporteurProjet(gestionnaire);
        importeur.importerProjet("src/SAE");
        ExportateurSourcePlantUML e = new ExportateurSourcePlantUML();
        System.out.println(e.exporterDiagramme(gestionnaire));
    }
}
