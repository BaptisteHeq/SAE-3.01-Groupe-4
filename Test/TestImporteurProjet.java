import SAE.Classe;
import SAE.ImporteurProjet;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestImporteurProjet {

    @Test
    public void testNomClasses() {
        ImporteurProjet i = new ImporteurProjet();
        List<Classe> classes = i.importerProjet("src/SAE");
        assertEquals(5, classes.size());
        //on recupere le noms des classes car on ne peut pas comparer directement les objets
        List<String> nomclasses = new ArrayList<>();
        for(Classe c : classes) {
            nomclasses.add(c.getNom());
        }
        assertEquals(true, nomclasses.contains("SAE.Attribut"));
        assertEquals(true, nomclasses.contains("SAE.Classe"));
        assertEquals(true, nomclasses.contains("SAE.ImporteurProjet"));
        assertEquals(true, nomclasses.contains("SAE.Methode"));
        assertEquals(true, nomclasses.contains("SAE.Main"));
    }
}
