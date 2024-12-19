package menu.registrierung;

import data.user.Passwort;
import data.user.UserName;
import menu.ManagerBase;
import menu.Menufehlermeldungen;
import menu.startseite.StartseiteManager;
import service.RegistrierungService;
import service.serviceexception.ServiceException;

public class RegistrierungManger extends ManagerBase {
    private final StartseiteManager startseiteManager;
    private final RegistrierungService registrierungService;

    public RegistrierungManger(StartseiteManager startseiteManager, RegistrierungService registrierungService) {
        super();
        this.startseiteManager = startseiteManager;
        this.registrierungService = registrierungService;
    }

    public void start() {
        printHead();

        RegistrierungMenuOption.printAll();

        printBitteWahlnummerWaehlenFooter();

        try {
            decider(RegistrierungMenuOption.ofWahlnummer(Integer.parseInt(scanner.nextLine())));
        } catch (NumberFormatException e) {
            Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
            start();
        }
    }

    public void decider(RegistrierungMenuOption option) {
        switch (option) {
            case REGISTRIEREN:
                registrieren();
                break;
            case ZURUECK:
                startseiteManager.start();
            case null:
                Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
                start();
        }
    }

    public void registrieren() {
        REGISTRIERUNG_DIALOG.NAME_EINGEBEN.print();
        UserName name = new UserName(scanner.nextLine());
        REGISTRIERUNG_DIALOG.PASSWORT.print();
        Passwort passwort = new Passwort(scanner.nextLine());

        try {
            registrierungService.registrieren(name, passwort);
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
            System.out.println("Erneut versuchen? (y/n)");

            if (scanner.nextLine().equalsIgnoreCase("y")) {
                printFooter();
                registrieren();
            } else start();

        }
        System.out.println("Registrierung erfolgreich. Es wurde für: " + name + " ein Konto erstellt!");
        printFooter();
        startseiteManager.start();
    }
}