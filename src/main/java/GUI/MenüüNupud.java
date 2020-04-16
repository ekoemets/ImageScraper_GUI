package GUI;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class MenüüNupud extends BorderPane {
    public MenüüNupud() {
        Button seaded = new Button("Seaded");
        Button tagasi = new Button("Eelmine");
        Button edasi = new Button("Järgmine");
        HBox vaatenupud = new HBox(10, tagasi, edasi);
        this.setRight(vaatenupud);
        this.setLeft(seaded);
    }
}
