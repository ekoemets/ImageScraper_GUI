package GUI;

import ImageScraper.VeebiKlient;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class InfoVaade extends BorderPane {
    private String aadress;
    private TextField salvestusKaust;
    private ComboBox<String> midaSalvestada;
    private boolean tolkimineKeelatud = true;

    public InfoVaade(VeebiKlient klient){
        TextField veebileht = new TextField();
        HBox veebiRiba = new HBox(10, new Label("Veebileht:"),veebileht);
        TextField kaustaTee = new TextField();
        HBox kaustaRiba = new HBox(10, new Label("Kaustatee:"),kaustaTee);
        VBox aadressid = new VBox(veebiRiba,kaustaRiba);
        this.setTop(aadressid);

        ChoiceBox<String> tyybivalik = new ChoiceBox<>();
        ComboBox<String> elemendivalik = new ComboBox<>();
        HBox valikuMenyyd = new HBox(tyybivalik,elemendivalik);
        this.setCenter(valikuMenyyd);
    }

    public void setAadress(String aadress){
        this.aadress = aadress;
    }

}
