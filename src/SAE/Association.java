package SAE;

public class Association {
    private Classe classe1;
    private Classe classe2;
    private String nom;
    private String cardinalite;

    public Association(Classe classe1, Classe classe2, String nom, String cardinalite) {
        this.classe1 = classe1;
        this.classe2 = classe2;
        this.nom = nom;
        this.cardinalite = cardinalite;
    }

    public Classe getClasse1() {
        return this.classe1;
    }

    public Classe getClasse2() {
        return this.classe2;
    }

    public String getNom() {
        return this.nom;
    }

    public String getCardinalite() {
        return this.cardinalite;
    }

    public String toString() {
        return this.classe1.getNom() + " " + this.cardinalite + " " + this.nom + " " + this.classe2.getNom();
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Association association = (Association) obj;
        return (classe1.equals(association.classe1) && classe2.equals(association.classe2)) ||
                (classe1.equals(association.classe1) && classe2.equals(association.classe2));
    }

    @Override
    public int hashCode() {
        return classe1.hashCode() + classe2.hashCode();
    }

}

