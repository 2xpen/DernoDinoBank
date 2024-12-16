package menu.registrierung;

import data.user.Passwort;
import data.user.UserName;
import menu.ManagerBase;
import menu.Menufehlermeldungen;
import menu.startseite.StartseiteManager;
import service.RegistrierungService;
import service.serviceexception.ServiceException;
import service.serviceexception.validateexception.ValidateUsernameException;
import validator.Validator;

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

            decider(RegistrierungMenuOption.ofWahlnummer(Integer.parseInt(scanner.next())));
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
        UserName name = new UserName(scanner.next());
        REGISTRIERUNG_DIALOG.PASSWORT.print();
        Passwort passwort = new Passwort(scanner.next());
        try {
            registrierungService.registrieren(name, passwort);
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
            System.out.println("erneut versuchen? (y) " +
                    "\nsonst anderes zeichen wählen");

            if (scanner.next().equalsIgnoreCase("y")) {
                printFooter();
                registrieren();
            } else start();

        }

        System.out.println("Registrierung erfolgreich, es wurde für: " + name + " ein Konto erstellt");
        printFooter();
        startseiteManager.start();

    }


}
