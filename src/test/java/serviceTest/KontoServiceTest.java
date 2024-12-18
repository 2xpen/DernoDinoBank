package serviceTest;

import data.identifier.KontoId;
import data.identifier.UserId;
import data.konto.Konto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.konto.KontoRepository;
import service.KontoService;
import service.serviceexception.DatenbankException;
import service.serviceexception.ServiceException;

import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class KontoServiceTest {

    KontoService kontoService;
    private KontoRepository kontoRepository;



    @BeforeEach
    void setUp() {

        kontoRepository = mock(KontoRepository.class);

        kontoService = new KontoService(kontoRepository);

    }


    @Test
    void testKontoExsistsPositiv() throws SQLException, DatenbankException {

        KontoId kontoId = new KontoId("kontoIdExsistent");

        when(kontoRepository.kontoExsist(kontoId)).thenReturn(true);

        assertTrue(kontoService.kontoExsist(kontoId));
    }




    @Test
    void testKontoExsistsNegativ() throws SQLException, DatenbankException {

        KontoId kontoId = new KontoId("kontoIdNichtExsistent");

        when(kontoRepository.kontoExsist(kontoId)).thenReturn(false);

        assertFalse(kontoService.kontoExsist(kontoId));

    }


    @Test
    void testKontostandErmitteln() throws SQLException, ServiceException {

        KontoId kontoId = new KontoId("kontoIdExsistent");
        UserId userId = new UserId("userIdExsistent");

        when(kontoRepository.kontoIdErmittelnByUserId(userId)).thenReturn(kontoId);
        when(kontoRepository.ladeKontoStandVonKonto(kontoId)).thenReturn(1000d);

        assertEquals(1000d, kontoService.kontostandErmitteln(userId));

    }









}
