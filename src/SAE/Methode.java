package SAE;

import java.util.List;

public class Methode {
    private int acces; // 0 = public, 1 = protected, 2 = private
    private String nom;
    private int typeRetour; // 0 = void, 1 = int, 2 = String, 3 = boolean
    private List<Attribut> parametres;

    public Methode(int acces, String nom, int typeRetour, List<Attribut> parametres) {
        this.acces = acces;
        this.nom = nom;
        this.typeRetour = typeRetour;
        this.parametres = parametres;
    }

    public int getAcces() {
        return this.acces;
    }

    public String getNom() {
        return this.nom;
    }

    public int getTypeRetour() {
        return this.typeRetour;
    }

    public List<Attribut> getParametres() {
        return this.parametres;
    }
}
