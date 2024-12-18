package SAE;

import java.util.ArrayList;

public class Modele implements Sujet {
    ArrayList<Observateur> observateurs = new ArrayList<Observateur>();
    GestionnaireClasses gestionnaireClasses;

    public Modele(){
    }

    public void remplir(GestionnaireClasses gestionnaireClasses){
        this.gestionnaireClasses = gestionnaireClasses;
    }


    public GestionnaireClasses getGestionnaireClasses() {
        return this.gestionnaireClasses;
    }


    @Override
    public void enregistrerObservateur(Observateur o) {
        this.observateurs.add(o);
        notifierObservateurs();
    }

    @Override
    public void supprimerObservateur(Observateur o) {
        this.observateurs.remove(o);
    }

    @Override
    public void notifierObservateurs() {
        for(Observateur o : this.observateurs){
            o.actualiser(this);
        }
    }
}
