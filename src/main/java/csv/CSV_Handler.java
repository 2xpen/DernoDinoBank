package csv;

import data.anweisungen.UeberweisungsAnweisungParam;
import data.geschaeftsvorfall.KontoauszugZeile;
import data.nachricht.NachrichtView;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CSV_Handler {

    public ArrayList<UeberweisungsAnweisungParam> massenueberweisung(String path) throws CSVException {
        ArrayList<UeberweisungsAnweisungParam> rueckgabe = new ArrayList<>();
        Scanner scan;

        try {
            scan = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            throw new CSVException(CSVException.Message.FileNotFound.addPath(path));
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


    public void exportKontoAuszug(List<KontoauszugZeile> list, Path path) throws CSVException {
                //header
        String content = "Transaktionsdatum; Empfänger; Sender; Beschreibung; Betrag"+"\n";
        for (KontoauszugZeile gz : list) {
            content += gz.getDatum() + ";";
            content += gz.getEmpfaenger() + ";";
            content += gz.getSender() + ";";
            content += gz.getBeschreibung() + ";";
            content += gz.getBetrag() + "\n";
        }
        write(path,content,ExportTypes.KONTOAUSZUG);
    }



    public void exportDirectMessages(List<NachrichtView> nachrichtViews,Path path) throws CSVException {
        String content = "Datum;Sender;Empfänger;Nachricht+"+"\n";
        for (NachrichtView gz : nachrichtViews) {
            content += gz.getDate() + ";";
            content += gz.getSender() + ";";
            content += gz.getEmpfaenger() + ";";
            content += gz.getMessage()+ ";"+ "\n";
        }

            // todo hier noch die addInfo Methode einfügen und saren mit wem die convo war (falls es eine spezifische convo war...
        write(path,content, ExportTypes.NACHRICHTEN);

    }




    public void write(Path zielPfad,String content,ExportTypes type) throws CSVException {

        LocalDate aktuellesDatum = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formatiertesDatum = aktuellesDatum.format(formatter);

        try {
            //todo kann jmd mir (leo) mal erklären wie man das anständig macht, keine lust das jetzt rauszusuchen
            FileWriter writer = new FileWriter(zielPfad+"\\"+type.getName() + formatiertesDatum+ ".csv");
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            throw new CSVException(CSVException.Message.WriteFailed.addInfo(e.getMessage()));
        }

    }


}