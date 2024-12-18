package serviceTest;

import data.anweisungen.UeberweisungsAnweisung;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.konto.KontoRepository;
import service.GevoService;
import service.ImportExportService;
import service.KontoService;
import service.UserService;
import service.ueberweisung.TransaktionsService;
import service.ueberweisung.TransaktionsValidatorService;

import static org.mockito.Mockito.mock;

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
        transaktionsValidatorService = mock(TransaktionsValidatorService.class);
        kontoService = mock(KontoService.class);
        userService = mock(UserService.class);
        kontoRepository = mock(KontoRepository.class);
        importExportService = mock(ImportExportService.class);

        transaktionsService = new TransaktionsService(kontoRepository,gevoService,importExportService,transaktionsValidatorService,kontoService,userService);

    }



    @Test
    void testValidUeberweisung() {

        kontoService.

        UeberweisungsAnweisung anweisung =



    }




}
