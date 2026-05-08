import db.DBConnector;
import db.DBInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        DBInitializer.initializeDB();
        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("/views/entryscene.fxml")
        );

        Scene scene = new Scene(loader.load(), 1600, 900);

        stage.setTitle("Medical Project");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}