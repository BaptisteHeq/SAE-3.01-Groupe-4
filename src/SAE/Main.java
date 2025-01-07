package SAE;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane bp = new BorderPane();
        MenuBar menuBar = new MenuBar();
        Menu menuFichier = new Menu("Fichier");
        Modele modele = new Modele();

        //MenuItem Importer
        MenuItem menuItemImporter = new MenuItem("Importer");
        menuItemImporter.setOnAction(e -> {
            //Ouvre la fenêtre de sélection de dossier (pour l'instant ça marche que en local)
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Choisir un dossier de projet");
            File selectedDirectory = directoryChooser.showDialog(stage);

            if (selectedDirectory != null) {
                //Appel à ImporteurProjet pour importer les fichiers
                GestionnaireClasses gestionnaire = new GestionnaireClasses();
                ImporteurProjet importeur = new ImporteurProjet(gestionnaire);
                importeur.importerProjet(selectedDirectory.getAbsolutePath());

                modele.remplir(gestionnaire);
                VuePane vuePane = new VuePane(modele);
                modele.enregistrerObservateur(vuePane);
                bp.setCenter(vuePane);

                //Affiche les classes importées dans la console pour voir si ça marche
                if (!gestionnaire.getClasses().isEmpty()) {
                    System.out.println("Classes importées : ");
                    for (Classe classe : gestionnaire.getClasses()) {
                        System.out.println(classe.getNom());
                    }
                } else {
                    System.out.println("Aucune classe Java trouvée dans le répertoire sélectionné");
                }
            } else {
                System.out.println("Aucun répertoire sélectionné");
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
        });

        //Menu Help (à faire dans les dernières itérations)
        Menu menuHelp = new Menu("Help");
        MenuItem menuItemAbout = new MenuItem("Documentation");
        menuItemAbout.setOnAction(e -> {
            System.out.println("Ici on affichera la doc");
        });

        //Menu Affichage
        Menu menuAffichage = new Menu("Affichage");
        Menu menuItemAffichageClasses = new Menu("Afficher/Masquer les classes");
        menuItemAffichageClasses.setOnAction(e -> {
            //Retire les doublons
            menuItemAffichageClasses.getItems().clear();
            //Affiche la liste de toutes les classes sours forme de boutons
            for (Classe classe : modele.getGestionnaireClasses().getClasses()) {
                Menu menuClasse = new Menu(classe.getNom());
                for (Methode methode : classe.getMethodes()) {
                    Menu menuMethode = new Menu(methode.getNom());
                    menuMethode.setOnAction(event -> {
                        if(methode.isVisible()){
                            methode.setInvisible();
                            modele.notifierObservateurs();
                        } else {
                            methode.setVisible();
                            modele.notifierObservateurs();
                        }
                    });
                    menuClasse.getItems().add(menuMethode);
                }

                //Affiche ou masque la classe en fonction de son état
                menuClasse.setOnAction(event -> {
                    if (classe.isVisible()) {
                        classe.setInvisible();
                    } else {
                        classe.setVisible();
                    }
                    modele.notifierObservateurs();
                });
                menuItemAffichageClasses.getItems().add(menuClasse);
            }
        });

        //Ajouter les menus
        menuFichier.getItems().add(menuItemImporter);
        menuFichier.getItems().add(menuItemVide);
        menuFichier.getItems().add(menuExporter);
        menuExporter.getItems().add(menuItemExportPNG);
        menuExporter.getItems().add(menuItemExportUML);
        menuAffichage.getItems().add(menuItemAffichageClasses);

        menuHelp.getItems().add(menuItemAbout);

        menuBar.getMenus().add(menuFichier);
        menuBar.getMenus().add(menuHelp);
        menuBar.getMenus().add(menuAffichage);

        bp.setTop(menuBar);

        Scene scene = new Scene(bp, 800, 600);
        stage.setTitle("UML Peneragor");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }}
