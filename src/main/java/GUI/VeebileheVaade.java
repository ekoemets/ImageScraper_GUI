package GUI;

import ImageScraper.VeebiKlient;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.*;
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

                ProgressIndicator olek = new ProgressIndicator(-1);
                //Text laadimine = new Text("Laen eelvaadet.");
                this.setCenter(olek);
                DoubleBinding eelvaateKõrgus = (heightProperty().subtract(sisend.heightProperty())).multiply(0.9);
                DoubleBinding eelvaateLaius = widthProperty().multiply( 0.9);

                ImageView eelvaade = new ImageView();
                eelvaade.fitHeightProperty().bind(eelvaateKõrgus);
                eelvaade.fitWidthProperty().bind(eelvaateLaius);

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