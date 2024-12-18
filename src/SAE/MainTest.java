package SAE;

import java.util.List;

public class MainTest {
    public static void main(String[] args) {
        GestionnaireClasses gestionnaire = new GestionnaireClasses();
        ImporteurProjet importeur = new ImporteurProjet(gestionnaire);
        importeur.importerProjet("src/SAE");
        ExportateurSourcePlantUML e = new ExportateurSourcePlantUML();
        System.out.println(e.exporterDiagramme(gestionnaire));
    }
}
