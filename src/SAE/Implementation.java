package SAE;

import java.util.Objects;

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

    public String getNom() {
        return this.classeInterface.getNom() + " implements " + this.classeImplementation.getNom();
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Implementation implementation = (Implementation) obj;
        return classeImplementation.equals(implementation.classeImplementation) &&
        classeInterface.equals(implementation.classeInterface);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classeImplementation, classeInterface);
    }

}
