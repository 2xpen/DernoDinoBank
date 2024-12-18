package menu.pinnwand;

import data.pinnwand.Pinnwand;
import data.pinnwand.PinnwandEntry;
import data.user.User;
import helper.FileHelper;
import menu.ManagerBase;
import menu.Menufehlermeldungen;
import menu.konto.UserLogedInManager;
import menu.personsuche.PersonSucheManager;
import service.ImportExportService;
import service.PinnwandService;
import service.serviceexception.ServiceException;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.List;

public class PinnwandManager extends ManagerBase {
    private final UserLogedInManager userLogedInManager;
    private final PinnwandService pinnwandService;
    private final ImportExportService importExportService;
    private PersonSucheManager personSucheManager;

    public PinnwandManager(UserLogedInManager userLogedInManager, PinnwandService pinnwandService, ImportExportService importExportService) {
        this.userLogedInManager = userLogedInManager;
        this.pinnwandService = pinnwandService;
        this.importExportService = importExportService;
    }

    public void start(User user) {
        printHead();

        PINNWAND_MENU_OPTION.printAll();

        printFooter();

        try {
            int wahlNummer = Integer.parseInt(scanner.nextLine());
            deciderPinnwandMenuOption(PINNWAND_MENU_OPTION.ofWahlNummer(wahlNummer), user);
        } catch (NumberFormatException e) {
            Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
            start(user);
        }
    }

    private void aufPinnwandSchreiben(User autor, User empfaenger) {
        System.out.println("Bitte gebe deine Nachricht ein:");
        String message = scanner.nextLine();
        try {
            pinnwandService.schreibenAufAnderePinnwand(message, autor.getUserId(), empfaenger.getUserId());
            System.out.println("Ihr Pinnwand eintrag wurde erstellt!");
            pinnwandVonUserAufrufen(autor, empfaenger);
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
            System.out.println("Erneut versuchen? (y/n)");
            if (scanner.nextLine().equals("y")) {
                aufPinnwandSchreiben(autor, empfaenger);
            }
        }
    }

    private void deciderPinnwandMenuOption(PINNWAND_MENU_OPTION option, User selector) {
        switch (option) {
            case PINNWAND_ANSEHEN:
                eigenePinnwandAnsehen(selector);
                break;
            case ZURUECK:
                userLogedInManager.start(selector);
                break;
            case null:
                Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
                start(selector);
                break;
        }

    }

    public void pinnwandVonUserAufrufen(User selector, User selectedUser) {
        try {
            Pinnwand pinnwandVonUser = pinnwandService.getPinnwand(selectedUser.getUserId());
            Pinnwand pinnwandSelector = pinnwandService.getPinnwand(selector.getUserId());
            if (pinnwandVonUser.getPinnwandentries().isEmpty()) {
                PINNWAND_DIALOG.PINNWAND_IST_LEER.print();
            } else {
                System.out.println("Pinnwand von " + selectedUser.getUsername());
                System.out.println(pinnwandVonUser);
            }

            System.out.println("1: Kommentar auf die Pinnwand von " + selectedUser.getUsername() + " schreiben");
            System.out.println("2: Alle Pinnwandnachrichten, die " + selectedUser.getUsername() + " an dich gesendet hat, exportieren. (Anforderung 11)");
            System.out.println("0: Zurück");
            String inputString = scanner.nextLine();

            int input = -1;
            try {
                input = Integer.parseInt(inputString);
            } catch (NumberFormatException n) {
                System.out.println("Ungültige Eingabe, es sind nur Ganzzahlen erlaubt!");
                pinnwandVonUserAufrufen(selector, selectedUser);
            }

            switch (input) {
                case 1:
                    aufPinnwandSchreiben(selector, selectedUser);
                    pinnwandVonUserAufrufen(selector, selectedUser);
                    break;
                case 2:
                    exportPinnwandnachrichten(pinnwandService.filterafterAnforderung11(pinnwandSelector, selector.getUserId(), selectedUser.getUserId()), selector, selectedUser);
                    pinnwandVonUserAufrufen(selector, selectedUser);
                case 0:
                    personSucheManager.startWithSelectedUser(selector, selectedUser);
                default:
                    Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
                    pinnwandVonUserAufrufen(selector, selectedUser);
            }

        } catch (ServiceException serviceException) {
            System.out.println(serviceException.getMessage());
            pinnwandVonUserAufrufen(selector, selectedUser);
        }
    }

    private void exportPinnwandnachrichten(List<PinnwandEntry> pinnwandEntries, User selector, User selectedUser) {
        System.out.println("Bitte den Zielpfad angeben.");
        Path zielPfad = Path.of(scanner.nextLine());

        try {
            FileHelper.isPathAccessible(zielPfad);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("erneut Versuchen? (y/n)");
            if (scanner.nextLine().equals("y")) {
                exportPinnwandnachrichten(pinnwandEntries, selector, selectedUser);
            }
            pinnwandVonUserAufrufen(selector, selectedUser);
        }
        try {
            importExportService.exportPinnwandnachrichten(pinnwandEntries, zielPfad, selector);
            System.out.println("Es wurde ein Protokoll unter: " + zielPfad + " abgelegt");
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
            Menufehlermeldungen.ERNEUT_VERSUCHEN.print();
            if (scanner.nextLine().equals("y")) {

                exportPinnwandnachrichten(pinnwandEntries, selector, selectedUser);
            }
            pinnwandVonUserAufrufen(selector, selectedUser);

        }
    }

    private void eigenePinnwandAnsehen(User user) {
        try {
            Pinnwand pinnwand = pinnwandService.getPinnwand(user.getUserId());
            if (pinnwand.getPinnwandentries().isEmpty()) {
                System.out.println("Pinnwand ist leer");
                start(user);
            } else {
                System.out.println("Pinnwand von " + user.getUsername());
                System.out.println(pinnwand);
                System.out.println("Enter drücken um zurückzukehren");
                if (scanner.nextLine() != null) {
                    start(user);
                }
            }
        } catch (ServiceException serviceException) {
            System.out.println(serviceException.getMessage());
            start(user);
        }
    }

    public void setPersonSucheManager(PersonSucheManager personSucheManager) {
        this.personSucheManager = personSucheManager;
    }
}