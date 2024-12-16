package menu.konto;

import data.user.User;
import menu.ManagerBase;
import menu.anmeldung.AnmeldungsManager;
import menu.Menufehlermeldungen;
import menu.directMessages.MessageManager;
import menu.helper.CurrencyFormatter;
import menu.konto.transaktion.TransaktionsManager;
import menu.kontoauszug.KontoauszugManager;
import menu.personsuche.PersonSucheManager;
import menu.pinnwand.PinnwandManager;
import service.KontoService;
import service.serviceexception.ServiceException;

public class UserLogedInManager extends ManagerBase {

    private final AnmeldungsManager anmeldungsManager;
    private final KontoService kontoService;
    private TransaktionsManager transaktionsManager;
    private PinnwandManager pinnwandManager;
    private MessageManager messageManager;
    private KontoauszugManager kontoauszugManager;
    private PersonSucheManager personSucheManager;
    private User user;

    public UserLogedInManager(AnmeldungsManager anmeldungsManager, KontoService kontoService) {
        super();
        this.anmeldungsManager = anmeldungsManager;
        this.kontoService = kontoService;
    }

    public void start(User user) {
        this.user = user;
        eingeloggtBildschirm();
    }

    private void eingeloggtBildschirm() {

        printHead();
        System.out.println("Herzlich Willkommen " + user.getUsername() + ", viel Spaß bei Ihrem OnlineBanking");
        //printet alle Optionen aus

        KONTO_MENU_OPTION.printAll();

        printFooter();

        try {

            int wahlNummer = Integer.parseInt(scanner.nextLine());
            auswahlDecider(KONTO_MENU_OPTION.ofWahlNummer(wahlNummer));

        } catch (NumberFormatException e) {
            Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
            eingeloggtBildschirm();
        }

    }


    private void auswahlDecider(KONTO_MENU_OPTION option) {

        switch (option) {
            case OPTION_TRANSAKTION:
                transaktionsManager.start(user);
                break;
            case OPTION_KONTOSTAND_ANSEHEN:
                kontostandAnsehen();
                break;
            case OPTION_PINNWAND:
                pinnwandManager.start(user);
                break;
            case OPTION_DIRECTMESSAGE:
                messageManager.start(user);
                break;
            case OPTION_KONTOBEWEGUNGEN:
                kontoauszugManager.start(user);
                break;
            case OPTION_PERSONSUCHE:
                personSucheManager.selectUser(user);
            case AUSLOGGEN:
                anmeldungsManager.start();
                break;
            case null:
                Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
                eingeloggtBildschirm();

        }

    }

    public void kontostandAnsehen() {

        printHead();
        try {
            System.out.println("Kontostand: " + CurrencyFormatter.formatCurrency(kontoService.kontostandErmitteln(user.getUserId())));
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
        start(user);

    }


    public UserLogedInManager setPinnwandManager(PinnwandManager pinnwandManager) {
        this.pinnwandManager = pinnwandManager;
        return this;
    }

    public UserLogedInManager setMessageManager(MessageManager messageManager) {
        this.messageManager = messageManager;
        return this;
    }

    public UserLogedInManager setUeberweisungManager(TransaktionsManager transaktionsManager) {
        this.transaktionsManager = transaktionsManager;return this;
    }

    public UserLogedInManager setKontoauszugManager(KontoauszugManager kontoauszugManager) {
        this.kontoauszugManager = kontoauszugManager;return this;
    }

    public UserLogedInManager setPersonSucheManager(PersonSucheManager personSucheManager) {
        this.personSucheManager = personSucheManager;return this;
    }

}


