package SAE;

public class TestSquelette {
    public static void main(String[] args) {
        try {
            //Test de l'exportateur de squelette
            ExportateurSqueletteJava.GenererSource("SAE.Methode", "src/SAE/test1");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}