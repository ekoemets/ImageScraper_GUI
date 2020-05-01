package GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ErrorScreen {

    public static void tooEsile(String selgitus){
        Stage kast = new Stage();
        kast.initModality(Modality.APPLICATION_MODAL);
        kast.setTitle("Error!");
        kast.setResizable(false);

        Label selgitusSilt = new Label(selgitus);
        HBox selgitusKast = new HBox(selgitusSilt);
        selgitusKast.setAlignment(Pos.CENTER);
        selgitusSilt.setStyle("-fx-font-size: 16px; -fx-text-alignment: center;");
        Button sulgeNupp = new Button("OK!");
        sulgeNupp.setOnMouseClicked(mouseEvent -> kast.close());

        BorderPane sisu = new BorderPane();
        sisu.setTop(selgitusKast);
        sisu.setCenter(sulgeNupp);
        Scene stseen = new Scene(sisu, 380, 120);
        kast.setScene(stseen);
        kast.show();
    }
}
