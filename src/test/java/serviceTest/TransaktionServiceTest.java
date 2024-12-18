package serviceTest;

import data.anweisungen.UeberweisungsAnweisung;
import data.identifier.KontoId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.konto.KontoRepository;
import service.GevoService;
import service.ImportExportService;
import service.KontoService;
import service.UserService;
import service.serviceexception.ServiceException;
import service.serviceexception.validateexception.ValidateBeschreibungException;
import service.serviceexception.validateexception.ValidateUeberweisungException;
import service.ueberweisung.TransaktionsService;
import service.ueberweisung.TransaktionsValidatorService;
import validator.Validator;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransaktionServiceTest {


    TransaktionsService transaktionsService;


    private GevoService gevoService;
    private TransaktionsValidatorService transaktionsValidatorService;
    private KontoService kontoService;
    private UserService userService;
    private KontoRepository kontoRepository;
    private ImportExportService importExportService;




    @BeforeEach
    void setUp() {
        gevoService = mock(GevoService.class);
        kontoService = mock(KontoService.class);
        userService = mock(UserService.class);
        kontoRepository = mock(KontoRepository.class);
        importExportService = mock(ImportExportService.class);

        transaktionsValidatorService = new TransaktionsValidatorService(kontoRepository,userService,kontoService);
        transaktionsService = new TransaktionsService(kontoRepository,gevoService,importExportService,transaktionsValidatorService,kontoService,userService);

    }



    @Test
    void testValidUeberweisung() throws ServiceException, SQLException {

        KontoId kontoIdSENDER = new KontoId("senderID");
        KontoId kontoIdEMPFEANGER = new KontoId("empfeangerID");

        String beschreibung = "hier bitt fuf euro";
        double betrag = 5;



        UeberweisungsAnweisung anweisung = new UeberweisungsAnweisung(kontoIdSENDER,kontoIdEMPFEANGER,beschreibung,betrag);

        when(kontoRepository.ladeKontoStandVonKonto(kontoIdSENDER)).thenReturn(1000.0);

        //test failed wenn exception kommt, wenn die methode einfach durchläuft dann ist all good
        transaktionsService.einzelUeberweisung(anweisung);
    }





    @Test
    void testSaldoUeberzogen() throws ServiceException, SQLException {
        /// testdaten erzeugen
        KontoId kontoIdSENDER = new KontoId("senderID");
        KontoId kontoIdEMPFEANGER = new KontoId("empfeangerID");

        String beschreibung = "hier bitt fuftausend euro";
        double betrag = 5000;

        UeberweisungsAnweisung anweisung = new UeberweisungsAnweisung(kontoIdSENDER,kontoIdEMPFEANGER,beschreibung,betrag);

        when(kontoRepository.ladeKontoStandVonKonto(kontoIdSENDER)).thenReturn(1000.0);

        ValidateUeberweisungException e =
        assertThrows(ValidateUeberweisungException.class, () -> transaktionsService.einzelUeberweisung(anweisung));

        assertEquals(ValidateUeberweisungException.Message.BETRAG_UEBERZIEHT_SALDO.getServiceErrorMessage(), e.getMessage());

    }


    @Test
    void testBeschreibungNichtKorrekt() throws ServiceException, SQLException {

        KontoId kontoIdSENDER = new KontoId("senderID");
        KontoId kontoIdEMPFEANGER = new KontoId("empfeangerID");

        String beschreibung = "[Songtext zu „Pokémon-Thema (Komm schnapp sie dir)“]\n" +
                "\n" +
                "[Strophe 1]\n" +
                "Ich will der Allerbeste sein\n" +
                "Wie keiner vor mir war\n" +
                "Ganz allein fang' ich sie mir\n" +
                "Ich kenne die Gefahr\n" +
                "Ich streife durch das ganze Land\n" +
                "Ich suche weit und breit\n" +
                "Das Pokemon um zu verstehn\n" +
                "Was ihm diese Macht verleiht\n" +
                "\n" +
                "[Refrain]\n" +
                "Pokémon, komm, schnapp sie dir\n" +
                "Nur ich und du\n" +
                "In allem was ich auch tu'\n" +
                "Pokémon\n" +
                "Du mein bester Freund, komm' retten wir die Welt\n" +
                "Pokémon, komm, schnapp sie dir\n" +
                "Dein Herz ist gut\n" +
                "Wir vertrauen auf unseren Mut\n" +
                "Ich lern von dir und du von mir\n" +
                "Pokémon\n" +
                "Komm und schnapp sie dir\n" +
                "Komm und schnapp sie dir";

        double betrag = 100;

        UeberweisungsAnweisung anweisung = new UeberweisungsAnweisung(kontoIdSENDER,kontoIdEMPFEANGER,beschreibung,betrag);

        when(kontoRepository.ladeKontoStandVonKonto(kontoIdSENDER)).thenReturn(1000.0);

        ValidateBeschreibungException e =
                assertThrows(ValidateBeschreibungException.class, () -> transaktionsService.einzelUeberweisung(anweisung));

        assertEquals(ValidateBeschreibungException.Message.BESCHREIBUNG.getServiceErrorMessage(), e.getMessage());

    }












}
