package SAE;

import java.util.ArrayList;
import java.util.List;

public class Classe {
    private String nom;
    private int acces; // 0 = public, 1 = protected, 2 = private
    private List methodes;
    private List attributs;
    private boolean visible;

    public Classe(String nom, int acces) {
        this.nom = nom;
        this.methodes = new ArrayList();
        this.attributs = new ArrayList();
        this.acces = acces;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Classe classe = (Classe) obj;
        return nom != null && nom.equals(classe.nom); // Comparaison basée sur le nom
    }

    @Override
    public int hashCode() {
        return nom != null ? nom.hashCode() : 0; // HashCode basé sur le nom
    }

}
