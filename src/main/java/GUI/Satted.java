package GUI;

import java.io.*;
import java.net.URL;
import java.util.Properties;

public class Satted {
    public static Properties loeSätted(String nimi) throws IOException {
        File säteteFail = leiaSäteteFail(nimi);
        Properties sätted = new Properties();
        try {
            sätted.load(new FileInputStream(säteteFail));
        } catch (IOException e) {
            throw new IOException("Ei saanud sätete faili \"" + nimi + "\" lugeda.");
        }
        return sätted;
    }

    public static void salvestaSätted(Properties sätted, String nimi) throws IOException{
        File säteteFail = leiaSäteteFail(nimi);
        try{
            sätted.store(new FileOutputStream(säteteFail), "");
        } catch (IOException e) {
            throw new IOException("Faili ei saanud avada");
        }
    }

    private static File leiaSäteteFail(String nimi) throws FileNotFoundException{
        String failitee = leiaSäteteFailitee(nimi);
        File fail = new File(failitee);
        return fail;
    }

    public static String  leiaSäteteFailitee(String nimi) throws FileNotFoundException{
        URL failitee = ClassLoader.getSystemClassLoader().getResource(nimi);
        if(failitee == null){
            throw new FileNotFoundException("Ei leidnud sätete faili \"" +  nimi + '"');
        }
        return failitee.getPath();
    }
}
