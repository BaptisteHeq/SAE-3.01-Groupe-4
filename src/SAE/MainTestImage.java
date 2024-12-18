package SAE;

import java.io.IOException;

public class MainTestImage {
    public static void main(String[] args) {

        GestionnaireClasses gestionnaire = new GestionnaireClasses();
        ImporteurProjet importeur = new ImporteurProjet(gestionnaire);
        importeur.importerProjet("src/SAE");

        ExportateurImagePlantUML exportateurImage = new ExportateurImagePlantUML();
        
        exportateurImage.exporterImage(gestionnaire, "C:/Users/dieud/OneDrive/univ/2A/diagramme.png");
    }
}
