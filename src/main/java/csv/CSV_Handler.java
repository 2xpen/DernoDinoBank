package csv;

import data.KontoauszugWrapper;
import data.anweisungen.UeberweisungsAnweisungParam;
import data.geschaeftsvorfall.KontoauszugZeile;
import data.nachricht.NachrichtView;
import data.pinnwand.PinnwandEntryView;
import menu.helper.CurrencyFormatter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class CSV_Handler implements ICSV_IMPORT_EXPORT {

    public ArrayList<UeberweisungsAnweisungParam> importMassenueberweisung(Path path) throws CSVException {
        ArrayList<UeberweisungsAnweisungParam> rueckgabe = new ArrayList<>();
        Scanner scan;

        boolean hasSyntaxError = false;
        List<Integer> invalidRows = new ArrayList<>();

        try {
            scan = new Scanner(path);
        } catch (IOException e) {
            throw new CSVException(CSVException.Message.FileNotFound);
        }

        if (!scan.hasNext()) {
            throw new CSVException(CSVException.Message.FileIstEmpty);
        }

        scan.nextLine();

        int zeilenIndex = 1;

        while (scan.hasNext()) {
            List<String> tokens = List.of(scan.nextLine().split(";"));

            if (tokens.size() != 3) {
                hasSyntaxError = true;
                invalidRows.add(zeilenIndex);
                zeilenIndex++;
                continue;
            }

            try {
                rueckgabe.add(
                        new UeberweisungsAnweisungParam(
                                tokens.get(0)
                                , tokens.get(2)
                                , Double.parseDouble(tokens.get(1)))
                );

            } catch (NumberFormatException e) {
                hasSyntaxError = true;
                invalidRows.add(zeilenIndex);
                zeilenIndex++;
                continue;
            }

            zeilenIndex++;
        }

        scan.close();

        if (hasSyntaxError) {
            throw new CSVException(CSVException.Message.CSVFormat.addZeilen(invalidRows));
        }

        return rueckgabe;
    }


    public void exportPinnwandnachrichten(List<PinnwandEntryView> pinnwandEntryViews, Path path) throws CSVException {
        String content = "Zeitpunkt der Nachricht;Sender;Empfänger;Nachricht" + "\n";

        if (pinnwandEntryViews.isEmpty()) {
            throw new CSVException(CSVException.Message.NichtsZumExportieren);
        }

        for (PinnwandEntryView entry : pinnwandEntryViews) {
            content += entry.getFormattedTimestamp() + ";";
            content += entry.getAutorName() + ";";
            content += entry.getEmpfaengerName() + ";";
            content += entry.getNachricht() + ";" + "\n";
        }
        write(path, content, ExportTypes.PINNWANDEINTRAEGE);
    }

    public void exportKontoAuszuege(KontoauszugWrapper kontoauszugWrapper, Path path) throws CSVException {

        if (kontoauszugWrapper.getKontauszugZeile().isEmpty()) {
            throw new CSVException(CSVException.Message.NichtsZumExportieren);
        }

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String content = "Transaktionsdatum; Empfänger; Sender; Beschreibung; Betrag" + "\n";
        for (KontoauszugZeile gz : kontoauszugWrapper.getKontauszugZeile()) {
            String formattedDate = formatDate(gz.getDatum(), dateFormatter);

            content += formattedDate + ";";
            content += gz.getEmpfaenger() + ";";
            content += gz.getSender() + ";";
            content += gz.getBeschreibung() + ";";
            content += CurrencyFormatter.formatCurrencyForCSV(gz.getBetrag()) + "\n";
        }
        write(path, content, ExportTypes.KONTOAUSZUG);
    }

    private String formatDate(Date date, SimpleDateFormat formatter) {
        if (date == null) {
            return "";
        }
        return formatter.format(date);
    }

    public void exportNachrichten(List<NachrichtView> nachrichtViews, Path path) throws CSVException {
        if (nachrichtViews.isEmpty()) {
            throw new CSVException(CSVException.Message.NichtsZumExportieren);
        }

        String content = "Zeitpunkt der Nachricht;Sender;Empfänger;Nachricht" + "\n";
        for (NachrichtView gz : nachrichtViews) {
            content += gz.getDate() + ";";
            content += gz.getSender() + ";";
            content += gz.getEmpfaenger() + ";";
            content += gz.getMessage() + ";" + "\n";
        }
        write(path, content, ExportTypes.NACHRICHTEN);
    }

    public void write(Path zielPfad, String content, ExportTypes type) throws CSVException {

        LocalDate aktuellesDatum = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formatiertesDatum = aktuellesDatum.format(formatter);

        try {
            FileWriter writer = new FileWriter(zielPfad + "\\" + type.getName() + formatiertesDatum + ".csv", true);
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            throw new CSVException(CSVException.Message.WriteFailed.addInfo(e.getMessage()));
        }
    }
}