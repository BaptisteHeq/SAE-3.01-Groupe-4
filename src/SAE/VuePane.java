package SAE;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Scale;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VuePane extends BorderPane implements Observateur {
    private Modele modele;

    private double scaleFactor = 1.0;
    private Scale scale;
    private Pane centerPane = new Pane();
    private Group lignesGroup = new Group(); // Groupe pour les lignes
    private Group classesGroup = new Group(); // Groupe pour les classes
    ScrollPane scrollPane = new ScrollPane(centerPane);

    public VuePane(Modele modele) {
        this.modele = modele;

        centerPane.getChildren().addAll(lignesGroup, classesGroup);


        // Configurer le ScrollPane
        scrollPane.setPannable(true); //clic-glissé pour naviguer
        scrollPane.setFitToWidth(true); //dépasser en largeur
        scrollPane.setFitToHeight(true); //dépasser en hauteur

        //molette zoom/dezoom
        centerPane.setOnScroll(this::handleZoom);

        //etat initial du zoom
        scale = new Scale(scaleFactor, scaleFactor);
        centerPane.getTransforms().add(scale);

        this.setCenter(scrollPane);
    }

    private void handleZoom(ScrollEvent event) {
        if (event.getDeltaY() > 0) {
            scaleFactor *= 1.1;
        } else {
            scaleFactor *= 0.9;
        }

        scale.setX(scaleFactor);
        scale.setY(scaleFactor);

        event.consume();
    }
    @Override
    public void actualiser(Sujet s) {
        List<Classe> classesTriees = modele.getGestionnaireClasses().trierClassesParDependances();
        lignesGroup.getChildren().clear();
        classesGroup.getChildren().clear();

        //1: calculer l'importance des classes
        Map<Classe, Integer> importance = calculerImportance(classesTriees);

        //trier les classes
        classesTriees.sort((c1, c2) -> importance.get(c2) - importance.get(c1));

        //2: le centre de mon positionnement
        double centerX = 500;
        double centerY = 400;
        //ajouter un point rouge au centre
        javafx.scene.shape.Circle circle = new javafx.scene.shape.Circle(centerX, centerY, 5);
        circle.setFill(javafx.scene.paint.Color.RED);
        centerPane.getChildren().add(circle);
        double radiusStep = 150; //espace entre les cercles de classes
        double angleStep = 360.0 / classesTriees.size();
        double radius = 150; //rayon du premier cercle

        double maxX = centerX;
        double maxY = centerY;

        //chaque classe à son VBox
        Map<Classe, VBox> vBoxHashMap = new HashMap<>();
        int index = 0;

        for (Classe c : classesTriees) {
            if(c.isVisible()) {
                VBox vPrincipale = creerBoiteClasse(c);

                double angle = Math.toRadians(index * angleStep);
                double x = centerX + Math.cos(angle) * radius;
                double y = centerY + Math.sin(angle) * radius;

                //augmenter le rayon quand c'est complet
                if (index % Math.max(6, classesTriees.size() / 3.5) == 0) {
                    radius += radiusStep;
                    angleStep = (360.0 / (classesTriees.size() - index)) * 1.5; //recalibrer l'angle
                }

                vPrincipale.setLayoutX(x);
                vPrincipale.setLayoutY(y);

                // Suivre les limites maximales
                maxX = Math.max(maxX, x + 100);
                maxY = Math.max(maxY, y + 100);

                //glisser deplacer
                vPrincipale.setOnMousePressed(event -> {
                    //position de la souris
                    vPrincipale.setUserData(new double[]{event.getSceneX(), event.getSceneY()});
                });

                vPrincipale.setOnMouseDragged(event -> {
                    scrollPane.setPannable(false);
                    //position de base
                    double[] sourisPos = (double[]) vPrincipale.getUserData();
                    centerPane.getChildren().removeIf(node -> node instanceof Line);
                    ajouterLignesRelations(vBoxHashMap);
                    if (sourisPos != null) {
                        double deltaX = event.getSceneX() - sourisPos[0];
                        double deltaY = event.getSceneY() - sourisPos[1];
                        //Déplacer la classe
                        vPrincipale.setLayoutX(vPrincipale.getLayoutX() + deltaX);
                        vPrincipale.setLayoutY(vPrincipale.getLayoutY() + deltaY);
                        vPrincipale.setUserData(new double[]{event.getSceneX(), event.getSceneY()});
                    }
                    lignesGroup.getChildren().clear();
                    ajouterLignesRelations(vBoxHashMap);
                });

                vBoxHashMap.put(c, vPrincipale);
                classesGroup.getChildren().add(vPrincipale);

                index++;
            }
        }

        //3 : lignes pour les relations
        ajouterLignesRelations(vBoxHashMap);
        //Pour mettre les flèches en 1er plan
        lignesGroup.toFront();

        //4 : ScrollPane
        centerPane.setMinWidth(maxX + 200);
        centerPane.setMinHeight(maxY + 200);
    }

    //importance des classes
    private Map<Classe, Integer> calculerImportance(List<Classe> classesTriees) {
        Map<Classe, Integer> importance = new HashMap<>();
        for (Classe c : classesTriees) {
            importance.put(c, 0);
        }

        //ajouter les dépendances au score
        for (Heritage h : modele.getGestionnaireClasses().getHeritages()) {
            importance.put(h.getClasseMere(), importance.get(h.getClasseMere()) + 1);
            importance.put(h.getClasseFille(), importance.get(h.getClasseFille()) + 1);
        }
        for (Association a : modele.getGestionnaireClasses().getAssociations()) {
            importance.put(a.getClasse1(), importance.get(a.getClasse1()) + 1);
            importance.put(a.getClasse2(), importance.get(a.getClasse2()) + 1);
        }
        for (Implementation i : modele.getGestionnaireClasses().getImplementations()) {
            importance.put(i.getClasseInterface(), importance.get(i.getClasseInterface()) + 1);
            importance.put(i.getClasseImplementation(), importance.get(i.getClasseImplementation()) + 1);
        }
        return importance;
    }

    //Vbox
    private VBox creerBoiteClasse(Classe c) {
        VBox vPrincipale = new VBox();
        vPrincipale.setMinWidth(100);
        vPrincipale.setMaxWidth(120);
        vPrincipale.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        VBox vAttributs = new VBox();
        VBox vMethodes = new VBox();

        Label titre = new Label(c.getNom());
        vPrincipale.getChildren().add(titre);

        Label l1 = new Label("Attributs");
        vAttributs.getChildren().add(l1);
        for (Attribut a : c.getAttributs()) {
            Label l = new Label(a.getNom());
            vAttributs.getChildren().add(l);
        }

        Label l4 = new Label("Méthodes");
        vMethodes.getChildren().add(l4);
        for (Methode m : c.getMethodes()) {
            if(m.isVisible()) {
                Label l = new Label(m.getNom());
                vMethodes.getChildren().add(l);
            }
        }

        vMethodes.setBorder(new Border(new BorderStroke(
                javafx.scene.paint.Color.BLACK,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                BorderWidths.DEFAULT
        )));
        vAttributs.setBorder(new Border(new BorderStroke(
                javafx.scene.paint.Color.BLACK,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                BorderWidths.DEFAULT
        )));

        vPrincipale.getChildren().addAll(vAttributs, vMethodes);
        vPrincipale.setBorder(new Border(new BorderStroke(
                javafx.scene.paint.Color.BLACK,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                BorderWidths.DEFAULT
        )));
        vPrincipale.setAlignment(Pos.TOP_CENTER);
        vPrincipale.toFront();

        return vPrincipale;
    }

    //lignes entre les classes
    private void ajouterLignesRelations(Map<Classe, VBox> classeVBoxMap) {
        for (Heritage h : modele.getGestionnaireClasses().getHeritages()) {
            ajouterLigneRelation(classeVBoxMap.get(h.getClasseMere()), classeVBoxMap.get(h.getClasseFille()), "Heritage");
        }
        for (Association a : modele.getGestionnaireClasses().getAssociations()) {
            ajouterLigneRelation(classeVBoxMap.get(a.getClasse1()), classeVBoxMap.get(a.getClasse2()), "Association");
        }
        for (Implementation i : modele.getGestionnaireClasses().getImplementations()) {
            ajouterLigneRelation(classeVBoxMap.get(i.getClasseInterface()), classeVBoxMap.get(i.getClasseImplementation()), "Implementation");
        }
    }

    //Ajouter une ligne entre deux boîtes
    private void ajouterLigneRelation(VBox source, VBox cible, String typeRelation) {
        if (source == null || cible == null) return;

        //Créer une ligne
        Line ligne = new Line();
        ligne.setStartX(source.getLayoutX() + source.getWidth() / 2);
        ligne.setStartY(source.getLayoutY() + source.getHeight() / 2);
        ligne.setEndX(cible.getLayoutX() + cible.getWidth() / 2);
        ligne.setEndY(cible.getLayoutY() + cible.getHeight() / 2);

        //Style de la ligne
        ligne.setStyle("-fx-stroke-width: 2;");
        if (typeRelation.equals("Heritage")) {
            ligne.setStroke(javafx.scene.paint.Color.BLUE);
        } else if (typeRelation.equals("Association")) {
            ligne.setStroke(javafx.scene.paint.Color.GREEN);
        } else if (typeRelation.equals("Implementation")) {
            ligne.setStroke(javafx.scene.paint.Color.ORANGE);
            ligne.getStrokeDashArray().addAll(5.0, 5.0); //Trait pointillé
        }

        ligne.toBack(); //S'assurer que la ligne est derrière les flèches

        //Ajouter la ligne
        lignesGroup.getChildren().add(ligne);

        //Ajouter la pointe de la flèche au trait
        ajouterPointeDeFleche(ligne, typeRelation, lignesGroup);
    }

    //Ajoute une pointe aux traits de relation en fonction du type de relation
    private void ajouterPointeDeFleche(Line ligne, String typeRelation, Group groupe) {
        //Calculer la direction de la ligne
        double startX = ligne.getStartX();
        double startY = ligne.getStartY();
        double endX = ligne.getEndX();
        double endY = ligne.getEndY();
        double angle = Math.atan2(endY - startY, endX - startX);

        //Longueur des côtés de la pointe de flèche
        double arrowLength = 15;
        double arrowWidth = 7;

        //Calculer les points du triangle
        double x1 = endX - arrowLength * Math.cos(angle - Math.PI / 6);
        double y1 = endY - arrowLength * Math.sin(angle - Math.PI / 6);
        double x2 = endX - arrowLength * Math.cos(angle + Math.PI / 6);
        double y2 = endY - arrowLength * Math.sin(angle + Math.PI / 6);

        //Pointes en fonction du type de relation
        if (typeRelation.equals("Association")) {
            //Flèche en ">" (sans base)
            Line side1 = new Line(endX, endY, x1, y1);
            Line side2 = new Line(endX, endY, x2, y2);
            side1.setStroke(ligne.getStroke());
            side1.setStrokeWidth(2);
            side2.setStroke(ligne.getStroke());
            side2.setStrokeWidth(2);

            //Ajouter la pointe
            groupe.getChildren().addAll(side1, side2);
        } else {
            //Pointe vide avec une base pour héritage et implémentation
            Polygon fleche = new Polygon();
            fleche.getPoints().addAll(endX, endY, x1, y1, x2, y2);
            if (typeRelation.equals("Heritage") || typeRelation.equals("Implementation")) {
                fleche.setFill(javafx.scene.paint.Color.TRANSPARENT); //Pointe vide
                fleche.setStroke(ligne.getStroke()); //Contour uniquement
                fleche.setStrokeWidth(2);
            }

            //Ajouter la pointe
            groupe.getChildren().add(fleche);
        }
    }
}