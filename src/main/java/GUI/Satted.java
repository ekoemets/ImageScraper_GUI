package GUI;

import java.io.*;
import java.nio.file.Files;
import java.util.Properties;

public class Satted {
    private static final String lokaalneFailiTee = ".imagescraper";

    public static Properties loeSätted(String nimi) throws IOException {
        File säteteFail = leiaSäteteFail(nimi);
        Properties sätted = new Properties();
        try (FileInputStream sättevoog = new FileInputStream(säteteFail)){
            sätted.load(sättevoog);
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

    private static File leiaSäteteFail(String nimi) throws IOException {
        File failitee = new File(lokaalneFailiTee);
        if(!failitee.exists()){
            failitee.mkdir();
        }
        File fail = new File(lokaalneFailiTee + File.separator + nimi);
        if(!fail.exists()){
            Files.copy(ClassLoader.getSystemClassLoader().getResourceAsStream(nimi), fail.toPath());
        }
        return fail;
    }

}
