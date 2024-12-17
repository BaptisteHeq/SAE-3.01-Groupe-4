package SAE;

import javafx.application.Application;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import java.io.File;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane bp = new BorderPane();
        MenuBar menuBar = new MenuBar();
        Menu menuFichier = new Menu("Fichier");

        //MenuItem Importer
        MenuItem menuItemImporter = new MenuItem("Importer");
        menuItemImporter.setOnAction(e -> {
            //Ouvre la fenêtre de sélection de dossier (pour l'instant ça marche que en local)
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Choisir un dossier de projet");
            File selectedDirectory = directoryChooser.showDialog(stage);

            if (selectedDirectory != null) {
                //Appel à ImporteurProjet pour importer les fichiers
                ImporteurProjet importeur = new ImporteurProjet();
                List<Classe> classesImportees = importeur.importerProjet(selectedDirectory.getAbsolutePath());

                //Affiche les classes importées dans la console pour voir si ça marche
                if (!classesImportees.isEmpty()) {
                    System.out.println("Classes importées : ");
                    for (Classe classe : classesImportees) {
                        System.out.println(classe.getNom());
                    }
                } else {
                    System.out.println("Aucune classe Java trouvée dans le répertoire sélectionné");
                }
            } else {
                System.out.println("Aucun répertoire sélectionné");
            }
        });

        //Menu Help (à faire dans les dernières itérations)
        Menu menuHelp = new Menu("Help");
        MenuItem menuItemAbout = new MenuItem("Documentation");
        menuItemAbout.setOnAction(e -> {
            System.out.println("Ici on affichera la doc");
        });

        //Ajouter les menus
        menuFichier.getItems().add(menuItemImporter);
        menuHelp.getItems().add(menuItemAbout);

        menuBar.getMenus().add(menuFichier);
        menuBar.getMenus().add(menuHelp);

        bp.setTop(menuBar);

        //Création de la scène
        Scene scene = new Scene(bp, 800, 600);
        stage.setTitle("UML Peneragor");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}