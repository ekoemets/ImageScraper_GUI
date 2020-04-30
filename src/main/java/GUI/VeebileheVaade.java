package GUI;

import ImageScraper.VeebiKlient;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.IOException;

public class VeebileheVaade extends BorderPane {
    private VeebiKlient klient;
    HBox sisend;

    public VeebileheVaade(VeebiKlient klient, MenüüNupud menüüNupud){
        this.klient = klient;

        Label veebilehtTekst = new Label("Veebileht");
        TextField veebileheAadress = new TextField("");
        veebileheAadress.setPromptText("Kirjuta siia veebilehe aadress");
        HBox.setHgrow(veebileheAadress, Priority.ALWAYS);
        Button otsiNupp = new Button("Otsi");
        this.sisend = new HBox(15, veebilehtTekst, veebileheAadress, otsiNupp);
        this.sisend.setAlignment(Pos.CENTER);

        Text eelvaateTekst = new Text("Eelvaade");

        otsiNupp.setOnMouseClicked(mouseEvent -> {
            if (!veebileheAadress.getText().isEmpty()) {
                menüüNupud.lülita();
                ProgressBar olek = new ProgressBar(-1);
                Text laadimine = new Text("Laen eelvaadet.");
                this.setCenter(olek);
                double eelvaateKõrgus = (this.getHeight() - sisend.getHeight()) * 0.9;
                double eelvaateLaius = this.getWidth() * 0.9;

                ImageView eelvaade = new ImageView();
                eelvaade.setFitHeight(eelvaateKõrgus);
                eelvaade.setFitWidth(eelvaateLaius);

                BorderPane pane = this;
                Thread veebileht = new Thread(() ->
                {

                    klient.get(veebileheAadress.getText());
                    Image piltLehest = null;
                    try {
                        piltLehest = SwingFXUtils.toFXImage(klient.teeLehePilt(), null);
                        eelvaade.setImage(piltLehest);
                        Platform.runLater(() -> pane.setCenter(eelvaade));

                    } catch (IOException e) {
                        Platform.runLater(() -> pane.setCenter(new Text("Viga, proovi uuesti!")));
                    } finally {
                        Platform.runLater(() -> menüüNupud.lülita());
                    }
                });
                veebileht.start();
            }
        });

        this.setTop(sisend);
        BorderPane.setAlignment(sisend, Pos.CENTER);
        this.setCenter(eelvaateTekst);
        BorderPane.setAlignment(eelvaateTekst, Pos.CENTER);
    }
}