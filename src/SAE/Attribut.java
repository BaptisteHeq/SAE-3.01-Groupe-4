package SAE;

public class Attribut {
    private String nom;
    private String type;

    public Attribut(String nom, String type) {
        this.nom = nom;
        this.type = type;
    }

    public String getNom() {
        return this.nom;
    }

    public String getType() {
        return this.type;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setType(String type) {
        this.type = type;
    }
}
