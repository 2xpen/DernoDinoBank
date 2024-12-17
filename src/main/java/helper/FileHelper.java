package helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.logging.FileHandler;

public class FileHelper {

    public static boolean isPathAccessible(Path path) throws FileNotFoundException {
            File file = new File(path.toString());

            // Prüfen, ob der Pfad existiert
            if (!file.exists()) {
                throw new FileNotFoundException("Pfad nicht gefunden: " + path);
            }

            // Prüfen, ob der Pfad lesbar ist
            if (!file.canRead()) {
                throw new FileNotFoundException("Pfad konnte nicht gelesen werden: " + path);
            }

            // Prüfen, ob der Pfad beschreibbar ist
            if (!file.canWrite()) {
                throw new FileNotFoundException("Pfad ist nicht beschreibbar: " + path);
            }

            return true;
        }

}