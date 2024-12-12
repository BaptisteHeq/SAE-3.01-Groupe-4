package SAE;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        BorderPane bp = new BorderPane();
        MenuBar menuBar = new MenuBar();
        Menu menuFichier = new Menu("Fichier");

        MenuItem menuItemImporter = new MenuItem("Importer");
        menuItemImporter.setOnAction(e -> {
            System.out.println("Importer sélectionné");
        });

        Menu menuHelp = new Menu("Help");
        MenuItem menuItemAbout = new MenuItem("Documentation");
        menuItemAbout.setOnAction(e -> {
            System.out.println("Documentation sélectionné");
        });

        menuFichier.getItems().add(menuItemImporter);
        menuHelp.getItems().add(menuItemAbout);

        menuBar.getMenus().add(menuFichier);
        menuBar.getMenus().add(menuHelp);

        bp.setTop(menuBar);

        Scene scene = new Scene(bp, 800, 600);
        stage.setTitle("UML Peneragor");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
