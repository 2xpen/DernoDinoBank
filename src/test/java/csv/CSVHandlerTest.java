package csv;

import data.anweisungen.UeberweisungsAnweisungParam;
import org.junit.jupiter.api.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CSVHandlerTest {
    private static final String TEMP_FILE = "C:\\Projekte\\DernoDinoBank\\src\\test\\testdaten\\testMassenUeberweisung.csv";
    private CSV_Handler csvHandler;

    @BeforeEach
    void setUp() {
        csvHandler = new CSV_Handler();
    }

    @Test
    void testMassenueberweisung_validFile() throws Exception {
        createTestFile("""
                Empfänger;Betrag;Beschreibung
                Max Mustermann;123.45;Rechnung Januar
                Erika Musterfrau;678.90;Rechnung Februar
                """);

        ArrayList<UeberweisungsAnweisungParam> result = csvHandler.importMassenueberweisung(Path.of(TEMP_FILE));

        assertEquals(2, result.size(), "Es sollten 2 Überweisungen eingelesen werden");

        UeberweisungsAnweisungParam ersteUeberweisung = result.get(0);
        assertEquals("Max Mustermann", ersteUeberweisung.getEmpfeanger());
        assertEquals(123.45, ersteUeberweisung.getBetrag());
        assertEquals("Rechnung Januar", ersteUeberweisung.getBeschreibung());

        UeberweisungsAnweisungParam zweiteUeberweisung = result.get(1);
        assertEquals("Erika Musterfrau", zweiteUeberweisung.getEmpfeanger());
        assertEquals(678.90, zweiteUeberweisung.getBetrag());
        assertEquals("Rechnung Februar", zweiteUeberweisung.getBeschreibung());
    }


    @Test
    void testMassenueberweisung_invalidFormat() {
        createTestFile("Empfänger;Betrag;Beschreibung"
                +"\nMax Mustermann;123.45");

                assertThrows(CSVException.class,
                () -> csvHandler.importMassenueberweisung(Path.of(TEMP_FILE)));
    }

    @Test
    void testMassenueberweisung_invalidNumber() {
        createTestFile("Empfänger;Betrag;Beschreibung"+
                "\nMax Mustermann;ABC;Rechnung Januar");

                assertThrows(CSVException.class,
                () -> csvHandler.importMassenueberweisung(Path.of(TEMP_FILE)));
    }

    @Test
    void testMassenueberweisung_fileNotFound() {
         assertThrows(CSVException.class,
                () -> csvHandler.importMassenueberweisung(Path.of("nicht_existierende_datei.csv")));
    }


    private void createTestFile(String content) {
        try (FileWriter writer = new FileWriter(TEMP_FILE)) {
            writer.write(content);
        } catch (IOException e) {
            fail("Fehler beim Erstellen der Testdatei: " + e.getMessage());
        }
    }
}
