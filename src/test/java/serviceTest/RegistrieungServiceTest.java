package serviceTest;

import data.user.Passwort;
import data.user.UserName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import repository.UserRepository;
import repository.konto.KontoRepository;
import service.RegistrierungService;
import service.serviceexception.RegistrierungException;
import service.serviceexception.ServiceException;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class RegistrieungServiceTest {

    private UserRepository userRepository;
    private KontoRepository kontoRepository;

    private RegistrierungService registrierungService;

    @BeforeEach
    public void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        kontoRepository = Mockito.mock(KontoRepository.class);

        registrierungService = new RegistrierungService(userRepository, kontoRepository);
    }


    @Test
    void testRegistrierungPositiv() throws SQLException, ServiceException {

        UserName userName = new UserName("valid@hsw-stud.de");
        Passwort password = new Passwort("validPasswort");


        when(userRepository.userNameExists(userName)).thenReturn(false);

        assertDoesNotThrow(() -> registrierungService.registrieren(userName, password));

    }


    @Test
    void testRegistrierungBenutzernameVergeben() throws SQLException, ServiceException {

        UserName userName = new UserName("schonVergeben@hsw-stud.de");
        Passwort password = new Passwort("validPasswort");


        when(userRepository.userNameExists(userName)).thenReturn(true);

        RegistrierungException e =
                assertThrows(RegistrierungException.class, () -> registrierungService.registrieren(userName, password));

        assertEquals(RegistrierungException.Message.BENUTZERNAME_BEREITS_VERGEBEN.getServiceErrorMessage(), e.getMessage());

    }


}
