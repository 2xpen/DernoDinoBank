package helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHelper {
    public static boolean isPathAccessible(Path path) throws FileNotFoundException {
            File file = new File(path.toString());

            try {
                Paths.get(file.toURI());
            }catch (InvalidPathException e){
                throw new FileNotFoundException("Pfad invalide: " + file.getAbsolutePath());
            }

            if(file.isDirectory()) {
                return true;
            }

            if (!file.exists()) {
                throw new FileNotFoundException("Pfad nicht gefunden: " + path);
            }

            if (!file.canRead()) {
                throw new FileNotFoundException("Pfad konnte nicht gelesen werden: " + path);
            }

            if (!file.canWrite()) {
                throw new FileNotFoundException("Pfad ist nicht beschreibbar: " + path);
            }
            return true;
        }
}