package serviceTest;

import data.identifier.UserId;
import data.nachricht.Nachricht;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import repository.directmessages.DirectMessagesRepository;
import service.MessageService;
import service.serviceexception.DatenbankException;
import service.serviceexception.ServiceException;
import service.serviceexception.validateexception.ValidateBeschreibungException;
import validator.Validator;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MessageServiceTest {

    private DirectMessagesRepository repository;
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        repository = mock(DirectMessagesRepository.class); // Mock erstellen
        messageService = new MessageService(repository);   // Service initialisieren
    }

    @Test
    void testGetNachrichten_Success() throws SQLException, ServiceException {
        // Arrange
        UserId userId = new UserId("user-123");
        Nachricht nachricht1 = new Nachricht(new Timestamp(System.currentTimeMillis()), userId, new UserId("user-456"), "Hallo Welt");
        Nachricht nachricht2 = new Nachricht(new Timestamp(System.currentTimeMillis()), userId, new UserId("user-789"), "Test Nachricht");
        List<Nachricht> expectedNachrichten = Arrays.asList(nachricht1, nachricht2);

        when(repository.getNachrichtenByUserId(userId)).thenReturn(expectedNachrichten);

        // Act
        List<Nachricht> result = messageService.getNachrichten(userId);

        // Assert
        assertEquals(2, result.size());
        assertEquals(expectedNachrichten, result);
        verify(repository).getNachrichtenByUserId(userId);
    }

    @Test
    void testGetNachrichten_DatenbankFehler() throws SQLException {
        // Arrange
        UserId userId = new UserId("user-123");

        when(repository.getNachrichtenByUserId(userId)).thenThrow(new SQLException("DB Fehler"));

        // Act & Assert
        DatenbankException exception = assertThrows(DatenbankException.class, () -> messageService.getNachrichten(userId));
        assertEquals(DatenbankException.Message.INTERNAL_SERVER_ERROR.getServiceErrorMessage(), exception.getMessage());
        verify(repository).getNachrichtenByUserId(userId);
    }

    @Test
    void testSendMessage_Success() throws SQLException, ValidateBeschreibungException, DatenbankException {
        // Arrange
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        UserId sender = new UserId("sender-123");
        UserId empfaenger = new UserId("empfaenger-456");
        String message = "Hallo, wie geht's?";

        doNothing().when(repository).createDirectMessage(timestamp, sender, empfaenger, message);

        // Act
        boolean result = messageService.sendMessage(timestamp, sender, empfaenger, message);

        // Assert
        assertTrue(result);
        verify(repository).createDirectMessage(timestamp, sender, empfaenger, message);
    }

    @Test
    void testSendMessage_ValidateBeschreibungException() throws SQLException {
        // Arrange
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        UserId sender = new UserId("sender-123");
        UserId empfaenger = new UserId("empfaenger-456");
        String invalidMessage = "";

        // Act & Assert
        assertThrows(ValidateBeschreibungException.class, () -> messageService.sendMessage(timestamp, sender, empfaenger, invalidMessage));
        verify(repository, never()).createDirectMessage(any(), any(), any(), any());
    }

    @Test
    void testSendMessage_DatenbankFehler() throws SQLException {
        // Arrange
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        UserId sender = new UserId("sender-123");
        UserId empfaenger = new UserId("empfaenger-456");
        String message = "Hallo";

        doThrow(new SQLException("DB Fehler")).when(repository).createDirectMessage(timestamp, sender, empfaenger, message);

        // Act & Assert
        DatenbankException exception = assertThrows(DatenbankException.class, () -> messageService.sendMessage(timestamp, sender, empfaenger, message));
        assertEquals(DatenbankException.Message.INTERNAL_SERVER_ERROR.getServiceErrorMessage(), exception.getMessage());
        verify(repository).createDirectMessage(timestamp, sender, empfaenger, message);
    }
}
