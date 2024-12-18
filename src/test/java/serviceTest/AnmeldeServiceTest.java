package serviceTest;

import data.identifier.UserId;
import data.user.Passwort;
import data.user.User;
import data.user.UserName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.UserRepository;
import service.AnmeldeService;
import service.serviceexception.AnmeldeServiceException;
import service.serviceexception.DatenbankException;
import service.serviceexception.ServiceException;
import validator.Validator;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AnmeldeServiceTest {


    private AnmeldeService anmeldeService;
    private UserRepository userRepository;






    @BeforeEach
    void setUp() {
         userRepository = mock(UserRepository.class);
         anmeldeService = new AnmeldeService(userRepository);
    }



    @Test
    void testanmeldenPositiv() throws SQLException, ServiceException {

        UserId userId = new UserId("123");
        UserName userName = new UserName("l.sindermann@hsw-stud.de");
        Passwort passwort = new Passwort("password123");

        User user = new User(userId, userName);
        when(userRepository.userNameExists(userName)).thenReturn(true);

        when(userRepository.anmelden(userName,passwort)).thenReturn(user);

        assertEquals(anmeldeService.anmelden(userName,passwort),user);

    }


    @Test
    void testanmeldenPasswortFalsch() throws SQLException, ServiceException {

        UserId userId = new UserId("123");
        UserName userName = new UserName("l.sindermann@hsw-stud.de");
        Passwort wrongPasswort = new Passwort("falsches Passwort");

        User user = new User(userId, userName);


        when(userRepository.userNameExists(userName)).thenReturn(true);
        when(userRepository.anmelden(userName,wrongPasswort)).thenReturn(null);

        AnmeldeServiceException anmeldeServiceException =
                assertThrows(AnmeldeServiceException.class, () ->anmeldeService.anmelden(userName, wrongPasswort));

        assertEquals(anmeldeServiceException.getMessage(), AnmeldeServiceException.Message.PASSWORT_NICHT_KORREKT.getServiceErrorMessage());
    }

    @Test
    void testanmeldenBenutzernameNichtVergeben() throws SQLException, ServiceException {

        UserId userId = new UserId("123");
        UserName userName = new UserName("l.sindermann@hsw-stud.de");
        Passwort wrongPasswort = new Passwort("falsches Passwort");

        User user = new User(userId, userName);


        when(userRepository.userNameExists(userName)).thenReturn(false);
        when(userRepository.anmelden(userName,wrongPasswort)).thenReturn(null);

        AnmeldeServiceException anmeldeServiceException =
                assertThrows(AnmeldeServiceException.class, () ->anmeldeService.anmelden(userName, wrongPasswort));


        assertEquals(anmeldeServiceException.getMessage(),AnmeldeServiceException.Message.BENUTZERNAME_NICHT_VERGEBEN.getServiceErrorMessage());
    }





}
