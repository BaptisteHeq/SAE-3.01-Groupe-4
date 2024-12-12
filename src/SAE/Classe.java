package SAE;

import java.util.ArrayList;
import java.util.List;

public class Classe {
    private String nom;
    private List methodes;
    private List attributs;

    public Classe(String nom) {
        this.nom = nom;
        this.methodes = new ArrayList();
        this.attributs = new ArrayList();
    }

    public void addMethode(Methode methode) {
        this.methodes.add(methode);
    }

    public void addAttribut(Attribut attribut) {
        this.attributs.add(attribut);
    }

    public void removeAttribut(Attribut attribut) {
        this.attributs.remove(attribut);
    }

    public List<Attribut> getAttributs() {
        return this.attributs;
    }

    public void removeMethode(Methode methode) {
        this.methodes.remove(methode);
    }

    public List<Methode> getMethodes() {
        return this.methodes;
    }

    public String getNom() {
        return this.nom;
    }
}
