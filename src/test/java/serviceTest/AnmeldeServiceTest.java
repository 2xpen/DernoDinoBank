package serviceTest;

import data.user.Passwort;
import data.user.User;
import data.user.UserName;
import data.identifier.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import repository.UserRepository;
import service.AnmeldeService;
import service.serviceexception.AnmeldeServiceException;
import service.serviceexception.DatenbankException;
import service.serviceexception.validateexception.ValidateBeschreibungException;
import service.serviceexception.validateexception.ValidateBetragException;
import service.serviceexception.validateexception.ValidateUsernameException;
import service.serviceexception.validateexception.ValidatePasswortException;
import validator.Validator;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnmeldeServiceTest {

    private UserRepository userRepository;
    private AnmeldeService anmeldeService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class); // Mock des Repositories
        anmeldeService = new AnmeldeService(userRepository); // Service mit Mock initialisieren
    }

    @Test
    void testAnmeldenErfolgreich() throws Exception {
        UserName userName = new UserName("t.ramos@hsw-stud.de");
        Passwort passwort = new Passwort("validPass");

        // Erstelle ein mock-User-Objekt mit einer UserId und UserName
        User mockUser = User.createUser(userName); // Benutzer mit der statischen Methode erstellen
        when(userRepository.anmelden(userName, passwort)).thenReturn(mockUser);

        User result = anmeldeService.anmelden(userName, passwort);

        assertNotNull(result); // Sicherstellen, dass ein User zurückgegeben wird
        assertEquals(mockUser, result); // Überprüfen, ob der richtige User zurückgegeben wird
        verify(userRepository).anmelden(userName, passwort); // Sicherstellen, dass die Methode aufgerufen wurde
    }

    @Test
    void testPasswortFalsch() throws Exception {
        UserName userName = new UserName("t.ramos@hsw-stud.de");
        Passwort passwort = new Passwort("wrongPass");

        when(userRepository.anmelden(userName, passwort)).thenReturn(null);
        when(userRepository.userNameExists(userName)).thenReturn(true);

        AnmeldeServiceException exception = assertThrows(AnmeldeServiceException.class,
                () -> anmeldeService.anmelden(userName, passwort));

        assertEquals("Passwort ist nicht korrekt", exception.getMessage());
    }

    @Test
    void testBenutzernameNichtVergeben() throws Exception {
        UserName userName = new UserName("michgibtsnicht@hsw-stud.de");
        Passwort passwort = new Passwort("anyPass");

        when(userRepository.anmelden(userName, passwort)).thenReturn(null);
        when(userRepository.userNameExists(userName)).thenReturn(false);

        AnmeldeServiceException exception = assertThrows(AnmeldeServiceException.class,
                () -> anmeldeService.anmelden(userName, passwort));

        assertEquals("der Benutzername ist nicht vergeben", exception.getMessage());
    }

    @Test
    void testValidatorUserNameFehler() {
        UserName invalidUserName = new UserName("invalid");
        Passwort passwort = new Passwort("validPass");

            assertThrows(ValidateUsernameException.class, () -> anmeldeService.anmelden(invalidUserName, passwort));

    }

    @Test
    void testValidatorPasswortFehler() {
        UserName userName = new UserName("t.ramos@hsw-stud.de");
        Passwort invalidPasswort = new Passwort("");


            assertThrows(ValidatePasswortException.class, () -> anmeldeService.anmelden(userName, invalidPasswort));

    }


    @Test
    void testValidatorBetragFehler() {
        double invalidBetrag = -1.0;

        assertThrows(ValidateBetragException.class, () -> Validator.isValidBetrag(invalidBetrag));
    }

    @Test
    void testValidatorBeschreibungFehler() {
        String invalidBeschreibung = "This description is too long and invalid because it contains prohibited characters!fjdsklöfjsaklöfjsaklöfjdskalöfjskalöfjdsaklöfjdsakölfjsdaklöfjdsaklöfjdskalödfjkdlösajfdskalöfjdsaklöfjdsklöfjasdklöfjdskölafjdsaklö";

        assertThrows(ValidateBeschreibungException.class, () -> Validator.isValidBeschreibung(invalidBeschreibung));
    }
}
