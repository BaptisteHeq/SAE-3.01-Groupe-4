package SAE;

import java.util.ArrayList;
import java.util.List;

public class Classe {
    private String nom;
    private int acces; // 0 = public, 1 = protected, 2 = private
    private List methodes;
    private List attributs;

    public Classe(String nom, int acces) {
        this.nom = nom;
        this.methodes = new ArrayList();
        this.attributs = new ArrayList();
        this.acces = acces;
    }

    public void addMethode(Methode methode) {
        this.methodes.add(methode);
    }

    public int getAcces() {
        return this.acces;
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
