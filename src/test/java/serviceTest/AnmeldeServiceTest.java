package serviceTest;

import data.identifier.UserId;
import data.user.Passwort;
import data.user.User;
import data.user.UserName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.AnmeldeService;
import service.serviceexception.ServiceException;

import java.sql.SQLException;

public class AnmeldeServiceTest {
    private final AnmeldeService anmeldeService;


    public AnmeldeServiceTest(AnmeldeService anmeldeService) {
        this.anmeldeService = anmeldeService;

    }

    @Test
    public void testAnmeldeServicePositive() throws SQLException, ServiceException {
        UserName testUsername = new UserName("l.sindermann@hsw-stud.de");
        Passwort testPasswort = new Passwort("a");
        User user = new User(new UserId("31df4494-3a25-457f-978b-b1d694"), new UserName("l.sindermann@hsw-stud.de"));
        Assertions.assertEquals(anmeldeService.anmelden(testUsername, testPasswort), user);
    }
    @Test
    public void testAnmeldeServiceNegative() throws ServiceException {
        UserName testUsername = new UserName("l.sinderman@hsw-stud.de");
        Passwort testPasswort = new Passwort("");
        Assertions.assertNull(anmeldeService.anmelden(testUsername, testPasswort));
    }
}