import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class VeebiKlient extends ChromeDriver {
    private List<Kuvatommis> kuvatõmmised; // hoiustab kõiki kuvatõmmiseid mis on tehtud meetodiga teeElemendiPildid

    public VeebiKlient() {
        this(new ChromeOptions().addArguments("--headless", "--window-size=1920,1080")); //Peidame akna ja seame standardse suuruse.
    }

    /**
     * Loob veebikliendi (brauseri) läbi ChromeDriveri liidese, mis võimaldab internetis lehtede avamist.
     * Tekitatud logi kirjad on vaigistatud.
     * @param parameetrid saab täpsustada aknasuurust ja muid brauseri attribuute.
     */
    public VeebiKlient(ChromeOptions parameetrid) {
        super(parameetrid);
        this.kuvatõmmised = new ArrayList<>();
    }


    public List<Kuvatommis> getKuvatõmmised() {
        return kuvatõmmised;
    }

    /**
     * Avab veebilehitsejas etteantud sõnega veebilehe.
     * @param url täpsustab mis veebileht avatakse.
     */
    @Override
    public void get(String url) {
        super.get(teisendaVeebileht(url));
    }

    /**
     * Juhul kui veebilehe alguses ei ole täpsustatud Http või https protokoll siis lisab selle sinna.
     * @param veebileht veebileht, mille algust kontrollitakse.
     * @return tagastab koos protokolliga veebilehe aadressi.
     */
    private String teisendaVeebileht(String veebileht){
        if(!veebileht.matches("https*://(.*)")){
            return "http://" + veebileht;
        }
        return veebileht;

    }

    /**
     * Teeb kogu ekraanil olevast veebilehest ekraanitõmmise.
     * @return tagastab ekraanitõmmise BufferedImage tüübina, et seda saaks salvestada ja sellest saaks alamtõmmiseid teha.
     * @throws IOException kui ImageIO.read ei suuda pildiks teisendada, siis annab teada.
     */
    private BufferedImage teeAknaPilt() throws IOException{
        File aknaKuvatõmmis = ((TakesScreenshot) this).getScreenshotAs(OutputType.FILE);  // teeb pildi kogu ekraanist
        BufferedImage aknaPilt = ImageIO.read(aknaKuvatõmmis);
        return aknaPilt;
    }


    /**
     * Teeb kuvatõmmise kogu veebilehest, muutes veebilehitseja suurust nii, et kogu leht mahuks "ekraanile".
     * @return tagastab BufferedImage, et seda saaks salvestada.
     * @throws IOException kui teeAknaPilt ei suuda pildiks teisendada, viskab erindi.
     */
    public BufferedImage teeLehePilt() throws IOException {
        JavascriptExecutor js = this;

        int leheKõrgus = ((Number) js.executeScript("" +
                "var body = document.body,\n" +
                "html = document.documentElement;\n" +
                "return Math.max( body.scrollHeight, body.offsetHeight," +
                "                 html.clientHeight, html.scrollHeight, html.offsetHeight );")).intValue();
        Dimension aknaSuurus = this.manage().window().getSize();
        if(aknaSuurus.getHeight() < leheKõrgus){
            this.manage().window().setSize(new Dimension(aknaSuurus.getWidth(), leheKõrgus));

        }
        BufferedImage terveLeht = teeAknaPilt();
        this.manage().window().setSize(aknaSuurus);  // aknatõmmis on tehtud, nüüd seab brauseriakna suuruse tagasi tavaliseks
        return terveLeht;
    }

    /**
     * Teeb etteantud elemenditunnuse kaudu igast leitud elemendist, mis jääb ekraanile, pildi.
     * @param elemendiTunnus mis elemendi järgi pilte teha.
     * @throws IOException kui laePildidAlla või teeElemendiPildid ei suuda pilti luua.
     */
    public void teeKuvatõmmised(By elemendiTunnus) throws IOException {
        //Tühjendame kuvatõmmiste listi
        kuvatõmmised.clear();
        if(elemendiTunnus.equals(By.tagName("img"))){ // kui elementideks on pildid tõmbab otse "img src" kaudu alla
            laePildidAlla(elemendiTunnus);
        }else{
            teeElemendiPildid(elemendiTunnus);
        }
    }

    /**
     * Teeb etteantud elemenditunnuse kaudu igast leitud elemendist pildi kuvatõmmise alampildina ja salvestab need Veebikliendi isendivälja.
     * @param elemendiTunnus mis elemendist pilte teha.
     * @throws IOException kui teeLehePilt ei suuda pilti luua
     */
    private void teeElemendiPildid(By elemendiTunnus) throws IOException {
        BufferedImage leheKuvatõmmis = teeLehePilt(); //Teeme tervest lehest kuvatõmmise
        int leheKõrgus = leheKuvatõmmis.getHeight();
        int leheLaius = leheKuvatõmmis.getWidth();

        List<WebElement> elemendid = this.findElements(elemendiTunnus); //Teeme igast elemendist kuvatõmmise
        for (WebElement element: elemendid) {
            Point asukoht = element.getLocation();
            int kõrgus = element.getSize().getHeight();
            int laius = element.getSize().getWidth();

            if(asukoht.getX() > 0 && asukoht.getY() > 0 &&
                    asukoht.getX() + laius < leheLaius && asukoht.getY() + kõrgus < leheKõrgus){ // kontroll et alampilt ei asuks ekraanist väljas
                BufferedImage elemendiKuvaTõmmis = leheKuvatõmmis.getSubimage(asukoht.getX(), asukoht.getY(), laius, kõrgus);
                this.kuvatõmmised.add(new Kuvatommis(element, elemendiKuvaTõmmis));
            }
        }
    }

    /**
     * Kui elementideks on pildid, siis leiab pildid otse allikast ja lisab Veebikliendi isendivälja.
     * @param elemendiTunnus mõeldud ainult By.tagname("img") kaudu kasutamiseks.
     * @throws IOException kui pildi loomine ei õnnestu.
     */
    private void laePildidAlla(By elemendiTunnus) throws IOException {
        List<WebElement> elemendid = this.findElements(elemendiTunnus);
        for(WebElement element: elemendid){
            String pildiSrc = element.getAttribute("src"); // leiab pildi allika
            if (pildiSrc == null || pildiSrc.contains("data:image")){ // kontrollib et pilt poleks base64 kodeeringus, kui on siis jätab vahele
                continue;
            }
            if (pildiSrc.startsWith("src\"") && pildiSrc.endsWith("\"")){
                pildiSrc = pildiSrc.substring(4,pildiSrc.length()-1);
            }
            URL pildiAadress = new URL(pildiSrc);
            BufferedImage pilt = ImageIO.read(pildiAadress);
            this.kuvatõmmised.add(new Kuvatommis(element, pilt));
        }
    }
}
