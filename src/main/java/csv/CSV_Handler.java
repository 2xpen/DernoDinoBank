package csv;

import data.KontoauszugWrapper;
import data.anweisungen.UeberweisungsAnweisungParam;
import data.geschaeftsvorfall.KontoauszugZeile;
import data.nachricht.NachrichtView;
import data.pinnwand.PinnwandEntryView;

import java.io.*;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static jdk.jfr.internal.util.ValueFormatter.formatTimestamp;

public class CSV_Handler implements ICSV_IMPORT_EXPORT {

    public ArrayList<UeberweisungsAnweisungParam> importMassenueberweisung(Path path) throws CSVException {
        ArrayList<UeberweisungsAnweisungParam> rueckgabe = new ArrayList<>();
        Scanner scan;

        try {
            scan = new Scanner(path);
        } catch (IOException e) {
            throw new CSVException(CSVException.Message.FileNotFound);
        }

        if (!scan.hasNext()) {
            throw new CSVException(CSVException.Message.FileIstEmpty);
        }

        //skip header


        scan.nextLine();
        int zeilenIndex = 1;
        while (scan.hasNext()) {

            List<String> tokens = List.of(scan.nextLine().split(";"));

            if (tokens.size() != 3) {
                throw new CSVException(CSVException.Message.CSVFormat.addZeile(zeilenIndex));
            }
            try {
                rueckgabe.add(
                        new UeberweisungsAnweisungParam(
                                tokens.get(0)
                                , tokens.get(2)
                                , Double.parseDouble(tokens.get(1)))
                );

            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
                throw new CSVException(CSVException.Message.NumberFormat.addZeile(zeilenIndex).addInfo(tokens.get(2)));
            }
            zeilenIndex++;
        }
        scan.close();

        if(rueckgabe.isEmpty()) {
            throw new CSVException(CSVException.Message.FileIstEmpty);
        }

        return rueckgabe;
    }



    public void exportPinnwandnachrichten(List<PinnwandEntryView> pinnwandEntryViews, Path path) throws CSVException {
        String content = "Datum;Sender;Empfänger;Nachricht+" + "\n";

        if(pinnwandEntryViews.isEmpty()) {
            throw new CSVException(CSVException.Message.NichtsZumExportieren);
        }

        for (PinnwandEntryView entry : pinnwandEntryViews) {
            content += entry.getTimestamp() + ";";
            content += entry.getEmpfaengerName() + ";";
            content += entry.getAutorName() + ";";
            content += entry.getNachricht() + ";" + "\n";

        }

        write(path,content, ExportTypes.NACHRICHTEN.addInfo(pinnwandEntryViews.getFirst().getEmpfaengerName().toString()));

    }

    public void exportKontoAuszuege(KontoauszugWrapper kontowrapper, Path path) throws CSVException {

         if(kontowrapper.getKontauszugZeile().isEmpty()) {
             throw new CSVException(CSVException.Message.NichtsZumExportieren);
         }

                //header
        String content = "Transaktionsdatum; Empfänger; Sender; Beschreibung; Betrag"+"\n";

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Datenzeilen
        for (KontoauszugZeile gz : kontowrapper.getKontauszugZeile()) {
            String formattedDate = formatTimestamp(gz.getDatum(), dateFormatter);
            content += gz.getEmpfaenger() + ";";
            content += gz.getSender() + ";";
            content += gz.getBeschreibung() + ";";
            content += gz.getBetrag() + "\n";
        }
        write(path,content,ExportTypes.KONTOAUSZUG);
    }


    private String formatTimestamp(Date date, DateTimeFormatter formatter) {
        if (date == null) {
            return "";
        }
        LocalDateTime localDateTime = date.toLocalDateTime();
        return localDateTime.format(formatter);
    }



    public void exportNachrichten(List<NachrichtView> nachrichtViews,Path path) throws CSVException {

        if(nachrichtViews.isEmpty()) {
            throw new CSVException(CSVException.Message.NichtsZumExportieren);
        }

        String content = "Datum;Sender;Empfänger;Nachricht+"+"\n";

        for (NachrichtView gz : nachrichtViews) {
            content += gz.getDate() + ";";
            content += gz.getSender() + ";";
            content += gz.getEmpfaenger() + ";";
            content += gz.getMessage()+ ";"+ "\n";
        }
            // todo hier noch die addInfo Methode einfügen und saren mit wem die convo war (falls es eine spezifische convo war...
        write(path,content, ExportTypes.NACHRICHTEN.addInfo(nachrichtViews.getFirst().getEmpfaenger()));

    }


    public void write(Path zielPfad,String content,ExportTypes type) throws CSVException {

        LocalDate aktuellesDatum = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formatiertesDatum = aktuellesDatum.format(formatter);

        try {
            //todo kann jmd mir (leo) mal erklären wie man das anständig macht, keine lust das jetzt rauszusuchen
            //der Writer appended die date falls der datei name im dir  schon vergeben ist
            FileWriter writer = new FileWriter(zielPfad+"\\"+type.getName() + formatiertesDatum+ ".csv",true);
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            throw new CSVException(CSVException.Message.WriteFailed.addInfo(e.getMessage()));
        }

    }


}