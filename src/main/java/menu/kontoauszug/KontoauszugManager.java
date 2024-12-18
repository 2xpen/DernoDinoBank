package menu.kontoauszug;

import data.KontoauszugWrapper;
import data.user.User;
import helper.FileHelper;
import menu.ManagerBase;
import menu.Menufehlermeldungen;
import menu.konto.UserLogedInManager;
import service.GevoService;
import service.ImportExportService;
import service.KontoService;
import service.UserService;
import service.serviceexception.ServiceException;

import java.io.FileNotFoundException;
import java.nio.file.Path;

public class KontoauszugManager extends ManagerBase {

    private final KontoService kontoService;
    private final GevoService gevoService;
    private final UserService userService;
    private final UserLogedInManager userLogedInManager;
    private final ImportExportService importExportService;
    private User user;

    public KontoauszugManager(KontoService kontoService, GevoService gevoService, UserService userService, UserLogedInManager userLogedInManager, ImportExportService importExportService) {
        this.kontoService = kontoService;
        this.gevoService = gevoService;
        this.userService = userService;
        this.userLogedInManager = userLogedInManager;
        this.importExportService = importExportService;
    }

    public void start(User user) {
        this.user = user;

        System.out.println("1: Alle Kontobewegungen sehen");
        System.out.println("2: Kontoauszuege exportieren");
        System.out.println("0: zurück");

        try {
            KontoauszugWrapper kontoauszugWrapper = gevoService.fetchTransaktionsHistorie(user.getUserId());

            int wahlnummer = Integer.parseInt(scanner.nextLine());

            switch (wahlnummer) {
                case 1:
                 printAllTransaktions(kontoauszugWrapper);
                 start(user);
                 break;
                 case 2:
                     exportTransaktionen(kontoauszugWrapper);
                     break;
                 case 0:
                     userLogedInManager.start(user);
                     break;
                 default:
                     Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
                     start(user);
            }

        } catch (NumberFormatException e) {
            Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
            Menufehlermeldungen.ERNEUT_VERSUCHEN.print();
            if(scanner.nextLine().equals("y")){
                start(user);
            }userLogedInManager.start(user);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

    private void uebersichtTransaktionen(KontoauszugWrapper kontoauszugWrapper) {
        printHead();

        printAllTransaktions(kontoauszugWrapper);

        userLogedInManager.start(user);
    }

    public void printAllTransaktions(KontoauszugWrapper kontoauszugWrapper) {
        System.out.println(kontoauszugWrapper.toString());
    }

    public void exportTransaktionen(KontoauszugWrapper kontoauszugWrapper) {
        System.out.println("Bitte Zielpfad angeben!");

        try {
            Path zielpfad = Path.of(scanner.nextLine());
            FileHelper.isPathAccessible(zielpfad);
            importExportService.exportKontobewegungen(kontoauszugWrapper,zielpfad);
            System.out.println("Es wurde ein CSV Dokument mit ihren Kontobewegungen unter "+zielpfad+" abgelegt.");
            start(user);
        } catch (FileNotFoundException | ServiceException e) {
            System.out.println(e.getMessage());
            Menufehlermeldungen.ERNEUT_VERSUCHEN.print();
            if(scanner.nextLine().equals("y")){
                exportTransaktionen(kontoauszugWrapper);
            }
            start(user);
        }
    }
}