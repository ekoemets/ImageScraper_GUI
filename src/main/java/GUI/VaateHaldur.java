package GUI;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class VaateHaldur {
    private BorderPane vaade;
    private Pane[] vaated;
    private int vaateIndeks;

    /**
     * @param vaade Vaade, kuhu pannakse hetkevaade.
     */
    public VaateHaldur(BorderPane vaade) {
        this.vaade = vaade;
        this.vaateIndeks = 0;
    }

    public void setVaated(Pane[] vaated) {
        this.vaated = vaated;
        muudaVaadet(vaateIndeks);
    }

    private void muudaVaadet(int indeks){
        vaade.setCenter(vaated[indeks]);
    }

    public void järgmine(){
        if(vaated.length > 0){
            int indeks = vaateIndeks < vaated.length - 1 ? vaateIndeks + 1: vaateIndeks;
            muudaVaadet(indeks);
        }
    }

    public void eelmine(){
        if(vaated.length > 0){
            int indeks = vaateIndeks > 0 ? vaateIndeks - 1: vaateIndeks;
            muudaVaadet(indeks);
        }
    }

}
