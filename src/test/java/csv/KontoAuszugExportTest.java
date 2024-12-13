package csv;

import data.geschaeftsvorfall.GevoArt;
import data.geschaeftsvorfall.KontoauszugZeile;
import data.user.UserName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class KontoAuszugExportTest {

    private CSV_Handler csvHandler = new CSV_Handler();


    @Test
    void testCSVExport_POSITIV() throws CSVException {

        List<KontoauszugZeile> liste =

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
                );

        csvHandler.kontoauszugDrucken(liste);

    }


}
