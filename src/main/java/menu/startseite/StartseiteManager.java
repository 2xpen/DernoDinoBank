package menu.startseite;

import data.user.User;
import menu.ManagerBase;
import menu.helper.ASCI_ART;
import menu.registrierung.RegistrierungManger;
import menu.anmeldung.AnmeldungsManager;
import menu.Menufehlermeldungen;

public class StartseiteManager extends ManagerBase {

    private AnmeldungsManager anmeldungsManager;
    private RegistrierungManger registrierungManger;

    public StartseiteManager() {
    }

    public void start() {

        printHead();
        System.out.println("Willkommen bei Ihrem OnlineBanking, wie können wir Ihnen helfen?");

        STARTSEITE_MENU_OPTION.printAll();

        printFooter();

        try {
            int wahlNummer = Integer.parseInt(scanner.nextLine());
            deciderStartseiteMenuOption(STARTSEITE_MENU_OPTION.ofWahlNummer(wahlNummer));

        } catch (NumberFormatException e) {
            Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
            start();
        }

    }


    private void deciderStartseiteMenuOption(STARTSEITE_MENU_OPTION option) {


        switch (option) {
            case ANMELDEN:
                anmeldungsManager.start();
                break;
            case REGISTRIEREN:
                registrierungManger.start();
                break;
            case PROGRAMM_SCHLIESSEN:
                programmSchliessen();
            case null:
                Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
                start();
        }


    }


    public void programmSchliessen() {

        try {

            ASCI_ART.PIKACHU.print();

            Thread.sleep(3000);
            ASCI_ART.FEUERZEUG.print();

            Thread.sleep(5000);

            ASCI_ART.ZIGARETTE.print();

            System.exit(0);
        } catch (InterruptedException e) {
            System.exit(0);
        }

    }

    public void setAnmeldungsManager(AnmeldungsManager anmeldungsManager) {
        this.anmeldungsManager = anmeldungsManager;
    }

    public void setRegistrierungManger(RegistrierungManger registrierungManger) {
        this.registrierungManger = registrierungManger;
    }

}


