package serviceTest;

import data.anweisungen.AbhebungsAnweisung;
import data.anweisungen.UeberweisungsAnweisung;
import data.anweisungen.UeberweisungsAnweisungParam;
import data.identifier.KontoId;
import data.identifier.UserId;
import data.konto.Konto;
import data.user.Passwort;
import data.user.User;
import data.user.UserName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.UserRepository;
import repository.konto.KontoRepository;
import service.*;
import service.serviceexception.RegistrierungException;
import service.serviceexception.ServiceException;
import service.serviceexception.validateexception.ValidateUeberweisungException;
import service.ueberweisung.TransaktionsService;
import service.ueberweisung.TransaktionsValidatorService;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class TransaktionsTest {
    private TransaktionsService transaktionsService;
    private  KontoRepository kontoRepository;
    private  GevoService gevoService;
    private  TransaktionsValidatorService transaktionsValidatorService;
    private  KontoService kontoService;
    private  UserService userService;
    private ImportExportService importExportService;

    @BeforeEach
    void setUp() {
         kontoRepository = mock(KontoRepository.class);
         gevoService = mock(GevoService.class);
         transaktionsValidatorService = mock(TransaktionsValidatorService.class);
         kontoService = mock(KontoService.class);
         userService = mock(UserService.class);
         importExportService = mock(ImportExportService.class);

        transaktionsService = new TransaktionsService(kontoRepository,gevoService,importExportService,transaktionsValidatorService,kontoService,userService);
    }

    @Test
    void testAbheben_validTransaction_shouldNotThrowException() throws ServiceException, SQLException {
        AbhebungsAnweisung anweisung = mock(AbhebungsAnweisung.class);
        when(anweisung.getBetrag()).thenReturn(100.0);
        when(kontoRepository.ladeKontoStandVonKonto(any(KontoId.class))).thenReturn(500.0);

        assertDoesNotThrow(() -> transaktionsService.abheben(anweisung));
    }

    @Test
    void testAbheben_invalidAmount_shouldThrowServiceException() throws ServiceException {
        AbhebungsAnweisung anweisung = mock(AbhebungsAnweisung.class);
        when(anweisung.getBetrag()).thenReturn(-100.0);

        assertThrows(ServiceException.class, () -> transaktionsService.abheben(anweisung));
    }

    @Test
    void testMassenUeberweisung_validTransaction_shouldNotThrowException() throws ServiceException {
        Path quellpfad = mock(Path.class);
        KontoId senderId = mock(KontoId.class);
        UeberweisungsAnweisungParam param = new UeberweisungsAnweisungParam("t.ramos@hsw-stud.de", "description", 100.0);
        List<UeberweisungsAnweisungParam> params = List.of(param);

        when(importExportService.importMassenUeberweisung(quellpfad)).thenReturn(params);

        User mockUser = mock(User.class);
        when(mockUser.getUserId()).thenReturn(mock(UserId.class));
        when(userService.ermittleUserByUserName(new UserName("t.ramos@hsw-stud.de"))).thenReturn(mockUser);

        assertDoesNotThrow(() -> transaktionsService.massenUeberweisung(quellpfad, senderId));
    }

    @Test
    void testMassenUeberweisung_emptyList_shouldThrowValidateUeberweisungException() throws ServiceException {
        Path quellpfad = mock(Path.class);
        KontoId senderId = mock(KontoId.class);
        List<UeberweisungsAnweisungParam> params = List.of();
        when(importExportService.importMassenUeberweisung(quellpfad)).thenReturn(params);

        assertThrows(ValidateUeberweisungException.class, () -> transaktionsService.massenUeberweisung(quellpfad, senderId));
    }

    @Test
    void testEinzelUeberweisung_validTransaction_shouldNotThrowException() throws ServiceException, SQLException {
        UeberweisungsAnweisung anweisung = mock(UeberweisungsAnweisung.class);
        KontoId senderId = mock(KontoId.class);
        KontoId empfaengerId = mock(KontoId.class);
        when(anweisung.getSenderId()).thenReturn(senderId);
        when(anweisung.getEmpfaengerId()).thenReturn(empfaengerId);
        when(kontoRepository.ladeKontoStandVonKonto(any(KontoId.class))).thenReturn(500.0);

        assertDoesNotThrow(() -> transaktionsService.einzelUeberweisung(anweisung));
    }

    @Test
    void testEinzelUeberweisung_invalidTransaction_shouldThrowServiceException() throws ServiceException, SQLException {
        UeberweisungsAnweisung anweisung = mock(UeberweisungsAnweisung.class);
        when(anweisung.getBetrag()).thenReturn(100.0);
        when(kontoRepository.ladeKontoStandVonKonto(any(KontoId.class))).thenReturn(50.0);

        assertThrows(ServiceException.class, () -> transaktionsService.einzelUeberweisung(anweisung));
    }

    @Test
    void testGetKontoIdByUserName_accountExists_shouldReturnKontoId() throws ServiceException {
        UserName userName = mock(UserName.class);
        KontoId expectedKontoId = mock(KontoId.class);
        when(userService.ermittleUserByUserName(userName)).thenReturn(mock(data.user.User.class));
        when(kontoService.ermittelKontoIdByUserId(any())).thenReturn(expectedKontoId);
        when(kontoService.kontoExsist(expectedKontoId)).thenReturn(true);

        KontoId result = transaktionsService.getKontoIdByUserName(userName, 1);

        assertEquals(expectedKontoId, result);
    }

    @Test
    void testGetKontoIdByUserName_accountDoesNotExist_shouldThrowValidateUeberweisungException() throws ServiceException {
        UserName userName = mock(UserName.class);
        when(userService.ermittleUserByUserName(userName)).thenReturn(mock(data.user.User.class));
        when(kontoService.kontoExsist(any(KontoId.class))).thenReturn(false);

        assertThrows(ValidateUeberweisungException.class, () -> transaktionsService.getKontoIdByUserName(userName, 1));
    }
}