package serviceTest;

import data.identifier.KontoId;
import data.identifier.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import repository.konto.KontoRepository;
import service.KontoService;
import service.serviceexception.DatenbankException;
import service.serviceexception.ServiceException;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KontoServiceTest {

    private KontoRepository kontoRepository;
    private KontoService kontoService;

    @BeforeEach
    void setUp() {
        kontoRepository = mock(KontoRepository.class); // Mock des Repositories
        kontoService = new KontoService(kontoRepository); // Service mit Mock initialisieren
    }

    @Test
    void testErmittelKontoIdByUserId_Success() throws SQLException, DatenbankException {
        // Arrange
        UserId userId = new UserId("user-123");
        KontoId expectedKontoId = new KontoId("konto-456");

        when(kontoRepository.kontoIdErmittelnByUserId(userId)).thenReturn(expectedKontoId);

        // Act
        KontoId result = kontoService.ermittelKontoIdByUserId(userId);

        // Assert
        assertEquals(expectedKontoId, result);
        verify(kontoRepository).kontoIdErmittelnByUserId(userId);
    }

    @Test
    void testErmittelKontoIdByUserId_ThrowsSQLException() throws SQLException {
        // Arrange
        UserId userId = new UserId("user-123");

        when(kontoRepository.kontoIdErmittelnByUserId(userId)).thenThrow(SQLException.class);

        // Act & Assert
        assertThrows(DatenbankException.class, () -> kontoService.ermittelKontoIdByUserId(userId));
        verify(kontoRepository).kontoIdErmittelnByUserId(userId);
    }

    @Test
    void testKontostandErmitteln_Success() throws SQLException, ServiceException {
        // Arrange
        UserId userId = new UserId("user-123");
        KontoId kontoId = new KontoId("konto-456");
        double expectedKontostand = 1500.75;

        when(kontoRepository.kontoIdErmittelnByUserId(userId)).thenReturn(kontoId);
        when(kontoRepository.ladeKontoStandVonKonto(kontoId)).thenReturn(expectedKontostand);

        // Act
        double result = kontoService.kontostandErmitteln(userId);

        // Assert
        assertEquals(expectedKontostand, result, 0.001);
        verify(kontoRepository).kontoIdErmittelnByUserId(userId);
        verify(kontoRepository).ladeKontoStandVonKonto(kontoId);
    }

    @Test
    void testKontostandErmitteln_ThrowsSQLException() throws SQLException {
        // Arrange
        UserId userId = new UserId("user-123");

        when(kontoRepository.kontoIdErmittelnByUserId(userId)).thenThrow(SQLException.class);

        // Act & Assert
        assertThrows(DatenbankException.class, () -> kontoService.kontostandErmitteln(userId));
        verify(kontoRepository).kontoIdErmittelnByUserId(userId);
        verify(kontoRepository, never()).ladeKontoStandVonKonto(any());
    }

    @Test
    void testErmittelUserIdByKontoId_Success() throws SQLException, DatenbankException {
        // Arrange
        KontoId kontoId = new KontoId("konto-456");
        UserId expectedUserId = new UserId("user-123");

        when(kontoRepository.ermittelUserIdByKontoId(kontoId)).thenReturn(expectedUserId);

        // Act
        UserId result = kontoService.ermittelUserIdByKontoId(kontoId);

        // Assert
        assertEquals(expectedUserId, result);
        verify(kontoRepository).ermittelUserIdByKontoId(kontoId);
    }

    @Test
    void testErmittelUserIdByKontoId_ThrowsSQLException() throws SQLException {
        // Arrange
        KontoId kontoId = new KontoId("konto-456");

        when(kontoRepository.ermittelUserIdByKontoId(kontoId)).thenThrow(SQLException.class);

        // Act & Assert
        assertThrows(DatenbankException.class, () -> kontoService.ermittelUserIdByKontoId(kontoId));
        verify(kontoRepository).ermittelUserIdByKontoId(kontoId);
    }
}