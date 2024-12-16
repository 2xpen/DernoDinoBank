package transaktion;

import com.sun.tools.javac.Main;
import data.anweisungen.UeberweisungsAnweisung;
import data.identifier.KontoId;
import data.user.Passwort;
import data.user.User;
import data.user.UserName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.AnmeldeService;
import service.GevoService;
import service.KontoService;
import service.UserService;
import service.serviceexception.ServiceException;
import service.ueberweisung.TransaktionsService;

public class Ueberweisungstest {
    private final KontoService kontoService;
    private final UserService userService;
    private final TransaktionsService transaktionsService;
    private final GevoService gevoService;
    private final AnmeldeService anmeldeService;

    public Ueberweisungstest(KontoService kontoService, UserService userService, TransaktionsService transaktionsService, GevoService gevoService, AnmeldeService anmeldeService) {
        this.kontoService = kontoService;
        this.userService = userService;
        this.transaktionsService = transaktionsService;
        this.gevoService = gevoService;
        this.anmeldeService = anmeldeService;
    }



    @Test
    void testUeberweisungPositiv () throws ServiceException {
        User testuser1 = anmeldeService.anmelden(new UserName("l.sindermann@hsw-stud.de"), new Passwort("a"));
        User testuser2 = anmeldeService.anmelden(new UserName("t.ramos@hsw-stud.de"), new Passwort("a"));

        KontoId kontoIdTestuser1 = kontoService.ermittelKontoIdByUserId(testuser1.getUserId());
        KontoId kontoIdTestuser2 = kontoService.ermittelKontoIdByUserId(testuser2.getUserId());

        String beschreibungTest = "Testüberweisung";
        double testBetrag = 20;
        UeberweisungsAnweisung anweisung = new UeberweisungsAnweisung(kontoIdTestuser1, kontoIdTestuser2, beschreibungTest, testBetrag);

        double kontostandTestuser1bevorUeberweisung = kontoService.kontostandErmitteln(testuser1.getUserId());
        double kontostandTestuser2bevorUeberweisung = kontoService.kontostandErmitteln(testuser2.getUserId());

        double erwateterKontostandTestuser1nachUeberweisung = kontostandTestuser1bevorUeberweisung -20;
        double erwarteterKontostandTestuser2nachUeberweisung = kontostandTestuser2bevorUeberweisung + 20;

        ///Hier wird dann die Überweisung durchgeführt


        transaktionsService.einzelUeberweisung(anweisung);

        assert(kontoService.kontostandErmitteln(testuser1.getUserId()) == erwateterKontostandTestuser1nachUeberweisung);
        assert(kontoService.kontostandErmitteln(testuser2.getUserId()) == erwarteterKontostandTestuser2nachUeberweisung);

    }

}
