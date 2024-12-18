package SAE;

import java.util.ArrayList;
import java.util.List;

public class GestionnaireClasses {
    private List<Classe> classes;
    private List<Heritage> heritages;
    private List<Association> associations;
    private List<Implementation> implementations;

    public GestionnaireClasses() {
        this.classes = new ArrayList<Classe>();
        this.heritages = new ArrayList<Heritage>();
        this.associations = new ArrayList<Association>();
        this.implementations = new ArrayList<Implementation>();
    }

    public void ajouterClasse(Classe classe) {
        this.classes.add(classe);
    }

    public void ajouterHeritage(Heritage heritage) {
        this.heritages.add(heritage);
    }

    public void ajouterAssociation(Association association) {
        this.associations.add(association);
    }

    public void ajouterImplementation(Implementation implementation) {
        this.implementations.add(implementation);
    }

    public List<Classe> getClasses() {
        return this.classes;
    }

    public List<Heritage> getHeritages() {
        return this.heritages;
    }

    public List<Association> getAssociations() {
        return this.associations;
    }

    public List<Implementation> getImplementations() {
        return this.implementations;
    }
}
