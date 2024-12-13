package csv;

import data.anweisungen.UeberweisungsAnweisungParam;
import data.geschaeftsvorfall.GevoZeile;
import data.geschaeftsvorfall.KontoauszugZeile;
import data.nachricht.NachrichtView;

import java.io.*;
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


    public void kontoauszugDrucken(List<KontoauszugZeile> list) throws CSVException {
        //Transaktionsdatum; Empfänger; Sender; Beschreibung; Betrag

        String content = null;

        for (KontoauszugZeile gz : list) {
            content += gz.getDatum() + ";";
            content += gz.getEmpfaenger() + ";";
            content += gz.getSender() + ";";
            content += gz.getBeschreibung() + ";";
            content += gz.getBetrag() + "\n";
        }

        if (content == null) {
            throw new CSVException(CSVException.Message.FileIstEmpty);
        }

        LocalDate aktuellesDatum = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formatiertesDatum = aktuellesDatum.format(formatter);
        write("Kontoauszug_vom_" + formatiertesDatum, content);
    }

    public void write(String name, String content) throws CSVException {
        try {
            FileWriter writer = new FileWriter(name + ".csv");
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            throw new CSVException(CSVException.Message.WriteFailed);
        }
    }


}