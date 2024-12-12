package SAE;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        TextField textField = new TextField();
        Scene scene = new Scene(textField);
        
        stage.setTitle("SAE");
        stage.setScene(scene);
        stage.show();
    }
}
