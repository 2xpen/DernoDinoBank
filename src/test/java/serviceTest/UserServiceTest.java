package serviceTest;

import data.identifier.UserId;
import data.user.User;
import data.user.UserName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.UserRepository;
import service.UserService;
import service.serviceexception.ServiceException;
import service.serviceexception.UserServiceException;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class UserServiceTest {


    private UserService userService;
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }


    @Test
    void testErmittleUserByUsernamePositiv() throws SQLException, ServiceException {


        UserName userName = new UserName("t.ramos@hsw-stud.de");

        User user = new User(new UserId("123"), userName);

        when(userRepository.findUserByName(userName)).thenReturn(user);

        assertEquals(user, userService.ermittleUserByUserName(userName));

    }

    @Test
    void testErmittleUserByUsernameBenutzerNichtGefunden() throws SQLException {


        UserName userName = new UserName("t.ramos@hsw-stud.de");

        User user = new User(new UserId("123"), userName);

        when(userRepository.findUserByName(userName)).thenReturn(null);

        UserServiceException e =
                assertThrows(UserServiceException.class, () -> userService.ermittleUserByUserName(userName));

        assertEquals(UserServiceException.Message.USER_NICHT_GEFUNDEN.getServiceErrorMessage(), e.getMessage());

    }


}
