package SAE;

public class Implementation {
    private Classe classeInterface;
    private Classe classeImplementation;

    public Implementation(Classe classeInterface, Classe classeImplementation) {
        this.classeInterface = classeInterface;
        this.classeImplementation = classeImplementation;
    }

    public Classe getClasseInterface() {
        return this.classeInterface;
    }

    public Classe getClasseImplementation() {
        return this.classeImplementation;
    }
}
