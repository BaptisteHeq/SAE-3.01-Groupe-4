package SAE;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

//importer par maven net.sourceforge.plantuml:plantuml:1.2024.4

public class ExportateurImagePlantUML {
    public void exporterImage(GestionnaireClasses gestionnaire, String cheminSortie){
        ExportateurSourcePlantUML e = new ExportateurSourcePlantUML();
        String codeSource = e.exporterDiagramme(gestionnaire);

        SourceStringReader reader = new SourceStringReader(codeSource);

        File fichierExport = new File(cheminSortie);

        try{
            OutputStream os = new java.io.FileOutputStream(fichierExport);
            reader.generateImage(os,new FileFormatOption(FileFormat.PNG));
            System.out.println("Exportation réussie : " + fichierExport.getAbsolutePath());
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }
}
