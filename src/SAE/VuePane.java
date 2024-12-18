package SAE;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.control.ScrollPane;

import java.util.List;

public class VuePane extends BorderPane implements Observateur {
    private Modele modele;
    private Pane centerPane;

    public VuePane(Modele modele) {
        this.modele = modele;

        // Créer un Pane pour le contenu et le placer dans un ScrollPane
        centerPane = new Pane();
        ScrollPane scrollPane = new ScrollPane(centerPane);

        // Configurer le ScrollPane
        scrollPane.setPannable(true); // Permet le clic-glissé pour naviguer
        scrollPane.setFitToWidth(false); // Laisse le contenu dépasser en largeur
        scrollPane.setFitToHeight(false); // Laisse le contenu dépasser en hauteur

        this.setCenter(scrollPane); // Ajouter le ScrollPane à la région centrale
    }

    @Override
    public void actualiser(Sujet s) {
        // Récupérer les classes du modèle
        List<Classe> classes = modele.getGestionnaireClasses().getClasses();
        centerPane.getChildren().clear(); // Nettoyer le contenu précédent

        double startX = 50; // Position de départ X
        double startY = 50; // Position de départ Y
        double offsetX = 150; // Décalage horizontal
        double offsetY = 300; // Décalage vertical

        int index = 0;

        for (Classe c : classes) {
            int compteur = 0;
            VBox vPrincipale = new VBox();
            VBox vAttributs = new VBox();
            VBox vMethodes = new VBox();

            Label titre = new Label(c.getNom());
            vPrincipale.getChildren().add(titre);

            Label l1 = new Label("Attributs");
            vAttributs.getChildren().add(l1);
            for (Attribut a : c.getAttributs()) {
                Label l = new Label(a.getNom());
                vAttributs.getChildren().add(l);
                compteur++;
            }

            Label l4 = new Label("Méthodes");
            vMethodes.getChildren().add(l4);
            for (Methode m : c.getMethodes()) {
                Label l = new Label(m.getNom());
                vMethodes.getChildren().add(l);
                compteur++;
            }

            vPrincipale.setMaxSize(100, 100);
            vPrincipale.setBorder(new Border(new BorderStroke(
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
            vMethodes.setBorder(new Border(new BorderStroke(
                    javafx.scene.paint.Color.BLACK,
                    BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY,
                    BorderWidths.DEFAULT
            )));
            vAttributs.setAlignment(Pos.CENTER);
            vMethodes.setAlignment(Pos.CENTER);

            vPrincipale.getChildren().add(vAttributs);
            vPrincipale.getChildren().add(vMethodes);
            vPrincipale.setAlignment(Pos.TOP_CENTER);

            // Positionner la VBox
            double x = startX + (index % 4) * offsetX;
            double y = startY + (index / 4) * offsetY;
            vPrincipale.setLayoutX(x);
            vPrincipale.setLayoutY(y);

            // Ajouter la VBox au Pane
            centerPane.getChildren().add(vPrincipale);

            index++;
        }

        System.out.println("Actualisation de la vue");
    }
}
