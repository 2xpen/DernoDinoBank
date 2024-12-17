package csv;

import data.anweisungen.UeberweisungsAnweisungParam;
import data.geschaeftsvorfall.KontoauszugZeile;
import data.nachricht.NachrichtView;
import data.pinnwand.PinnwandEntryView;

import java.io.*;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
        while (scan.hasNext()) {
            int zeilenIndex = 0;
            List<String> tokens = List.of(scan.nextLine().split(";"));
            if (tokens.size() < 3) {
                throw new CSVException(CSVException.Message.CSFFormat.addZeile(zeilenIndex));
            }
            try {
                rueckgabe.add(
                        new UeberweisungsAnweisungParam(
                                tokens.get(0)
                                , tokens.get(2)
                                , Double.parseDouble(tokens.get(1)))
                );
            } catch (NumberFormatException e) {
                throw new CSVException(CSVException.Message.NumberFormat.addZeile(zeilenIndex).addInfo(tokens.get(2)));
            }
        }
        scan.close();
        return rueckgabe;
    }



    public void exportPinnwandnachrichten(List<PinnwandEntryView> pinnwandEntryViews, Path path) throws CSVException {
        String content = "Zeitpunkt der Nachricht;Sender;Empfänger;Nachricht" + "\n";

        if(pinnwandEntryViews.isEmpty()) {
            throw new CSVException(CSVException.Message.NichtsZumExportieren);
        }

        for (PinnwandEntryView entry : pinnwandEntryViews) {
            content += entry.getFormattedTimestamp() + ";";
            content += entry.getEmpfaengerName() + ";";
            content += entry.getAutorName() + ";";
            content += entry.getNachricht() + ";" + "\n";
        }
        write(path,content, ExportTypes.NACHRICHTEN.addInfo(pinnwandEntryViews.getFirst().getEmpfaengerName().toString()));
    }

    public void exportKontoAuszuege(List<KontoauszugZeile> list, Path path) throws CSVException {

         if(list.isEmpty()) {
             throw new CSVException(CSVException.Message.NichtsZumExportieren);
         }


        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                //header
        String content = "Transaktionsdatum; Empfänger; Sender; Beschreibung; Betrag"+"\n";
        for (KontoauszugZeile gz : list) {

            String formattedDate = formatDate(gz.getDatum(), dateFormatter);

            content += formattedDate + ";";
            content += gz.getEmpfaenger() + ";";
            content += gz.getSender() + ";";
            content += gz.getBeschreibung() + ";";
            content += gz.getBetrag() + "\n";
        }
        write(path,content,ExportTypes.KONTOAUSZUG);
    }

    // Hilfsmethode zur Formatierung eines Date-Objekts
    private String formatDate(Date date, SimpleDateFormat formatter) {
        if (date == null) {
            return "";
        }
        return formatter.format(date);
    }


    public void exportNachrichten(List<NachrichtView> nachrichtViews,Path path) throws CSVException {


        if(nachrichtViews.isEmpty()) {
            throw new CSVException(CSVException.Message.NichtsZumExportieren);
        }

        String content = "Zeitpunkt der Nachricht;Sender;Empfänger;Nachricht"+"\n";
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