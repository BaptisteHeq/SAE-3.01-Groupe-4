package SAE;

public class Attribut {
    private String nom;
    private int type; // 0 = void, 1 = int, 2 = String, 3 = boolean

    public Attribut(String nom, int type) {
        this.nom = nom;
        this.type = type;
    }

    public String getNom() {
        return this.nom;
    }

    public int getType() {
        return this.type;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setType(int type) {
        this.type = type;
    }
}
