package SAE;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.util.Pair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

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
                try {
                    importeur.importerProjet(selectedDirectory.getAbsolutePath());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

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
            ExportateurImagePlantUML exportateur = new ExportateurImagePlantUML();
            //recup la date pour dater le fichier et ne pas avoir de conflit à la création car chaque fichier est unique
            String date = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
            String cheminComplet = "outExportateur/ImageUML" + "_" + date + ".png";

            exportateur.exporterImage(modele.getGestionnaireClasses(), cheminComplet);
        });

        MenuItem menuItemExportUML = new MenuItem("UML");
        menuItemExportUML.setOnAction(e -> {
            if(modele.getGestionnaireClasses() == null){
                montrerErreur("Aucun projet");
                return;
            }
            System.out.println("UML sélectionné");
            ExportateurSourcePlantUML exportateur = new ExportateurSourcePlantUML();
            //recup la date pour dater le fichier et ne pas avoir de conflit à la création car chaque fichier est unique
            String date = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
            String cheminComplet = "outExportateur/SourceUML" + "_" + date + ".txt";

            String codeSource = exportateur.exporterDiagramme(modele.getGestionnaireClasses());
            //on write le code source dans le fichier
            try {
                BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(cheminComplet));
                writer.write(codeSource);
                writer.close();
                System.out.println("ecriture reussie");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        MenuItem menuItemExportJava = new MenuItem("Java");
        menuItemExportJava.setOnAction(e -> {
            if(modele.getGestionnaireClasses() == null){
                montrerErreur("Aucun projet");
                return;
            }
            System.out.println("Java sélectionné");
            ExportateurSourceJava exportateur = new ExportateurSourceJava();
            //recup la date pour dater le fichier et ne pas avoir de conflit à la création car chaque fichier est unique
            String date = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
            String cheminComplet = "outExportateur/SourceJava" + "_" + date + "/";

            // Création du répertoir
            java.io.File directory = new java.io.File(cheminComplet);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            HashMap rendu = exportateur.exporterSourceList(modele.getGestionnaireClasses());
            //on parcours les clés et les valeurs pour écrire les fichiers
            for(Object nomObj : rendu.keySet()){
                try {
                    String nom = (String) nomObj;
                    BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(cheminComplet + nom + ".java"));
                    writer.write((String) rendu.get(nom));
                    writer.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
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
        MenuItem menuHeritage = new MenuItem("Ajouter un Héritage");
        MenuItem menuImplementation = new MenuItem("Ajouter une Implémentation");

        menuHeritage.setOnAction(event -> {
            if(modele.getGestionnaireClasses() == null){
                montrerErreur("Aucun projet");
                return;
            }
            Stage dialog = new Stage();
            dialog.setTitle("Ajouter un Héritage");

            VBox layout = new VBox(10);
            layout.setPadding(new Insets(20));

            Label labelInstructions = new Label("Sélectionnez une classe mère et une classe fille :");

            //ComboBox pour le choix de la classe mere
            ComboBox<String> comboClasseMere = new ComboBox<>();
            for (Classe classe : modele.getGestionnaireClasses().getClasses()) {
                comboClasseMere.getItems().add(classe.getNom());
                comboClasseMere.setPromptText("Classe Mère");
            }

            // ComboBox pour le choix de la classe fille
            ComboBox<String> comboClasseFille = new ComboBox<>();
            for (Classe classe : modele.getGestionnaireClasses().getClasses()) {
                comboClasseFille.getItems().add(classe.getNom());
                }
            comboClasseFille.setPromptText("Classe Fille");

            //Bouton pour valider
            Button btnValider = new Button("valider");
            btnValider.setOnAction(e -> {
                if(comboClasseFille.getValue()!=null && comboClasseMere.getValue()!=null){
                        Classe classeMere = modele.gestionnaireClasses.getClasseByNom(comboClasseMere.getValue());
                        Classe classeFille = modele.gestionnaireClasses.getClasseByNom(comboClasseFille.getValue());
                        if(classeMere!=classeFille){
                            Heritage heritage = new Heritage(classeMere,classeFille);
                            modele.gestionnaireClasses.ajouterHeritage(heritage);
                            modele.notifierObservateurs();
                        }
                        else{
                            montrerErreur("Une classe ne peut pas hériter d'elle-même");
                        }
                }
                else{
                    montrerErreur("Une des deux classes n'a pas été spécifiée");
                }

                dialog.close();
            });

            //On ajoute les boutons au layout et on affiche la scene
            layout.getChildren().addAll(labelInstructions, comboClasseMere, comboClasseFille, btnValider);
            Scene dialogScene = new Scene(layout, 300, 200);
            dialog.setScene(dialogScene);
            dialog.showAndWait();
        });

        menuImplementation.setOnAction(event -> {
            if(modele.getGestionnaireClasses() == null){
                montrerErreur("Aucun projet");
                return;
            }
            Stage dialog = new Stage();
            dialog.setTitle("Ajouter une Implémentation");

            VBox layout = new VBox(10);
            layout.setPadding(new Insets(20));

            Label labelInstructions = new Label("Sélectionnez une classe interface et une classe implementation :");

            //ComboBox pour le choix de la classe mere
            ComboBox<String> comboClasseInterface = new ComboBox<>();
            for (Classe classe : modele.getGestionnaireClasses().getClasses()) {
                if(classe.getIsInterface()){
                    comboClasseInterface.getItems().add(classe.getNom());
                }
            }
            comboClasseInterface.setPromptText("Classe Interface");

            // ComboBox pour le choix de la classe fille
            ComboBox<String> comboClasseImplementation = new ComboBox<>();
            for (Classe classe : modele.getGestionnaireClasses().getClasses()) {
                if(!classe.getIsInterface()){
                    comboClasseImplementation.getItems().add(classe.getNom());
                }
            }
            comboClasseImplementation.setPromptText("Classe Implementation");

            //Bouton pour valider
            Button btnValider = new Button("valider");
            btnValider.setOnAction(e -> {
                if(comboClasseImplementation.getValue()!=null && comboClasseInterface.getValue()!=null){
                    Classe classeInterface = modele.gestionnaireClasses.getClasseByNom(comboClasseInterface.getValue());
                    Classe classeImplementation = modele.gestionnaireClasses.getClasseByNom(comboClasseImplementation.getValue());
                    Implementation implementation = new Implementation(classeInterface, classeImplementation);
                    modele.gestionnaireClasses.ajouterImplementation(implementation);
                    modele.notifierObservateurs();
                }
                else{
                    montrerErreur("Une des deux classes n'a pas été spécifiée");
                }
                dialog.close();
            });

            //On ajoute les boutons au layout et on affiche la scene
            layout.getChildren().addAll(labelInstructions, comboClasseInterface, comboClasseImplementation, btnValider);
            Scene dialogScene = new Scene(layout, 300, 200);
            dialog.setScene(dialogScene);
            dialog.showAndWait();
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
                CheckBox interfaceCheck = new CheckBox("Interface");
                Alert interfaceDialog = new Alert(Alert.AlertType.CONFIRMATION);
                interfaceDialog.setTitle("Type de classe");
                interfaceDialog.setHeaderText("Précisez si la classe est une interface ou non.");
                interfaceDialog.getDialogPane().setContent(interfaceCheck);
                boolean isInterface = interfaceDialog.showAndWait()
                        .filter(response -> response == ButtonType.OK)
                        .map(response -> interfaceCheck.isSelected())
                        .orElse(false);
                Classe nouvelleClasse = new Classe(nom, visibiliteCode,isInterface);
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
        menuExporter.getItems().add(menuItemExportJava);
        menuAffichage.getItems().add(menuItemAffichageClasses);
        menuAjouter.getItems().addAll(menuItemAjouterClasse, menuItemAjouterMethode,menuHeritage,menuImplementation);
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


