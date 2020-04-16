package GUI;

import ImageScraper.VeebiKlient;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class VeebileheVaade extends VBox {
    private VeebiKlient klient;

    public VeebileheVaade(VeebiKlient klient, ReadOnlyDoubleProperty kõrgus, ReadOnlyDoubleProperty laius){
        this.klient = klient;
        Label veebilehtTekst = new Label("Veebileht");
        TextField veebileheAadress = new TextField("");
        HBox.setHgrow(veebileheAadress, Priority.ALWAYS);
        Button otsiNupp = new Button("Otsi");
        ImageView eelvaateVaade = new ImageView();
        eelvaateVaade.fitWidthProperty().bind(laius.multiply(0.9));
        eelvaateVaade.fitHeightProperty().bind(kõrgus.multiply(0.9));
        otsiNupp.setOnMouseClicked(mouseEvent -> {
                new Thread(() ->
                {
                    klient.get(veebileheAadress.getText());
                    Image eelvaade = null;
                    try {
                        eelvaade = SwingFXUtils.toFXImage(klient.teeLehePilt(), null);
                    } catch (IOException e) {
                        //TODO create meaningful error
                        e.printStackTrace();
                    }
                    eelvaateVaade.setImage(eelvaade);
                }).start();

        });

        HBox sisend = new HBox(15, veebilehtTekst, veebileheAadress, otsiNupp);
        sisend.setAlignment(Pos.CENTER);


        this.getChildren().addAll(sisend, eelvaateVaade);
        this.setAlignment(Pos.CENTER);
        this.spacingProperty().set(20);
        System.out.println(this.getHeight());
    }

}
