package SAE;

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
}
