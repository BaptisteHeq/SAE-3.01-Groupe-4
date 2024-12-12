import SAE.Attribut;
import SAE.Classe;
import SAE.Methode;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestClasse{

    @Test
    public void testGetNom() {
        Classe c = new Classe("nom",0);
        assertEquals("nom", c.getNom());
    }

    @Test
    public void testGetMethodes() {
        Classe c = new Classe("nom",0);
        Methode m = new Methode(0, "methode", String.class, new ArrayList<>());
        c.addMethode(m);
        assertEquals("methode", c.getMethodes().get(0).getNom());
    }

    @Test
    public void testRemoveMethodes() {
        Classe c = new Classe("nom",0);
        Methode m = new Methode(0, "methode", String.class, new ArrayList<>());
        c.addMethode(m);
        int size1 = c.getMethodes().size();
        c.removeMethode(m);
        int size2 = c.getMethodes().size();
        assertEquals(1, size1);
        assertEquals(0, size2);
    }

    @Test
    public void testAddAttribut() {
        Classe c = new Classe("nom",0);
        Attribut a = new Attribut("attribut", String.class);
        c.addAttribut(a);
        assertEquals("attribut", c.getAttributs().get(0).getNom());
    }

    @Test
    public void testRemoveAttribut() {
        Classe c = new Classe("nom",0);
        Attribut a = new Attribut("attribut", String.class);
        c.addAttribut(a);
        int size1 = c.getAttributs().size();
        c.removeAttribut(a);
        int size2 = c.getAttributs().size();
        assertEquals(1, size1);
        assertEquals(0, size2);
    }

    @Test
    public void testGetAcces() {
        Classe c = new Classe("nom",0);
        assertEquals(0, c.getAcces());
    }
}
