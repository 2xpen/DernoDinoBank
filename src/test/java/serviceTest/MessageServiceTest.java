package serviceTest;

import data.identifier.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.UserRepository;
import repository.directmessages.DirectMessagesRepository;
import service.AnmeldeService;
import service.MessageService;
import service.UserService;
import service.serviceexception.DatenbankException;
import service.serviceexception.validateexception.ValidateBeschreibungException;
import validator.Validator;

import java.sql.Timestamp;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MessageServiceTest {
    private MessageService messageService;
    private DirectMessagesRepository directMessagesRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        directMessagesRepository = mock(DirectMessagesRepository.class);
        userService = mock(UserService.class);
        messageService = new MessageService(directMessagesRepository, userService);
    }

    @Test
    void testBeschreibungZuLang() {
        Timestamp date = new Timestamp(System.currentTimeMillis());
        UserId sender = new UserId("sender");
        UserId empfaenger = new UserId("empfaenger");
        String message = "skjdfhlskdjfhksdjfhsdjkfhkjsdhflskdjhfkjsadhflkjasdhflksjdahflksajdhflskjdahfskljdahflksjadhfklsadjfhskldjfhklsjdhfklsjdfhsjkldhfskljdhfklsjdhfkljsdhfjjjjjdskljfhasdljkfhiöejhkjhfsdkösjfhalksjehflskdjafhuhisdfjklsajdghflsdjkhflskajdhfalskdjhfaslkjdfhasldkjfhsaldkjfh";

        ValidateBeschreibungException e = assertThrows(ValidateBeschreibungException.class, () -> messageService.sendMessage(date, sender, empfaenger, message));
        assertEquals(ValidateBeschreibungException.Message.BESCHREIBUNG.getServiceErrorMessage(), e.getMessage());
    }


}
