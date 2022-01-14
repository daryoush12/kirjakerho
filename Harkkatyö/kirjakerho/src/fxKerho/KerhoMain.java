package fxKerho;
	
import javafx.application.Application;
import javafx.stage.Stage;
import kerho.Kerho;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;

/**
 * Pääohjelma rekisterin käynnistämiseksi.
 * @author psvaltus
 * @version 27.4.2019
 * 
 */
public class KerhoMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			final FXMLLoader ldr = new FXMLLoader(getClass().getResource("KerhoGUIView.fxml"));
			final Pane root = (Pane)ldr.load();
			final KerhoGUIController kerhoController = (KerhoGUIController)ldr.getController();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("kerho.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Kirjakerho");
			Kerho kerho = new Kerho();
			kerhoController.setKerho(kerho);
			kerhoController.avaa();
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Käynnistetään käyttöliittymä.
	 * @param args ei käytössä
	 */
	public static void main(String[] args) {
		launch(args);
	}
}