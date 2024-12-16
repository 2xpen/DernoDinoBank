package menu.pinnwand;

import data.pinnwand.Pinnwand;
import data.user.User;
import menu.ManagerBase;
import menu.Menufehlermeldungen;
import menu.konto.UserLogedInManager;
import menu.personsuche.PersonSucheManager;
import service.PinnwandService;
import service.serviceexception.ServiceException;
import service.serviceexception.validateexception.ValidateBeschreibungException;

public class PinnwandManager extends ManagerBase {

    private final UserLogedInManager userLogedInManager;
    private final PinnwandService pinnwandService;
    private PersonSucheManager personSucheManager;

    public PinnwandManager(UserLogedInManager userLogedInManager, PinnwandService pinnwandService) {
        this.userLogedInManager = userLogedInManager;
        this.pinnwandService = pinnwandService;
    }

    public void start(User user) {


        printHead();

        PINNWAND_MENU_OPTION.printAll();

        printFooter();

        try {
            int wahlNummer = Integer.parseInt(scanner.nextLine());
            deciderPinnwandMenuOption(PINNWAND_MENU_OPTION.ofWahlNummer(wahlNummer),user);
        } catch (NumberFormatException e) {
            Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
            start(user);
        }
    }


    private void deciderPinnwandMenuOption(PINNWAND_MENU_OPTION option,User selector) {
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

    public void pinnwandVonUserAufrufen(User selector,User selectedUser) {

        try {

            Pinnwand pinnwand = pinnwandService.getPinnwand(selectedUser.getUserId());

            if (pinnwand.getPinnwandentries().isEmpty()) {
                PINNWAND_DIALOG.PINNWAND_IST_LEER.print();

            } else {
                System.out.println("Pinnwand von " + selectedUser.getUsername());
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
                pinnwandVonUserAufrufen(selector, selectedUser);
            }
            if (input == 1) {
                aufPinnwandSchreiben(selector,selectedUser);
            } else {
                personSucheManager.startWithSelectedUser(selector, selectedUser);
            }
            //todo @tom teil die methode mit man will auf eine pinnwand schreiben auf, ich will nicht in die suche zurückspringgen nur weil die einggebene message nichtz korrrekt ist, ich wills einfach nochmal versuchen oder abrrechen
        } catch (ServiceException serviceException) {
            System.out.println(serviceException.getMessage());
            pinnwandVonUserAufrufen(selector,selectedUser);
        }
    }


    private void aufPinnwandSchreiben(User autor, User empfaenger){
        System.out.println("Bitte gebe deine Nachricht ein:");
        String message = scanner.nextLine();
        try {
            pinnwandService.schreibenAufAnderePinnwand(message, autor.getUserId() ,empfaenger.getUserId());
            System.out.println("Ihr Pinnwand eintrag wurde erstellt");
            pinnwandVonUserAufrufen(autor,empfaenger);

        } catch (ServiceException e) {
            System.out.println(e.getMessage());
            System.out.println("Erneut versuchen? (y) sonst anderes zeichen wählen");
            if(scanner.nextLine().equals("y")){
                aufPinnwandSchreiben(autor,empfaenger);
            }
            pinnwandVonUserAufrufen(autor,empfaenger);
        }
    }

    private void eigenePinnwandAnsehen(User user) {
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










