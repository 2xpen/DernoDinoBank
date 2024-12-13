package csv;

import data.anweisungen.UeberweisungsAnweisungParam;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CSVHandlerTest {

    private static final String TEMP_FILE = "C:\\Projekte\\Banksocialmedia\\src\\test\\testdaten\\csvHandler\\test_massenueberweisung.csv";
    private CSV_Handler csvHandler;

    @BeforeEach
    void setUp() {
        csvHandler = new CSV_Handler();
    }

    @AfterEach
    void tearDown() {
        File file = new File(TEMP_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    ///  POSITIV TEST
    @Test
    void testMassenueberweisung_validFile() throws Exception {
        // Erstelle eine temporäre CSV-Datei mit gültigen Daten
        createTestFile("""
                Max Mustermann;123.45;Rechnung Januar
                Erika Musterfrau;678.90;Rechnung Februar
                """);

        ArrayList<UeberweisungsAnweisungParam> result = csvHandler.massenueberweisung(TEMP_FILE);

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
        // Erstelle eine CSV-Datei mit falschem Format
        createTestFile("Max Mustermann;123.45"); // Fehlende Beschreibung

        CSVException exception = assertThrows(CSVException.class,
                () -> csvHandler.massenueberweisung(TEMP_FILE));

        assertTrue(exception.getMessage().contains("Das format der Datei ist falsch"),
                "Die Fehlermeldung sollte auf ein falsches Format hinweisen");
    }

    @Test
    void testMassenueberweisung_invalidNumber() {
        // Erstelle eine CSV-Datei mit ungültiger Zahl
        createTestFile("Max Mustermann;ABC;Rechnung Januar");

        CSVException exception = assertThrows(CSVException.class,
                () -> csvHandler.massenueberweisung(TEMP_FILE));

        assertTrue(exception.getMessage().contains("Der Betrag ist keine gültige Zahl"),
                "Die Fehlermeldung sollte auf eine ungültige Zahl hinweisen");
    }

    @Test
    void testMassenueberweisung_fileNotFound() {
        CSVException exception = assertThrows(CSVException.class,
                () -> csvHandler.massenueberweisung("nicht_existierende_datei.csv"));

        assertTrue(exception.getMessage().contains("Datei konnte nicht gefunden werden"),
                "Die Fehlermeldung sollte auf eine nicht gefundene Datei hinweisen");
    }


    private void createTestFile(String content) {
        try (FileWriter writer = new FileWriter(TEMP_FILE)) {
            writer.write(content);
        } catch (IOException e) {
            fail("Fehler beim Erstellen der Testdatei: " + e.getMessage());
        }
    }
}
