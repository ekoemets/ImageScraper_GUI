package GUI;

import ImageScraper.FailiHaldur;
import ImageScraper.Kuvatommis;
import ImageScraper.VeebiKlient;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;

public class SalvestamiseAken {

    public static void tooEsile(VeebiKlient klient, String kaustaTee, String tyyp, String atribuut, boolean tolgitakse){
        Stage taust = new Stage();
        taust.setTitle("Salvestab pilte");
        taust.initModality(Modality.APPLICATION_MODAL);
        taust.setResizable(false);

        Label selgitusSilt = new Label("Programm otsib internetist sinu valitud elemente.\n" +
                "Palun oota natuke (:\n" +
                "Kui valisid piltide tõlkimise siis võib minna kuni minut.");
        HBox selgitusKast = new HBox(selgitusSilt);
        selgitusKast.setAlignment(Pos.CENTER);
        selgitusSilt.setStyle("-fx-font-size: 16px; -fx-text-alignment: center;");

        Button sulgeNupp = new Button("OK!");
        sulgeNupp.setOnMouseClicked(mouseEvent -> taust.close());
        // et kasutaja ei saaks poole protsessi pealt akent kinni panna
        sulgeNupp.setDisable(true);

        BorderPane sisu = new BorderPane();
        sisu.setTop(selgitusKast);
        sisu.setCenter(sulgeNupp);

        Scene stseen = new Scene(sisu,400,150);
        taust.setScene(stseen);
        taust.show();

        Thread salvestamine = new Thread(() ->{
            try {
                switch (tyyp) {
                    case "class":
                        klient.teeKuvatõmmised(By.className(atribuut));
                        break;
                    case "tag":
                        klient.teeKuvatõmmised(By.tagName(atribuut));
                        break;
                    case "id":
                        klient.teeKuvatõmmised(By.id(atribuut));
                        break;
                    case "css (asjatundjatele)":
                        klient.teeKuvatõmmised(By.cssSelector(atribuut));
                        break;
                }
            } catch (IOException e){
                VeaAken.tooEsile("Kuvatõmmiste tegemisel läks midagi valesti :[");
            }
            if (atribuut.equals("img")){
                if (tolgitakse) {
                    // avab brauseris Google tõlke
                    klient.get("https://translate.google.com/?hl=en#view=home&op=translate&sl=en&tl=et");
                    int pildiIndeks = 1;
                    String tõlge;
                    for (Kuvatommis pilt : klient.getKuvatõmmised()) {
                        // puhastab tõlkimise sisendi kasti
                        klient.findElementByXPath("//textarea[@id='source']").clear();

                        // sisestab pildi nime tõlkimiseks
                        klient.findElementByXPath("//textarea[@id='source']").sendKeys(pilt.getNimi().replaceAll("_", "-"));

                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            // sleep on igaks-juhuks jäetud, seega siin try-catch lihtsalt väldib kompileerimiserrorit
                            // ja erinditöötlemise puudumine ei ole probleemiks
                            continue;
                        }
                        // ootab kuni on tõlgitud või on möödunud 3 sekundit
                        WebDriverWait oota = new WebDriverWait(klient, 3);
                        oota.until(ExpectedConditions.visibilityOfElementLocated(new By.ByXPath("//span[@class='tlid-translation translation']/span")));

                        try {
                            // loeb tõlke väljundikastist
                            tõlge = klient.findElementByXPath("//span[@class='tlid-translation translation']/span").getText();
                        } catch (Exception e) {
                            System.out.println("Ei suutnud seda nime tõlkida.");

                            // kui tõlkimine ebaõnnestub, paneb pildile üldise nime, et vältida ülesalvestamist
                            tõlge = "pilt" + pildiIndeks;
                            pildiIndeks++;
                        }
                        // nimetab pildi eestikeelseks, eeldusel et tõlkimine oli edukas
                        pilt.setNimi(tõlge);
                    }
                }
            }
            else{
                int i = 1;
                for (Kuvatommis pilt: klient.getKuvatõmmised()) {
                    // kuna tegu on suvalise elemenditüübiga, nummerdatakse tõmmised, et vältida ülesalvestamist
                    pilt.setNimi(pilt.getNimi() + i);
                    i++;
                }
            }
            // salvestab kõik pildid kasutaja arvutisse
            try {
                FailiHaldur.salvestaPildid(klient.getKuvatõmmised(), kaustaTee);
            } catch (IOException e) {
                VeaAken.tooEsile("\nPiltide salvestamisel läks midagi valesti :[");
            }

            if (klient.getKuvatõmmised().size() > 0) {
                Platform.runLater(()-> selgitusSilt.setText("\nEdukalt salvestati "+klient.getKuvatõmmised().size() +" pilti."));
            }
            else
                Platform.runLater(()-> selgitusSilt.setText("\nKahjuks sellise atribuudiga pilte ei leidnud\n" +
                                                            "ega õnnestunud salvestada."));

            // kasutaja saab nüüd ise sulgeda akna.
            Platform.runLater(()-> sulgeNupp.setDisable(false));
        });
        salvestamine.start();
    }
}