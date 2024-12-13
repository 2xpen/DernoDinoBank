package menu.pinnwand;

import data.pinnwand.Pinnwand;
import data.pinnwand.PinnwandEntry;
import data.user.User;
import data.user.UserName;
import menu.ManagerBase;
import menu.Menufehlermeldungen;
import menu.anmeldung.ANMELDE_DIALOG;
import menu.konto.KontoAnsichtManager;
import service.PinnwandService;
import service.UserService;
import service.serviceexception.ServiceException;
import service.serviceexception.validateexception.ValidateBeschreibungException;
import validator.Validator;

import java.util.List;

public class PinnwandManager extends ManagerBase {

    private final KontoAnsichtManager kontoAnsichtManager;
    private final PinnwandService pinnwandService;
    private final UserService userService;
    private User user;

    public PinnwandManager(KontoAnsichtManager kontoAnsichtManager, PinnwandService pinnwandService, UserService userService) {
        this.kontoAnsichtManager = kontoAnsichtManager;
        this.pinnwandService = pinnwandService;
        this.userService = userService;
    }

    public void start(User user) {
        this.user = user;

        printHead();

        PINNWAND_MENU_OPTION.printAll();

        printFooter();

        try {
            int wahlNummer = Integer.parseInt(scanner.next());
            deciderPinnwandMenuOption(PINNWAND_MENU_OPTION.ofWahlNummer(wahlNummer));
        } catch (NumberFormatException e) {
            Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
            start(user);
        }
    }


    private void deciderPinnwandMenuOption(PINNWAND_MENU_OPTION option) {
        switch (option) {
            case PINNWAND_SUCHEN:
                pinnwandSuche();
                break;
            case PINNWAND_ANSEHEN:
                eigenePinnwandAnsehen();
                break;
            case ZURUECK:
                kontoAnsichtManager.start(user);
                break;
            case null:
                Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
                start(user);
                break;
        }

    }

    private void pinnwandSuche() {

        try {
            scanner.nextLine();
            PINNWAND_DIALOG.BESITZER_VON_PINNWAND_EINGEBEN.print();

            UserName eingegebenerName = new UserName(scanner.nextLine());

            User besitzer = userService.ermittleUserByUserName(eingegebenerName);

            Pinnwand pinnwand = pinnwandService.getPinnwand(besitzer.getUserId());

            if (pinnwand.getPinnwandentries().isEmpty()) {
                PINNWAND_DIALOG.PINNWAND_IST_LEER.print();
                PINNWAND_KOMMENTAR_MENU.printAll();

                int wahlNummer = Integer.parseInt(scanner.next());
                deciderPinnwandKommentar(PINNWAND_KOMMENTAR_MENU.ofWahlNummer(wahlNummer));

                System.out.println("Wenn du einen Kommentar schreiben möchtest Bitte 1 eingeben");
                System.out.println("Wenn du zurück möchtest bitte eine andere Zahl eingeben");

            } else {
                System.out.println("Pinnwand von " + besitzer.getUsername());
                System.out.println(pinnwand);
            }
            System.out.println("Wenn du einen Kommentar schreiben möchtest Bitte 1 eingeben");
            System.out.println("Wenn du zurück möchtest bitte eine andere Zahl eingeben");
            String inputString = scanner.nextLine();

            int input = -1;
            try {
                input = Integer.parseInt(inputString);
            } catch (NumberFormatException n) {
                System.out.println("Ungültige Eingabe, es sind nur Ganzzahlen erlaubt");
                start(user);
            }

            if (input == 1) {
                System.out.println("Bitte gebe deine Nachricht ein:");
                String message = scanner.nextLine();
                pinnwandService.schreibenAufAnderePinnwand(message, user.getUserId(), besitzer.getUserId());
                System.out.println("Pinnwand eintrag wurde erstellt");
                start(user);
            } else {
                start(user);
            }

            //todo @tom teil die methode mit man will auf eine pinnwand schreiben auf, ich will nicht in die suche zurückspringgen nur weil die einggebene message nichtz korrrekt ist, ich wills einfach nochmal versuchen oder abrrechen
        } catch (ValidateBeschreibungException e) {
            System.out.println(e.getMessage());
            pinnwandSuche();
        } catch (ServiceException serviceException) {
            System.out.println(serviceException.getMessage());
            start(user);
        }

        //

    }

    private void eigenePinnwandAnsehen() {
        try {

            Pinnwand pinnwand = pinnwandService.getPinnwand(user.getUserId());

            //falls leer dann ja ne, wenn nicht halt printen
            if (pinnwand.getPinnwandentries().isEmpty()) {
                System.out.println("Pinnwand ist leer");
                start(user);
            } else {
                System.out.println("Pinnwand von " + user.getUsername());
                System.out.println(pinnwand);

                System.out.println("Beliebiges Zeichen eingeben um zurück zu kehren");
                if (scanner.hasNext()) {
                    scanner.next();
                    start(user);
                }
            }
        } catch (ServiceException serviceException) {
            System.out.println(serviceException.getMessage());
            start(user);
        }
    }
    private void deciderPinnwandKommentar(PINNWAND_KOMMENTAR_MENU option) {
        switch (option) {
            case KOMMENTAR_SCHREIBEN:
                komentarSchreiben();
                break;
            case ZURUECK:
                start(user);
                break;
            case null:
                Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
                start(user);
                break;
        }
    }
    public void komentarSchreiben(){
        String inputString = scanner.nextLine();

        int input = -1;
        try {
            input = Integer.parseInt(inputString);
        } catch (NumberFormatException n) {
            System.out.println("Ungültige Eingabe, es sind nur Ganzzahlen erlaubt");
            start(user);
        }

        if (input == 1) {
            PINNWAND_DIALOG.PINNWAND_NACHRICHT_EINGEBEN.print();
            String message = scanner.nextLine();





            //pinnwandService.schreibenAufAnderePinnwand(message, user.getUserId(), besitzer.getUserId());






            PINNWAND_DIALOG.PINNWAND_NACHRICHT_ERSTELLT.print();
            start(user);
        } else {
            start(user);
        }
    }
}
