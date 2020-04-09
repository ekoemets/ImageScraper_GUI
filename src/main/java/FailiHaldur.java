import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class FailiHaldur {

    /**
     * Salvestab kõik Veebikliendi poolt tehtud tõmmised kasutaja arvutisse ettenähtud kausta.
     * @param pildid Kõik tehtud tõmmised.
     * @param kaustaTee koht salvestamiseks, eelnevalt loodud, kindlasti olemas.
     * @throws IOException kui elemenditõmmis salvestati ilma pildi informatsioonita, väljastab kirja ja jätab pildi vahele.
     */
    static void salvestaPildid(List<Kuvatommis> pildid, String kaustaTee) throws IOException {
        for (Kuvatommis kuvatommis : pildid) {
            File väljundFail = new File(kaustaTee + "\\" +kuvatommis.getNimi() + ".png");
            try {
                ImageIO.write(kuvatommis.getPilt(), "png", väljundFail);
            }
            catch (IllegalArgumentException e){
                System.out.println("Pilti ei eksisteeri, jätan selle vahele.");
            }
        }
    }

    /**
     * Salvestab üheainsa pildi kasutaja arvutisse ettenähtud kausta.
     * @param pilt mida salvestatakse.
     * @param kaustaTee kuhu salvestatakse, eelnevalt loodud, kindlasti olemas.
     * @param nimi mis omistatakse pildile.
     * @throws IOException kui üritatakse salvestada pilti, mille kohta pole tõmmist.
     */
    static void salvestaPilt(BufferedImage pilt, String kaustaTee, String nimi) throws IOException {
        File väljundFail = new File(kaustaTee + "\\" + nimi + ".png");
        ImageIO.write(pilt, "png", väljundFail);
    }
}
