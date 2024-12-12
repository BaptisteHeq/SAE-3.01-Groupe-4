import SAE.Methode;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TestMethode {
    @Test
    public void testGetNom() {
        Methode m = new Methode(0, "nom", 0, new ArrayList<>());
        assertEquals("nom", m.getNom());
    }

    @Test
    public void testGetAcces() {
        Methode m = new Methode(0, "nom", 0, new ArrayList<>());
        assertEquals(0, m.getAcces());
    }

    @Test
    public void testGetTypeRetour() {
        Methode m = new Methode(0, "nom", 0, new ArrayList<>());
        assertEquals(0, m.getTypeRetour());
    }

    @Test
    public void testGetParametres() {
        Methode m = new Methode(0, "nom", 0, new ArrayList<>());
        assertEquals(0, m.getParametres().size());
    }
}
