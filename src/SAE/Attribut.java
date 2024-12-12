package SAE;

public class Attribut {
    private String nom;
    private int acces; // 0 = public, 1 = protected, 2 = private
    private Class type;

    public Attribut(String nom, Class type) {
        this.nom = nom;
        this.type = type;
    }

    public Attribut(String nom, Class type, int acces) {
        this.nom = nom;
        this.type = type;
        this.acces = acces;
    }

    public String getNom() {
        return this.nom;
    }

    public Class getType() {
        return this.type;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setType(Class type) {
        this.type = type;
    }
}
