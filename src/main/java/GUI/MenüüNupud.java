package GUI;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class MenüüNupud extends BorderPane {
    private final Button seaded;
    private final Button tagasi;
    private final Button edasi;
    private boolean keelatud;

    public MenüüNupud(VaateHaldur vaateHaldur) {
        this.keelatud = false;
        seaded = new Button("Seaded");
        seaded.setOnMouseClicked(mouseEvent -> {
            try {
                EelistusteAken.tooEsile();
            } catch (IOException e){
                VeaAken.tooEsile("Midagi läks valesti :(");
            }
        });
        tagasi = new Button("Eelmine");
        tagasi.setOnMouseClicked(mouseEvent -> vaateHaldur.eelmine());
        edasi = new Button("Järgmine");
        edasi.setOnMouseClicked(mouseEvent -> vaateHaldur.järgmine());
        HBox vaatenupud = new HBox(10, tagasi, edasi);
        this.setRight(vaatenupud);
        this.setLeft(seaded);
    }

    public void lülita(){
        keelatud = !keelatud;
        seaded.setDisable(keelatud);
        tagasi.setDisable(keelatud);
        edasi.setDisable(keelatud);
    }


}
