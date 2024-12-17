package serviceTest;

import data.identifier.PinnwandEntryId;
import data.identifier.UserId;
import data.pinnwand.Pinnwand;
import data.pinnwand.PinnwandEntry;
import data.pinnwand.PinnwandEntryView;
import data.user.User;
import data.user.UserName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import repository.pinnwand.PinnwandRepository;
import service.PinnwandService;
import service.UserService;
import service.serviceexception.DatenbankException;
import service.serviceexception.ServiceException;
import service.serviceexception.validateexception.ValidateBeschreibungException;
import validator.Validator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PinnwandServiceTest {

    @Mock
    private PinnwandRepository pinnwandRepository;

    @Mock
    private UserService userService;

    private PinnwandService pinnwandService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        pinnwandService = new PinnwandService(pinnwandRepository, userService);
    }

/*    @Test
    public void testGetPinnwand() throws ServiceException, SQLException {
        // Arrange
        UserId userId = new UserId("dd7e720a-4bb6-425b-be98-a81ff3");
        List<PinnwandEntry> entries = new ArrayList<>();
        PinnwandEntry entry = new PinnwandEntry("Test message", new UserId("f87974fa-cbdd-4748-b92f-d275af"));
        entries.add(entry);

        when(PinnwandRepository.getPinnwandByUserId(userId)).thenReturn(entries);
        when(userService.ermittleUserByUserId(any())).thenReturn(new User(new UserId("dd7e720a-4bb6-425b-be98-a81ff3"),new UserName("test user")));

        // Act
        Pinnwand pinnwand = pinnwandService.getPinnwand(userId);

        // Assert
        assertNotNull(pinnwand);
        assertEquals(1, pinnwand.getPinnwandentries().size());
        assertEquals("Test User", pinnwand.getPinnwandentries().get(0).getAutorName().toString());
    }*/

    @Test
    public void testSchreibenAufAnderePinnwand() throws ServiceException, ValidateBeschreibungException, SQLException {
        // Arrange
        String message = "Test message";
        UserId autorId = new UserId("f87974fa-cbdd-4748-b92f-d275af");
        UserId besitzerId = new UserId("dd7e720a-4bb6-425b-be98-a81ff3");

        // Act
        pinnwandService.schreibenAufAnderePinnwand(message, autorId, besitzerId);

        // Assert
        verify(pinnwandRepository, times(1));
        PinnwandRepository.createPinnwandentry(message, autorId, besitzerId);
    }

    @Test
    public void testConvertPinnwandEntriesToPinnwand() throws ServiceException, SQLException {
        // Arrange
        List<PinnwandEntry> entries = new ArrayList<>();
        PinnwandEntry entry = new PinnwandEntry("Test message", new UserId("1"));
        entries.add(entry);

        when(userService.ermittleUserByUserId(any())).thenReturn(User.createUser(new UserName("Test User")));

        // Act
        Pinnwand pinnwand = pinnwandService.convertPinnwandEntriesToPinnwand(entries);

        // Assert
        assertNotNull(pinnwand);
        assertEquals(1, pinnwand.getPinnwandentries().size());
        assertEquals("Test User", pinnwand.getPinnwandentries().get(0).getAutorName().toString());
    }


}
