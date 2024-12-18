package SAE;

import java.io.IOException;


public class MainTestImage extends javafx.application.Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(javafx.stage.Stage primaryStage) {
        GestionnaireClasses gestionnaire = new GestionnaireClasses();
        ImporteurProjet importeur = new ImporteurProjet(gestionnaire);
        importeur.importerProjet("src/SAE");

        ExportateurImagePlantUML exportateurImage = new ExportateurImagePlantUML();

        exportateurImage.exporterImage(gestionnaire, "fichier.png");
    }
}


