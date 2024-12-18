package SAE;

import javafx.scene.control.Label;

public class VueClasse extends Label implements Observateur {

    private Modele modelClasse;

    public VueClasse(Modele modelClasse){
        this.modelClasse=modelClasse;
    }

    @Override
    public void actualiser(Sujet s) {
    }
}
