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
        kontoRepository = mock(KontoRepository.class);
        kontoService = new KontoService(kontoRepository);
    }

    @Test
    void testErmittelKontoIdByUserId_Success() throws SQLException, DatenbankException {
        UserId userId = new UserId("user-123");
        KontoId expectedKontoId = new KontoId("konto-456");

        when(kontoRepository.kontoIdErmittelnByUserId(userId)).thenReturn(expectedKontoId);

        KontoId result = kontoService.ermittelKontoIdByUserId(userId);

        assertEquals(expectedKontoId, result);
        verify(kontoRepository).kontoIdErmittelnByUserId(userId);
    }

    @Test
    void testErmittelKontoIdByUserId_ThrowsSQLException() throws SQLException {
        UserId userId = new UserId("user-123");

        when(kontoRepository.kontoIdErmittelnByUserId(userId)).thenThrow(SQLException.class);

        assertThrows(DatenbankException.class, () -> kontoService.ermittelKontoIdByUserId(userId));
        verify(kontoRepository).kontoIdErmittelnByUserId(userId);
    }

    @Test
    void testKontostandErmitteln_Success() throws SQLException, ServiceException {
        UserId userId = new UserId("user-123");
        KontoId kontoId = new KontoId("konto-456");
        double expectedKontostand = 1500.75;

        when(kontoRepository.kontoIdErmittelnByUserId(userId)).thenReturn(kontoId);
        when(kontoRepository.ladeKontoStandVonKonto(kontoId)).thenReturn(expectedKontostand);

        double result = kontoService.kontostandErmitteln(userId);

        assertEquals(expectedKontostand, result, 0.001);
        verify(kontoRepository).kontoIdErmittelnByUserId(userId);
        verify(kontoRepository).ladeKontoStandVonKonto(kontoId);
    }

    @Test
    void testKontostandErmitteln_ThrowsSQLException() throws SQLException {
        UserId userId = new UserId("user-123");

        when(kontoRepository.kontoIdErmittelnByUserId(userId)).thenThrow(SQLException.class);

        assertThrows(DatenbankException.class, () -> kontoService.kontostandErmitteln(userId));
        verify(kontoRepository).kontoIdErmittelnByUserId(userId);
        verify(kontoRepository, never()).ladeKontoStandVonKonto(any());
    }

    @Test
    void testErmittelUserIdByKontoId_Success() throws SQLException, DatenbankException {
        KontoId kontoId = new KontoId("konto-456");
        UserId expectedUserId = new UserId("user-123");

        when(kontoRepository.ermittelUserIdByKontoId(kontoId)).thenReturn(expectedUserId);

        UserId result = kontoService.ermittelUserIdByKontoId(kontoId);

        assertEquals(expectedUserId, result);
        verify(kontoRepository).ermittelUserIdByKontoId(kontoId);
    }

    @Test
    void testErmittelUserIdByKontoId_ThrowsSQLException() throws SQLException {
        KontoId kontoId = new KontoId("konto-456");

        when(kontoRepository.ermittelUserIdByKontoId(kontoId)).thenThrow(SQLException.class);

        assertThrows(DatenbankException.class, () -> kontoService.ermittelUserIdByKontoId(kontoId));
        verify(kontoRepository).ermittelUserIdByKontoId(kontoId);
    }
}
