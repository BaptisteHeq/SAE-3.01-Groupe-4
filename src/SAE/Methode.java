package SAE;

import java.util.List;

public class Methode {
    private int acces; // 0 = public, 1 = protected, 2 = private
    private String nom;
    private Class typeRetour;
    private List<Attribut> parametres;
    private boolean visible;

    public Methode(int acces, String nom, Class typeRetour, List<Attribut> parametres) {
        this.acces = acces;
        this.nom = nom;
        this.typeRetour = typeRetour;
        this.parametres = parametres;
        this.visible = true;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible() {
        this.visible = visible;
    }

    public void setInvisible() {
        this.visible = false;
    }


    public int getAcces() {
        return this.acces;
    }

    public String getNom() {
        return this.nom;
    }

    public Class getTypeRetour() {
        return this.typeRetour;
    }

    public List<Attribut> getParametres() {
        return this.parametres;
    }
}
