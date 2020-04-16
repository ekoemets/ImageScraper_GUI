package GUI;

import ImageScraper.VeebiKlient;
import io.github.bonigarcia.wdm.WebDriverManager;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

public class KasutajaLiides extends Application {

    private final static int PADDING = 20;
    private Pane[] vaated;
    private int hetkeVaade;
    VeebiKlient klient;

    @Override
    public void start(Stage pealava) throws IOException {

        //Loeme eelistused sätete failist.
        Properties eelistused = Satted.loeSätted("app.properties");
        int aknaKõrgus = Integer.parseInt(eelistused.getProperty("aken.korgus"));
        int aknaLaius = Integer.parseInt(eelistused.getProperty("aken.laius"));
        String stiil = this.getClass().getResource(eelistused.getProperty("stiil")).toExternalForm();

        //Käivitame veebikliendi
        System.setProperty("webdriver.chrome.silentOutput", "true");                     // peidab kasutaja jaoks ebaolulise info
        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);   // Ei tekita üleliigset logi
        WebDriverManager.chromedriver().setup();                                         //Tõmbab vajadusel alla chromedriveri kui seda ei eksisteeri
        klient = new VeebiKlient();


        hetkeVaade = 0;
        VBox vaateHoidja = new VBox();
        vaateHoidja.setPadding(new Insets(PADDING));
        vaateHoidja.getStylesheets().add(stiil);

        Scene stseen = new Scene(vaateHoidja, aknaKõrgus, aknaLaius);
        vaated = new Pane[]{new VeebileheVaade(klient, stseen.heightProperty(), stseen.widthProperty())};

        vaateHoidja.getChildren().add(vaated[hetkeVaade]);


        pealava.setOnCloseRequest(e -> klient.close());
        pealava.setScene(stseen);
        pealava.show();


    }

    @Override
    public void stop(){
        klient.quit();
    }
}
