package menu.anmeldung;

import data.user.Passwort;
import data.user.UserName;
import menu.ManagerBase;
import menu.Menufehlermeldungen;
import menu.konto.UserLogedInManager;
import menu.startseite.StartseiteManager;
import service.AnmeldeService;
import service.serviceexception.ServiceException;

public class AnmeldungsManager extends ManagerBase {
    private final AnmeldeService anmeldeService;
    private final StartseiteManager startseiteManager;
    private UserLogedInManager userLogedInManager;

    public AnmeldungsManager(StartseiteManager startseiteManager, AnmeldeService anmeldeService) {
        super();
        this.startseiteManager = startseiteManager;
        this.anmeldeService = anmeldeService;
    }

    public void start() {
        printHead();

        ANMELDE_MENU_OPTION.printAll();

        printFooter();

        try {
            int wahlNummer = Integer.parseInt(scanner.nextLine());
            deciderAnmeldeMenuOption(ANMELDE_MENU_OPTION.ofWahlNummer(wahlNummer));
        } catch (NumberFormatException e) {
            Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
            start();
        }
    }

    private void deciderAnmeldeMenuOption(ANMELDE_MENU_OPTION option) {
        switch (option) {
            case ANMELDEN:
                anmeldunginput();
                break;
            case ZURUECK:
                startseiteManager.start();
                break;
            case null:
                Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
                start();
        }
    }

    private void anmeldunginput() {
        printHead();

        ANMELDE_DIALOG.ANMELDE_FENSTER.print();

        printFooter();

        try {
            ANMELDE_DIALOG.BENUTZERNAMEN_EINGEBEN.print();
            UserName benutzername = new UserName(scanner.nextLine());

            ANMELDE_DIALOG.PASSWORT_EINGEBEN.print();
            Passwort password = new Passwort(scanner.nextLine());

            userLogedInManager.start(anmeldeService.anmelden(
                    benutzername,
                    password));
        } catch (ServiceException sf) {
            System.out.println(sf.getMessage());
            start();
        }
    }

    public void setKontoansichtsmanager(UserLogedInManager k) {
        this.userLogedInManager = k;
    }
}