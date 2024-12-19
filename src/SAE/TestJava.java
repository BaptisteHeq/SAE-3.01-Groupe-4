package SAE;

public class TestJava extends javafx.application.Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(javafx.stage.Stage primaryStage) {

        GestionnaireClasses gestionnaireClasses = new GestionnaireClasses();
        ImporteurProjet importeurProjet = new ImporteurProjet(gestionnaireClasses);
        importeurProjet.importerProjet("src/SAE");
        ExportateurSourceJava exportateurSourceJava = new ExportateurSourceJava();
        System.out.println(exportateurSourceJava.exporterSource(gestionnaireClasses));

        //Génération un fichier .txt
        String cheminFichier = "testSourceJava.txt";
        exportateurSourceJava.genererFichierTxt(cheminFichier, gestionnaireClasses);
        System.out.println("Fichier exporté à : " + cheminFichier);
    }
}