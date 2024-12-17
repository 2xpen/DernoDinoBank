package serviceTest;



import data.konto.Konto;
import data.user.Passwort;
import data.user.User;
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
import static org.mockito.Mockito.*;

    class RegistrierungServiceTest {

        private UserRepository userRepository;
        private KontoRepository kontoRepository;
        private RegistrierungService registrierungService;

        @BeforeEach
        void setUp() {
            userRepository = mock(UserRepository.class);
            kontoRepository = mock(KontoRepository.class);
            registrierungService = new RegistrierungService(userRepository, kontoRepository);
        }

        @Test
        void testRegistrieren_UserNameAlreadyExists() throws SQLException {
            UserName userName = new UserName("test@example.com");
            Passwort passwort = new Passwort("password123");

            // Mock: Benutzername existiert bereits
            when(userRepository.userNameExists(userName)).thenReturn(true);

            // Test und Assert
            assertThrows(RegistrierungException.class, () -> registrierungService.registrieren(userName, passwort));

            // Verifizieren, dass keine weiteren Interaktionen mit Repositories stattfanden
            verify(userRepository, never()).createUser(any(User.class), any(Passwort.class));
            verify(kontoRepository, never()).createKonto(any(Konto.class));
        }

        @Test
        void testRegistrieren_UserCreationAndKontoCreationSuccessful() throws SQLException, ServiceException {
            UserName userName = new UserName("test@example.com");
            Passwort passwort = new Passwort("password123");
            User createdUser = User.createUser(userName);

            // Mock: Benutzername existiert nicht
            when(userRepository.userNameExists(userName)).thenReturn(false);

            // Test: Keine Exception geworfen
            assertDoesNotThrow(() -> registrierungService.registrieren(userName, passwort));

            // Verifizieren der Methodenaufrufe
            verify(userRepository).userNameExists(userName);
            verify(userRepository).createUser(any(User.class), eq(passwort));
            verify(kontoRepository).createKonto(any(Konto.class));
        }

        @Test
        void testRegistrieren_UserRepositoryThrowsSQLException() throws SQLException {
            UserName userName = new UserName("test@example.com");
            Passwort passwort = new Passwort("password123");

            // Mock: SQLException beim Erstellen des Benutzers
            when(userRepository.userNameExists(userName)).thenReturn(false);
            doThrow(SQLException.class).when(userRepository).createUser(any(User.class), eq(passwort));

            // Test und Assert
            assertDoesNotThrow(() -> registrierungService.registrieren(userName, passwort));

        }

        @Test
        void testRegistrieren_KontoRepositoryThrowsSQLException() throws SQLException {
            UserName userName = new UserName("test@example.com");
            Passwort passwort = new Passwort("password123");

            // Mock: KontoRepository wirft SQLException
            when(userRepository.userNameExists(userName)).thenReturn(false);
            doNothing().when(userRepository).createUser(any(User.class), eq(passwort));
            doThrow(SQLException.class).when(kontoRepository).createKonto(any(Konto.class));

            // Test und Assert
            assertThrows(RuntimeException.class, () -> registrierungService.registrieren(userName, passwort));

            // Verifizieren der Methodenaufrufe
            verify(userRepository).userNameExists(userName);
            verify(userRepository).createUser(any(User.class), eq(passwort));
            verify(kontoRepository).createKonto(any(Konto.class));
        }
    }


