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
        /*if(!contientClasse(classe))
            this.classes.add(classe);
        else{
            //fusionner les deux classes qui ont le même nom
            for(Classe c : this.classes){
                if(c.getNom().equals(classe.getNom())){
                    for(Methode m : classe.getMethodes()){
                        if(!c.getMethodes().contains(m))
                            c.addMethode(m);
                    }
                    for(Attribut a : classe.getAttributs()){
                        if(!c.getAttributs().contains(a))
                            c.addAttribut(a);
                    }
                }
            }
        }
        */
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

    public boolean contientClasse(Classe classe) {
        String nomClasse = classe.getNom();
        for (Classe c : this.classes) {
            if (c.getNom().equals(nomClasse)) {
                return true;
            }
        }
        return false;
    }

    public void nettoyerDoublons(){
        /*for(Classe c : this.classes){
            for(Classe c2 : this.classes){
                if(c.getNom().equals(c2.getNom())){
                    for(Methode m : c2.getMethodes()){
                        c.addMethode(m);
                    }
                    for(Attribut a : c2.getAttributs()){
                        c.addAttribut(a);
                    }
                    this.classes.remove(c2);

                }
            }
        }*/
        //plante car on supprime pendant qu'on itere (je pense)

        for (int i = 0; i < this.classes.size(); i++) {
            for (int j = i + 1; j < this.classes.size(); j++) {
                if (this.classes.get(i).getNom().equals(this.classes.get(j).getNom())) {
                    for (Methode m : this.classes.get(j).getMethodes()) {
                        if (!this.classes.get(i).getMethodes().contains(m)) {
                            this.classes.get(i).addMethode(m);
                        }
                    }
                    for (Attribut a : this.classes.get(j).getAttributs()) {
                        if (!this.classes.get(i).getAttributs().contains(a)) {
                            this.classes.get(i).addAttribut(a);
                        }
                    }
                    this.classes.remove(j);
                    j--;
                }
            }
        }
    }
}
