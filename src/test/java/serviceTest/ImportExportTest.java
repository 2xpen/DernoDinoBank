package serviceTest;

import csv.CSV_Handler;
import csv.ICSV_IMPORT_EXPORT;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import service.ImportExportService;
import service.KontoService;
import service.UserService;

public class ImportExportTest {

    ICSV_IMPORT_EXPORT csvHandler = new CSV_Handler();
    private UserService userService;
    private KontoService kontoService;
    private ImportExportService importExportService;


    @BeforeEach
    public void setUp() {
        userService = Mockito.mock(UserService.class);
        kontoService = Mockito.mock(KontoService.class);

        importExportService = new ImportExportService(userService, kontoService);
    }

}
