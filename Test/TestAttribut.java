import org.junit.Test;
import SAE.Attribut;

import static org.junit.Assert.assertEquals;

public class TestAttribut {
    @Test
    public void testGetNom() {
        Attribut a = new Attribut("nom", String.class);
        assertEquals("nom", a.getNom());
    }

    @Test
    public void testGetType() {
        Attribut a = new Attribut("nom", String.class);
        assertEquals(String.class, a.getType());
    }
}
