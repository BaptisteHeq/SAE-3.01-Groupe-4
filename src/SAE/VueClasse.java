package SAE;

import javafx.scene.layout.VBox;

public class VueClasse extends VBox implements Observateur {

    private Modele modelClasse;

    public VueClasse(Modele modelClasse){
        this.modelClasse=modelClasse;
    }

    @Override
    public void actualiser(Sujet s) {

    }
}
