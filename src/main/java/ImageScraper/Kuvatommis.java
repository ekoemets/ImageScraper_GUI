package ImageScraper;

import org.openqa.selenium.WebElement;
import java.awt.image.BufferedImage;


public class Kuvatommis {
    private BufferedImage pilt;
    private String nimi;

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public Kuvatommis(WebElement element, BufferedImage pilt) {
        this.pilt = pilt;
        this.nimi = looNimi(element);
    }

    public BufferedImage getPilt() {
        return pilt;
    }

    /**
     * Loob WebElement klassi põhjal nime. Juhul kui sellel elemendil on src atribuut siis seatakse nimeks
     * selle src atribuudi viimasest märgist / kuni punktini jääv tekst. Vastasel juhul seatakse nimeks selle
     * elemendi tagname.
     * @param element WebElement tüüpi objekt, millest nime luuakse.
     * @return Loodud nimi.
     */
    private static String looNimi(WebElement element){
        String allikas = element.getAttribute("src");
        if (allikas == null || allikas.equals("")){
            return element.getTagName();
        }
        else if (allikas.contains("/")) {
            String nimi = allikas.substring(allikas.lastIndexOf("/") + 1);
            if (nimi.endsWith(".jpg") || nimi.endsWith(".png")){
                nimi = nimi.substring(0,nimi.lastIndexOf("."));
            }
            return nimi;
        }
        else {
            return allikas;
        }
    }
}
