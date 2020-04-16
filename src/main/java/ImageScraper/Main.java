package ImageScraper;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.logging.Level;

import static java.io.File.separatorChar;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.setProperty("webdriver.chrome.silentOutput", "true");                     // peidab kasutaja jaoks ebaolulise info
        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);   // Ei tekita üleliigset logi
        WebDriverManager.chromedriver().setup();                                         //Tõmbab vajadusel alla chromedriveri kui seda ei eksisteeri
        VeebiKlient klient = new VeebiKlient();

        System.out.println( "%%%%%%%%%%%%%%%%%%%%%    Elemendi-Hankija    %%%%%%%%%%%%%%%%%%%%%%\n" +
                            "===================================================================\n" +
                            "Selle programmi eesmärk on veebilehtedelt elemenditõmmiste tegemine.\n" +
                            "Elemendi-Hankija võimaldab ise valida veebilehe ja salvestuskausta.\n" +
                            "Sisse on ka ehitatud piltide nimetamine eesti keelde läbi Google Translate.\n");

        int ringe = 0; // juhuks kui kasutaja soovib mitmelt lehelt elemente või samalt erinevaid
        String veebileht = "";
        String kohtSalvestamiseks = "";
        Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.print(   "Vali edasine tegevus:\n" +
                                "1. Veebilehelt elemenditõmmiste tegemine.\n" +
                                "2. Programmi sulgemine.\n" +
                                "Sinu otsus (number): ");
            int tegevuseValik = scan.nextInt();


            System.out.println("\n");
            if (tegevuseValik == 1){
                if (ringe == 0){
                    while (true) {
                        System.out.print(   "Valisid elemenditõmmiste tegemise, sisesta soovitava veebilehe aadress.\n" +
                                            "Link: ");

                        String leheSisend = scan.next();
                        if (leheSisend.equals("") || leheSisend.startsWith(" ")) {                          // kui kasutaja teeb sisestamisel vea, ss küsib uuesti sisendit
                            System.out.println("\nSisestasid tühja või vigase (algas tühikuga) aadressi, proovi uuesti.");
                        }
                        else{
                            veebileht = leheSisend; // muutuja veebileht on järgmise ringi jaoks, kui kasutaja soovib sama lehte
                            klient.get(veebileht);  // avab chromedriveriga sisestatud veebilehe
                            break;
                        }
                    }


                }
                else{
                    System.out.print(   "Valisid uuesti elemenditõmmiste tegemise, sisesta järgmise veebilehe aadress.\n" +
                                        "Kui soovid sama lehekülge, võid lihtsalt ENTER vajutada.\n" +
                                        "Link: ");

                    String leheSisend = scan.next();
                    if (!leheSisend.equals("")) { // kui on tühi sõne, siis kasutab eelmisest ringist defineeritud veebilehte
                        veebileht = leheSisend;
                    }
                    klient.get(veebileht);
                }


                System.out.println("\n");
                String koduKaust = System.getProperty("user.home");
                System.out.print(   "Sinu kodukaust on \"" + koduKaust + "\"\n" +
                                    "Pildid salvestatakse sinu kodukausta alamkausta.\n"  +
                                    "Kui alamkausta ei eksisteeri, üritab programm selle luua.\n" +
                                    "Kui ei soovi kausta muuta, vajuta lihtsalt ENTER.\n" +
                                    "Sisesta alamkausta tee: " +separatorChar);
                String kaustaTee = scan.next();
                if (!kaustaTee.equals("") || kohtSalvestamiseks.equals("")) {                               // kui sisestati tühi kaustatee, kasutab kaustateed eelmisest ringist
                    kohtSalvestamiseks = Files.createDirectories(Path.of(koduKaust, kaustaTee)).toString(); // loob kõik puuduolevad kaustad kaustatees
                }


                System.out.println("\n");
                System.out.print(   "Ettevalmistused on tehtud. Nüüd vali, kuidas soovid tõmmiseid teha:\n" +
                                    "1. Class atribuudi kaudu (näiteks \"navbar\", \"layer\" või \"container\") \n" +
                                    "2. Tag atribuudi kaudu (näiteks \"img\", \"a\" või \"section\")\n" +
                                    "3. Id atribuudi kaudu (näiteks \"contact\", \"parallax\" või \"navigation\")\n" +
                                    "4. Kasuta CSS selectorit (asjatundjatele)\n" +
                                    "Sinu valik (number): ");

                int atribuudiValik = scan.nextInt();
                System.out.print("\nSisesta ka atribuut(nt \"img\"): ");
                String atribuut = scan.next();

                switch (atribuudiValik){
                    case 1:
                        klient.teeKuvatõmmised(By.className(atribuut)); break;
                    case 2:
                        klient.teeKuvatõmmised(By.tagName(atribuut)); break;
                    case 3:
                        klient.teeKuvatõmmised(By.id(atribuut)); break;
                    case 4:
                        klient.teeKuvatõmmised(By.cssSelector(atribuut)); break;
                    default:
                        System.out.println("\n\nSisestasid midagi valesti, rakendub vaikimisi valik: piltide tegemine");
                        klient.teeKuvatõmmised(By.tagName("img"));
                        atribuut = "img";
                }

                if (atribuut.equals("img")) {
                    System.out.print( "\nRakendus pildisalvestamine, kas soovid pildinimed tõlkida?\n" +
                                        "(Tegu on eksperimentaalse funktsiooniga, sest põhineb ebatäpsel Google tõlkel)\n" +
                                        "Soovin tõlkida (\"jah\" / \"ei\"): ");
                    String tolkimine = scan.next();

                    if (tolkimine.equals("jah")) {
                        klient.get("https://translate.google.com/?hl=en#view=home&op=translate&sl=en&tl=et");              // avab brauseris Google tõlke
                        int pildiIndeks = 1;
                        String tõlge;
                        for (Kuvatommis pilt : klient.getKuvatõmmised()) {
                            klient.findElementByXPath("//textarea[@id='source']").clear();                          // puhastab tõlkimise sisendi kasti
                            klient.findElementByXPath("//textarea[@id='source']").sendKeys(pilt.getNimi().replaceAll("_", "-")); // sisestab pildi nime tõlkimiseks

                            Thread.sleep(300);
                            WebDriverWait oota = new WebDriverWait(klient, 3); // ootab kuni on tõlgitud või on möödunud 3 sekundit
                            oota.until(ExpectedConditions.visibilityOfElementLocated(new By.ByXPath("//span[@class='tlid-translation translation']/span")));

                            try {
                                tõlge = klient.findElementByXPath("//span[@class='tlid-translation translation']/span").getText(); // loeb tõlke väljundikastist
                            }
                            catch (NoSuchElementException e){
                                System.out.println("Ei suutnud seda nime tõlkida.");
                                tõlge = "pilt" + pildiIndeks; // paneb pildile üldise nime, et vältida ülesalvestamist
                                pildiIndeks++;
                            }
                            pilt.setNimi(tõlge); // nimetab pildi eestikeelseks, eeldusel et tõlkimine oli edukas
                        }
                    }
                }
                else{
                    int i = 1;
                    for (Kuvatommis pilt: klient.getKuvatõmmised()) {
                        pilt.setNimi(pilt.getNimi() + i);  // kuna tegu on suvalise elemenditüübiga, nummerdatakse tõmmised, et vältida ülesalvestamist
                        i++;
                    }
                }
                FailiHaldur.salvestaPildid(klient.getKuvatõmmised(), kohtSalvestamiseks); // salvestab kõik pildid kasutaja arvutisse


                System.out.println("\n");
                if (klient.getKuvatõmmised().size() > 0) {
                    System.out.println("Elemenditõmmised on edukalt salvestatud kausta \"" + kohtSalvestamiseks + "\"");
                }
                else
                    System.out.println("Sellise atribuudiga elemente ei leitud, ühtegi pilti ei salvestatud.");

                System.out.print(   "\nKas soovid salvestada kogu lehest pildi?\n" +
                                    "(\"jah\" / \"ei\"): ");
                String koguLeht = scan.next();
                if (koguLeht.equals("jah")){
                    klient.get(veebileht);
                    BufferedImage lehePilt = klient.teeLehePilt();  // teeb kogu lehest pildi
                    String leheNimi = veebileht.substring(veebileht.indexOf("/")+2, veebileht.indexOf(".")); // leiab veebilehe üldise nime
                    FailiHaldur.salvestaPilt(lehePilt, kohtSalvestamiseks, leheNimi);
                    System.out.println("\nLehetõmmise salvestamine edukas.\n");
                }

                ringe++; // läheb järgmisele ringile
            }
            else if (tegevuseValik == 2){
                klient.quit();                              // sulgeb brauseri
                System.out.println("\n");
                System.out.println( "Programm lõpetab töö.\n" +
                                    "Aitäh kasutamast.");
                break;
            }
            else
                System.out.println("Vigane sisend, proovi uuesti.");


        }
    }
}
