package GUI;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Salvestamine {

    public static void tooEsile(){
        Stage taust = new Stage();
        taust.setTitle("Salvestab pilte");
        taust.initModality(Modality.APPLICATION_MODAL);
        taust.setResizable(false);

        Label selgitusSilt = new Label("Programm otsib internetist sinu valitud elemente.\n" +
                "Palun oota (:\n" +
                "Kui valisid piltide tõlkimise siis võib minna kuni minut.");
        HBox selgitusKast = new HBox(selgitusSilt);
        selgitusKast.setAlignment(Pos.CENTER);
        selgitusSilt.setStyle("-fx-font-size: 16px; -fx-text-alignment: center;");
    }
}
