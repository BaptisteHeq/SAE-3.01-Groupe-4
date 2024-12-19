package SAE;

import java.util.Objects;

public class Heritage {
    private Classe classeMere;
    private Classe classeFille;

    public Heritage(Classe classeMere, Classe classeFille) {
        this.classeMere = classeMere;
        this.classeFille = classeFille;
    }

    public Classe getClasseMere() {
        return this.classeMere;
    }

    public Classe getClasseFille() {
        return this.classeFille;
    }

    public String getNom() {
        return this.classeFille.getNom() + " extends " + this.classeMere.getNom();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Heritage heritage = (Heritage) obj;
        return classeFille.equals(heritage.classeFille) && classeMere.equals(heritage.classeMere);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classeFille, classeMere);
    }

}
