package csv;

import data.KontoauszugWrapper;
import data.anweisungen.UeberweisungsAnweisungParam;
import data.nachricht.NachrichtView;
import data.pinnwand.PinnwandEntryView;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public interface ICSV_IMPORT_EXPORT {

    void exportNachrichten(List<NachrichtView> nachrichten, Path path) throws CSVException;

    void exportKontoAuszuege(KontoauszugWrapper kontoauszuege, Path path) throws CSVException;

    void exportPinnwandnachrichten(List<PinnwandEntryView> pinnwandnachrichten, Path path) throws CSVException;

    ArrayList<UeberweisungsAnweisungParam> importMassenueberweisung(Path path) throws CSVException;
}
