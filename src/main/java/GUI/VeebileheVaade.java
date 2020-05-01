package GUI;

import ImageScraper.VeebiKlient;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.IOException;

public class VeebileheVaade extends BorderPane {
    private VeebiKlient klient;

    public VeebileheVaade(VeebiKlient klient, MenüüNupud menüüNupud){
        this.klient = klient;

        Label veebilehtTekst = new Label("Veebileht");
        TextField veebileheAadress = new TextField("");
        veebileheAadress.setPromptText("Kirjuta siia veebilehe aadress");
        HBox.setHgrow(veebileheAadress, Priority.ALWAYS);
        Button otsiNupp = new Button("Otsi");
        HBox sisend = new HBox(15, veebilehtTekst, veebileheAadress, otsiNupp);
        sisend.setAlignment(Pos.CENTER);
        Label eelvaateTekst = new Label("Eelvaade");
        this.setTop(sisend);
        BorderPane.setAlignment(sisend, Pos.CENTER);
        this.setCenter(eelvaateTekst);
        BorderPane.setAlignment(eelvaateTekst, Pos.CENTER);


        BorderPane vaade = this;
        otsiNupp.setOnAction( inputEvent -> {
            if (!veebileheAadress.getText().isEmpty()) {
                menüüNupud.lülita();

                ProgressIndicator olek = new ProgressIndicator(-1);
                vaade.setCenter(olek);
                DoubleBinding eelvaateKõrgus = (VeebileheVaade.this.heightProperty().subtract(sisend.heightProperty())).multiply(0.9);
                DoubleBinding eelvaateLaius = VeebileheVaade.this.widthProperty().multiply(0.9);

                ImageView eelvaade = new ImageView();
                eelvaade.fitHeightProperty().bind(eelvaateKõrgus);
                eelvaade.fitWidthProperty().bind(eelvaateLaius);

                Thread veebileht = new Thread(() ->
                {

                    klient.get(veebileheAadress.getText());
                    Image piltLehest = null;
                    try {
                        piltLehest = SwingFXUtils.toFXImage(klient.teeLehePilt(), null);
                        eelvaade.setImage(piltLehest);
                        Platform.runLater(() -> vaade.setCenter(eelvaade));

                    } catch (IOException e) {
                        Platform.runLater(() -> vaade.setCenter(new Text("Viga, proovi uuesti!")));
                    } finally {
                        Platform.runLater(() -> menüüNupud.lülita());
                    }
                });
                veebileht.start();
            }
        });

        veebileheAadress.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER){
                otsiNupp.fire();
            }
        });
    }
}