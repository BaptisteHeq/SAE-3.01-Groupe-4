import SAE.Attribut;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


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
