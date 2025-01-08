package SAE;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.awt.*;
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
            GestionnaireClasses gestionnaire = new GestionnaireClasses();
            modele.remplir(gestionnaire);
            VuePane vuePane = new VuePane(modele);
            modele.enregistrerObservateur(vuePane);
            bp.setCenter(vuePane);
        });

        Menu menuExporter = new Menu("Exporter");

        MenuItem menuItemExportPNG = new MenuItem("PNG");
        menuItemExportPNG.setOnAction(e -> {
            if(modele.getGestionnaireClasses() == null){
                montrerErreur("Aucun projet");
                return;
            }
            System.out.println("PNG sélectionné");
        });

        MenuItem menuItemExportUML = new MenuItem("UML");
        menuItemExportUML.setOnAction(e -> {
            if(modele.getGestionnaireClasses() == null){
                montrerErreur("Aucun projet");
                return;
            }
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
            if(modele.getGestionnaireClasses() == null){
                montrerErreur("Aucun projet");
                return;
            }
            //Retire les doublons
            menuItemAffichageClasses.getItems().clear();

            //Affiche la liste de toutes les classes sous forme de boutons
            for (Classe classe : modele.getGestionnaireClasses().getClasses()) {
                Menu menuClasse = new Menu(classe.getNom());
                //Couleur en fonction de la visibilité
                if (classe.isVisible()) {
                    menuClasse.setStyle("-fx-background-color: #98FB98");
                } else {
                    menuClasse.setStyle("-fx-background-color: #ffcccc");
                }

                //Affiche la liste des méthodes de la classe sous forme de boutons
                for (Methode methode : classe.getMethodes()) {
                    Menu menuMethode = new Menu(methode.getNom());
                    //Couleur en fonction de la visibilité
                    if (methode.isVisible()) {
                        menuMethode.setStyle("-fx-background-color: #98FB98");
                    } else {
                        menuMethode.setStyle("-fx-background-color: #ffcccc");
                    }
                    menuMethode.setOnAction(event -> {
                        if(methode.isVisible()){
                            methode.setInvisible();
                            menuMethode.setStyle("-fx-background-color: #ffcccc");
                        } else {
                            methode.setVisible();
                            menuMethode.setStyle("-fx-background-color: #98FB98");
                        }
                        modele.notifierObservateurs();
                    });
                    menuClasse.getItems().add(menuMethode);
                }

                //Affiche ou masque la classe en fonction de son état
                menuClasse.setOnAction(event -> {
                    if (classe.isVisible()) {
                        classe.setInvisible();
                        menuClasse.setStyle("-fx-background-color: #ffcccc");
                    } else {
                        classe.setVisible();
                        menuClasse.setStyle("-fx-background-color: #98FB98");
                    }
                    modele.notifierObservateurs();
                });
                menuItemAffichageClasses.getItems().add(menuClasse);
            }
        });

        //faire un bouton pour ajouter des classes
        Menu menuAjouter = new Menu("Ajouter");
        MenuItem menuItemAjouterClasse = new MenuItem("Classe");
        Menu menuItemAjouterMethode = new Menu("Méthode");
        menuItemAjouterClasse.setOnAction(e -> {
            if(modele.getGestionnaireClasses() == null){
                montrerErreur("Aucun projet");
                return;
            }
            // Créer une boîte de dialogue pour demander le nom de la classe et sa visibilité
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Ajouter une Classe");
            dialog.setHeaderText("Ajouter une nouvelle classe");

            // Définir les boutons OK et Annuler
            ButtonType boutonOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(boutonOk, ButtonType.CANCEL);

            // Créer les champs de saisie pour le nom et la visibilité
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            //grid.setPadding(new Insets(20, 150, 10, 10));

            TextField nomClasse = new TextField();
            nomClasse.setPromptText("Nom de la classe");

            ComboBox<String> comboVisibilite = new ComboBox<>();
            comboVisibilite.getItems().addAll("Public", "Privé");
            comboVisibilite.setValue("Public");

            grid.add(new Label("Nom de la classe:"), 0, 0);
            grid.add(nomClasse, 1, 0);
            grid.add(new Label("Visibilité:"), 0, 1);
            grid.add(comboVisibilite, 1, 1);

            dialog.getDialogPane().setContent(grid);

            // Récupérer le résultat du dialogue
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == boutonOk) {
                    return new Pair<>(nomClasse.getText(), comboVisibilite.getValue());
                }
                return null;
            });

            // Traiter le résultat du dialogue
            dialog.showAndWait().ifPresent(result -> {
                String nom = result.getKey();
                String visibilite = result.getValue();
                int visibiliteCode = visibilite.equals("Public") ? 1 : 0;
                Classe nouvelleClasse = new Classe(nom, visibiliteCode);
                modele.getGestionnaireClasses().ajouterClasse(nouvelleClasse);
                modele.notifierObservateurs();
            });
        });
        menuItemAjouterMethode.setOnAction(e -> {
            if(modele.getGestionnaireClasses() == null){
                montrerErreur("Aucun projet");
                return;
            }
            //Retire les doublons
            menuItemAjouterMethode.getItems().clear();
            //Affiche la liste de toutes les classes sous forme de boutons
            for (Classe classe : modele.getGestionnaireClasses().getClasses()) {
                MenuItem c = new MenuItem(classe.getNom());
                c.setOnAction(event -> {
                    // Créer une boîte de dialogue pour demander le nom de la methode
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Ajouter une Methode");
                    dialog.setHeaderText("Ajouter une nouvelle methode");
                    dialog.setContentText("Entrez le nom de la methode:");
                    dialog.showAndWait().ifPresent(nomMethode -> {
                        Methode nouvelleMethode = new Methode(1, nomMethode, new Classe("void", 0).getClass(), null);
                        classe.addMethode(nouvelleMethode);
                        modele.notifierObservateurs();
                    });
                });
                menuItemAjouterMethode.getItems().add(c);
            }
        });
        //Ajouter les menus
        menuFichier.getItems().add(menuItemImporter);
        menuFichier.getItems().add(menuItemVide);
        menuFichier.getItems().add(menuExporter);
        menuExporter.getItems().add(menuItemExportPNG);
        menuExporter.getItems().add(menuItemExportUML);
        menuAffichage.getItems().add(menuItemAffichageClasses);
        menuAjouter.getItems().addAll(menuItemAjouterClasse, menuItemAjouterMethode);
        menuHelp.getItems().add(menuItemAbout);
        menuBar.getMenus().add(menuFichier);
        menuBar.getMenus().add(menuHelp);
        menuBar.getMenus().add(menuAffichage);
        menuBar.getMenus().add(menuAjouter);
        bp.setTop(menuBar);
        Scene scene = new Scene(bp, 800, 600);
        stage.setTitle("UML Peneragor");
        stage.setScene(scene);
        stage.show();
    }

    public void montrerErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }}


