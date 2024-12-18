package SAE;

import javafx.application.Application;
<<<<<<< Updated upstream
import javafx.stage.DirectoryChooser;
=======
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
>>>>>>> Stashed changes
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import java.io.File;
import java.util.List;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane bp = new BorderPane();
        MenuBar menuBar = new MenuBar();
        Menu menuFichier = new Menu("Fichier");

        //MenuItem Importer
        MenuItem menuItemImporter = new MenuItem("Importer");
        menuItemImporter.setOnAction(e -> {
<<<<<<< Updated upstream
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
=======
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sélectionner un fichier Java");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers Java", "*.java"));
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                System.out.println("Fichier sélectionné : " + selectedFile.getAbsolutePath());
            } else {
                System.out.println("Aucun fichier sélectionné");
            }
        });

        MenuItem menuItemVide = new MenuItem("Nouveau");
        menuItemVide.setOnAction(e -> {
            System.out.println("Nouveau sélectionné");
        });

        Menu menuExporter = new Menu("Exporter");

        MenuItem menuItemExportPNG = new MenuItem("PNG");
        menuItemExportPNG.setOnAction(e -> {
            System.out.println("PNG sélectionné");
        });

        MenuItem menuItemExportUML = new MenuItem("UML");
        menuItemExportUML.setOnAction(e -> {
            System.out.println("UML sélectionné");
>>>>>>> Stashed changes
        });

        //Menu Help (à faire dans les dernières itérations)
        Menu menuHelp = new Menu("Help");
        MenuItem menuItemAbout = new MenuItem("Documentation");
        menuItemAbout.setOnAction(e -> {
            System.out.println("Ici on affichera la doc");
        });

        //Ajouter les menus
        menuFichier.getItems().add(menuItemImporter);
        menuFichier.getItems().add(menuItemVide);
        menuFichier.getItems().add(menuExporter);
        menuExporter.getItems().add(menuItemExportPNG);
        menuExporter.getItems().add(menuItemExportUML);

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