import java.io.IOException;
import java.util.Properties;

public class SattedTest {
    public static void main(String[] args) throws IOException {
        Properties eelistused = Satted.loeSätted("app.properties");
        System.out.println(eelistused.getProperty("ekraan.korgus"));
        System.out.println(eelistused.getProperty("ekraan.laius"));
        eelistused.setProperty("taustavarv", "Color.RED");
        Satted.salvestaSätted(eelistused, "app.properties");
    }

    private static void looSätted(){
        Properties sätted = new Properties();
        sätted.setProperty("ekraan.laius", "800");
        sätted.setProperty("ekraan.korgus", "800");
        try {
            Satted.salvestaSätted(sätted, "app.properties");
        } catch (IOException e) {
            System.out.println("Ei saanud sätteid faili salvestada");
            e.printStackTrace();
        }
    }
}
