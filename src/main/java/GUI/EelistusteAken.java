package GUI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;

public class EelistusteAken {

    public static void tooEsile() throws IOException {
        Properties seaded = Sätted.loeSätted("app.properties");

        Stage eelistused = new Stage();
        eelistused.initModality(Modality.APPLICATION_MODAL);
        eelistused.setTitle("Eelistused");
        eelistused.setResizable(false);

        GridPane asetus = new GridPane();
        asetus.setPadding(new Insets(10));
        asetus.setHgap(10);
        asetus.setVgap(10);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(60);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(40);
        asetus.getColumnConstraints().addAll(col1,col2);


        // aknalaiusele viitav silt
        Label aknaLaiuseSilt = new Label("Vaikimisi laius:");
        asetus.add(aknaLaiuseSilt,0,0);

        // aknalaiuse tekstiväli
        TextField laiusTekst = new TextField(seaded.getProperty("aken.laius"));
        asetus.add(laiusTekst,1,0);

        // aknakõrgusele viitav silt
        Label aknaKorguseSilt = new Label("Vaikimisi kõrgus:");
        asetus.add(aknaKorguseSilt,0,1);

        // aknakõrguse tekstiväli
        TextField korgusTekst = new TextField(seaded.getProperty("aken.korgus"));
        asetus.add(korgusTekst,1,1);

        // stiilile viitav silt
        Label stiiliSilt = new Label("Stiil:");
        asetus.add(stiiliSilt, 0,2);

        // stiili valiku menüü
        String stiil = seaded.getProperty("stiil");
        Label stiiliValik = new Label(stiil.substring(stiil.lastIndexOf('/') + 1, stiil.lastIndexOf('.')));
        asetus.add(stiiliValik,1,2);

        // stiilivaliku navigeerimise nupud
        Button jargmine = new Button(">");
        Button eelmine = new Button("<");

        //stiiliga seotud väli
        HBox stiiliRida = new HBox(10,eelmine,jargmine);
        asetus.add(stiiliRida,1,3);

        // stiilivaliku muutumise kuulaja
        jargmine.setOnMouseClicked(mouseEvent -> vahetaStiili(stiiliValik));
        eelmine.setOnMouseClicked(mouseEvent -> vahetaStiili(stiiliValik));

        // eelistuste salvestamise nupp
        Button salvestaNupp = new Button("Salvesta");
        asetus.add(salvestaNupp,1,6);
        salvestaNupp.setOnMouseClicked(mouseEvent -> {
            seaded.setProperty("aken.laius",laiusTekst.getText());
            seaded.setProperty("aken.korgus",korgusTekst.getText());
            seaded.setProperty("stiil", "styles/" + stiiliValik.getText() + ".css");
            try {
                Sätted.salvestaSätted(seaded, "app.properties");
            } catch (IOException | NullPointerException e){
                VeaAken.tooEsile("Eelistuste salvestamisel läks midagi valesti.\n" +
                        "Tõenäoliselt tekkis viga app.properties faili leidmisel.");
            }
        });

        // eelistuste akna sulgemise nupp
        Button sulgeNupp = new Button("OK");
        sulgeNupp.setOnMouseClicked(mouseEvent -> eelistused.close());
        asetus.add(sulgeNupp, 0,6);


        Scene stseen = new Scene(asetus,300,250);

        eelistused.setScene(stseen);
        eelistused.show();
    }

    private static void vahetaStiili(Label stiiliValik){
        Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
        if (stiiliValik.getText().equals("hele")){
            stiiliValik.setText("tume");
            Application.setUserAgentStylesheet("styles/tume.css");
        } else {
            stiiliValik.setText("hele");
            Application.setUserAgentStylesheet("styles/hele.css");

        }
    }
}
