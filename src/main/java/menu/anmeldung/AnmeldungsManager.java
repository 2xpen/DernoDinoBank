package menu.anmeldung;


import data.user.UserName;
import data.user.Passwort;
import menu.ManagerBase;
import menu.startseite.StartseiteManager;
import menu.Menufehlermeldungen;
import menu.konto.KontoAnsichtManager;
import service.AnmeldeService;
import service.serviceexception.ServiceException;


public class AnmeldungsManager extends ManagerBase {

    private final AnmeldeService anmeldeService;
    private final StartseiteManager startseiteManager;
    private KontoAnsichtManager kontoAnsichtManager;

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
            int wahlNummer = Integer.parseInt(scanner.next());
            deciderAnmeldeMenuOption(ANMELDE_MENU_OPTION.ofWahlNummer(wahlNummer));
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
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
            UserName benutzername = new UserName(scanner.next());

            ANMELDE_DIALOG.PASSWORT_EINGEBEN.print();
            Passwort password = new Passwort(scanner.next());

            kontoAnsichtManager.start(anmeldeService.anmelden(

                    benutzername,
                    password));

        } catch (ServiceException sf) {
            System.out.println(sf.getMessage());
            start();
        }


    }


    public void setKontoansichtsmanager(KontoAnsichtManager k) {
        this.kontoAnsichtManager = k;
    }

    // GENAU DAS HIER GEHT IN DIE SERVICE STRUKTUR ÜBER, DIE MANAGER-KLASSEN SIND NUR FÜR DEN DIALOG MIT DEM NUTZER ZUSTÄNDIG
//

    //ersetzt durch anmeldeService.anmelden();

//    public User userlogin(String benutzername, String password){
//        return new User(new UserId(),benutzername,password,new KontoId());
//    }


}
