package GUI;

import ImageScraper.VeebiKlient;
import com.sun.javafx.css.StyleManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

public class KasutajaLiides extends Application {

    private final static int PADDING = 20;
    VeebiKlient klient;

    @Override
    public void start(Stage pealava) throws IOException {

        //Loeme eelistused sätete failist.
        Properties eelistused = Satted.loeSätted("app.properties");
        int aknaKõrgus = Integer.parseInt(eelistused.getProperty("aken.korgus"));
        int aknaLaius = Integer.parseInt(eelistused.getProperty("aken.laius"));
        String stiil = ClassLoader.getSystemClassLoader().getResource(eelistused.getProperty("stiil")).toExternalForm();
        Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
        //StyleManager.getInstance().addUserAgentStylesheet(stiil);

        //Käivitame veebikliendi
        System.setProperty("webdriver.chrome.silentOutput", "true");                        // peidab kasutaja jaoks ebaolulise info
        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);   // Ei tekita üleliigset logi
        WebDriverManager.chromedriver().setup();                                            //Tõmbab vajadusel alla chromedriveri kui seda ei eksisteeri
        klient = new VeebiKlient();

        //Peavaade, kuhu sisse pannakse kõik ülejäänud elemendid.
        BorderPane peaVaade = new BorderPane();
        peaVaade.setPadding(new Insets(PADDING));

        //Haldab järjestikuste vaadete vahetamist
        VaateHaldur vaateHaldur = new VaateHaldur(peaVaade);

        //Vaadete vahel navigeermiseks nupud
        MenüüNupud menüüNupud = new MenüüNupud(vaateHaldur);
        peaVaade.setBottom(menüüNupud);

        // Infovaade
        InfoVaade infoVaade = new InfoVaade(klient, pealava);

        //Vaated, mille vahel haldur vahetab
        Pane[] vaated = new Pane[]{new VeebileheVaade(klient, menüüNupud),infoVaade};
        vaateHaldur.setVaated(vaated);

        Scene stseen = new Scene(peaVaade, aknaKõrgus, aknaLaius);

        pealava.setOnCloseRequest(e -> klient.quit());
        pealava.setTitle("ImageScraper");
        pealava.setScene(stseen);
        pealava.show();

    }

    @Override
    public void stop(){
        System.out.println("Stop was called");
        klient.quit();
    }
}