package csv;

import data.KontoauszugWrapper;
import data.geschaeftsvorfall.GevoArt;
import data.geschaeftsvorfall.KontoauszugZeile;
import data.user.UserName;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class KontoAuszugExportTest {


    private String testDatei = "C:\\Projekte\\DernoDinoBank\\src\\test\\testdaten\\csvHandler\\%s";
    private CSV_Handler csvHandler;

    @BeforeEach
    void setUp() {
        csvHandler = new CSV_Handler();
        LocalDate aktuellesDatum = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formatiertesDatum = aktuellesDatum.format(formatter);

        testDatei = testDatei.formatted(ExportTypes.KONTOAUSZUG.getName() + formatiertesDatum+ ".csv");

    }

    @AfterEach
    void tearDown() {
        File file = new File(testDatei);
        if (file.exists()) {
            file.delete();
        }
    }



    @Test
    void testCSVExport_POSITIV() throws CSVException, IOException {


        KontoauszugWrapper kontoauszugWrapper = new KontoauszugWrapper(

                List.of(
                        new KontoauszugZeile(
                                Timestamp.valueOf("2024-12-10 23:11:32.0")
                                , Optional.of(new UserName("l.sindermann@hsw-stud.de"))
                                , new UserName("r.heim@hsw-stud.de")
                                , "xoxoxox"
                                , 69
                                , GevoArt.UEBERWEISUNG)

                        , new KontoauszugZeile(
                                Timestamp.valueOf("2024-12-10 23:11:32.0")
                                , Optional.of(new UserName("tom.ramos@hsw-stud.de"))
                                , new UserName("m.farwick@hsw-stud.de")
                                , "ey kolleg"
                                , 66
                                , GevoArt.UEBERWEISUNG)

                        , new KontoauszugZeile(
                                Timestamp.valueOf("2024-12-10 23:11:32.0")
                                , Optional.of(new UserName("r.heim@hsw-stud.de"))
                                , new UserName("tom.ramos@hsw-stud.de")
                                , "von tom zu becci"
                                , 66
                                , GevoArt.UEBERWEISUNG)

                        , new KontoauszugZeile(
                                Timestamp.valueOf("2024-12-10 23:11:32.0")
                                , Optional.of(new UserName("tom.ramos@hsw-stud.de"))
                                , new UserName("s@s.t")
                                , "poliwoli "
                                , 66
                                , GevoArt.UEBERWEISUNG)
                )
        );

        csvHandler.exportKontoAuszuege(kontoauszugWrapper, Path.of("C:\\Projekte\\DernoDinoBank\\src\\test\\testdaten\\csvHandler"));

        try {
            Scanner scanner = new Scanner(new File(testDatei));
            List<String> zeilen = new ArrayList<>();


            while (scanner.hasNextLine()) {
                zeilen.add(scanner.nextLine());
            }

            //dreckig drauf getestet ob die anzahl der eintraege stimmt, liste ist eins größer als das reingegebene wegen dem Header
/*            assert eintrag.size() == liste.size();*/

            Assertions.assertEquals(
                    "Transaktionsdatum; Empfänger; Sender; Beschreibung; Betrag"
                    ,zeilen.get(0));

            Assertions.assertEquals(
                    "2024-12-10;l.sindermann@hsw-stud.de;r.heim@hsw-stud.de;xoxoxox;69.00"
                    ,zeilen.get(1));


            Assertions.assertEquals(
                    "2024-12-10;tom.ramos@hsw-stud.de;m.farwick@hsw-stud.de;ey kolleg;66.00"
                    ,zeilen.get(2));

        }catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }



    }


    @Test
    void testCSVExport_NEGATIV() throws CSVException {
        //kann eigentlich nur failen wenn dann in zukunft man noch den pfad angeben kann in dem das gespeichert werden soll nicht gibt
    }


}
