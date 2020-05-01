package GUI;

import ImageScraper.VeebiKlient;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class InfoVaade extends BorderPane {
    private boolean tolkimineKeelatud = false;
    private final Button JARGMINE;
    private final Button EELMINE;
    private final Label tolkimiseValik;

    public InfoVaade(VeebiKlient klient, Stage aken){

        TextField veebileht = new TextField();
        veebileht.setPromptText("Kui kasutasid eelvaadet, ei ole vaja siia veebilehe aadressi uuesti kirjutada");
        HBox veebiRiba = new HBox(10, new Label("Veebileht:"), veebileht);
        veebiRiba.setAlignment(Pos.CENTER);
        HBox.setHgrow(veebileht,Priority.ALWAYS);

        TextField kaustaTee = new TextField(System.getProperty("user.home"));
        HBox.setHgrow(kaustaTee,Priority.ALWAYS);
        Button kaustaNupp = new Button("Vali kaust");
        kaustaNupp.setOnMouseClicked(event -> {
            DirectoryChooser dirc = new DirectoryChooser();
            File valitudkaust = dirc.showDialog(aken);
            if (valitudkaust != null){
                kaustaTee.setText(valitudkaust.getAbsolutePath());
            }
        });
        HBox kaustaRiba = new HBox(10, new Label("Kaustatee:"),kaustaTee,kaustaNupp);
        kaustaRiba.setAlignment(Pos.CENTER);
        VBox aadressid = new VBox(10,veebiRiba,kaustaRiba);
        this.setTop(aadressid);

        ChoiceBox<String> tyybivalik = new ChoiceBox<>();
        tyybivalik.getItems().addAll("class","tag","id","css (asjatundjatele)");

        ComboBox<String> elemendivalik = new ComboBox<>();
        elemendivalik.setEditable(true);
        elemendivalik.getItems().addAll("navbar","layer","container","img","a","section","contact","parallax","navigation");
        elemendivalik.setOnAction(event -> {
            if (elemendivalik.getValue().equals("img")){
                if (tolkimineKeelatud){
                    lülitaTolkimine();
                }
            } else if (!tolkimineKeelatud){
                lülitaTolkimine();
            }
        });
        VBox tyybiMenu = new VBox(10, new Label("Vali tüüp"),tyybivalik);
        VBox elemendiMenu = new VBox(10,new Label("Vali atribuut või sisesta ise"),elemendivalik);
        HBox valikuLahter = new HBox(10,tyybiMenu,elemendiMenu);
        valikuLahter.setPadding(new Insets(20,0,0,0));
        this.setCenter(valikuLahter);

        tolkimiseValik = new Label("Ei");
        HBox tolkimiseRiba = new HBox(10,new Label("Soovin tõlkida (eksperimentaalne, ainult \"img\" tüübil):"),tolkimiseValik);
        tolkimiseRiba.setPadding(new Insets(0,0,20,0));
        tolkimiseRiba.setAlignment(Pos.CENTER_LEFT);
        this.setBottom(tolkimiseRiba);

        // tõlkimise vahetamise nupud
        JARGMINE = new Button(">");
        JARGMINE.setOnMouseClicked(mouseEvent -> tolkimiseValik.setText("Jah"));
        EELMINE = new Button("<");
        EELMINE.setOnMouseClicked(mouseEvent -> tolkimiseValik.setText("Ei"));
        lülitaTolkimine();

        // tegevuse nupp, et salvestaks pildid
        Button valmisNupp = new Button("Valmis!");
        valmisNupp.setOnMouseClicked(event -> {
            if (veebileht.getText().isEmpty()){
                if (klient.getCurrentUrl() == null || klient.getCurrentUrl().equals("data:,")){
                    VeaAken.tooEsile("\nPuudub veebilehe aadress!");
                    return;
                }
                else veebileht.setText(klient.getCurrentUrl());
            }
            else klient.get(veebileht.getText());

            if (kaustaTee.getText().isEmpty()){
                VeaAken.tooEsile("\nPuudub kaustatee salvestamiseks!");
                return;
            }
            if (tyybivalik.getValue() == null){
                VeaAken.tooEsile("\nPalun vali tüüp!");
                return;
            }
            if (elemendivalik.getValue() == null || elemendivalik.getValue().isEmpty()){
                VeaAken.tooEsile("\nPalun vali element!");
                return;
            }
            SalvestamiseAken.tooEsile(klient,kaustaTee.getText(),tyybivalik.getValue(),elemendivalik.getValue(),!tolkimineKeelatud);
        });
        tolkimiseRiba.getChildren().addAll(EELMINE, JARGMINE,valmisNupp);

    }

    public void lülitaTolkimine(){
        tolkimineKeelatud = !tolkimineKeelatud;
        if (tolkimineKeelatud && tolkimiseValik.getText().equals("Jah")){
            tolkimiseValik.setText("Ei");
        }
        EELMINE.setDisable(tolkimineKeelatud);
        JARGMINE.setDisable(tolkimineKeelatud);
    }

}
