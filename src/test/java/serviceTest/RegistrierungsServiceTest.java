package serviceTest;

import data.user.Passwort;
import data.user.User;
import data.user.UserName;
import org.junit.jupiter.api.Test;
import repository.UserRepository;
import service.AnmeldeService;
import service.KontoService;
import service.RegistrierungService;
import service.UserService;
import service.serviceexception.ServiceException;

public class RegistrierungsServiceTest {
    private final RegistrierungService registrierungService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final AnmeldeService anmeldeService;
    private final KontoService kontoService;


    public RegistrierungsServiceTest(RegistrierungService registrierungService, UserService userService , UserRepository userRepository, UserRepository userRepository1, AnmeldeService anmeldeService , KontoService kontoService) {
        this.registrierungService = registrierungService;
        this.userService = userService;
        this.userRepository = userRepository1;
        this.anmeldeService = anmeldeService;
        this.kontoService = kontoService;
    }

    @Test
    void testRegistrierung () throws ServiceException {


        registrierungService.registrieren(new UserName("m.farwick@hsw-stud.de"), new Passwort("a"));
        User testUser = anmeldeService.anmelden(new UserName("m.farwick@hsw-stud.de"), new Passwort("a"));
        String testUsername = "m.farwick@hsw-stud.de";
        String testPasswort = "a";

        ///Hier gucken ob Konto mit erstellt wird.


        assert testUser != null;
        assert kontoService.ermittelKontoIdByUserId(userService.ermittleUserByUserName(testUser.getUsername()).getUserId()) != null;
        assert testUsername.equals(testUser.getUsername().toString());
        /// Todo @Leo schauen ob das Konto auch auf den richtigen User zeigt.


    }
}
