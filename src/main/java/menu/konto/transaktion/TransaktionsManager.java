package menu.konto.transaktion;

import data.anweisungen.AbhebungsAnweisung;
import data.anweisungen.UeberweisungsAnweisung;
import data.user.User;
import data.user.UserName;
import menu.ManagerBase;
import menu.Menufehlermeldungen;
import menu.konto.KontoAnsichtManager;
import service.KontoService;
import service.UserService;
import service.serviceexception.ServiceException;
import service.ueberweisung.TransaktionsService;

public class TransaktionsManager extends ManagerBase {

    private final KontoAnsichtManager kontoAnsichtManager;
    private final KontoService kontoService;
    private final TransaktionsService transaktionsService;
    private final UserService userService;
    private User user;

    public TransaktionsManager(KontoAnsichtManager kontoAnsichtManager, KontoService kontoService, TransaktionsService transaktionsService, UserService userService) {
        this.kontoAnsichtManager = kontoAnsichtManager;
        this.kontoService = kontoService;
        this.transaktionsService = transaktionsService;
        this.userService = userService;
    }


    public void start(User user) {
        this.user = user;

        printHead();

        for (TransaktionOption option : TransaktionOption.values()) {
            option.print();
        }

        printBitteWahlnummerWaehlenFooter();

        try {
            decider(TransaktionOption.ofWahlNummer(Integer.parseInt(scanner.next())));
        } catch (NumberFormatException e) {
            Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
            start(user);
        }

    }


    private void decider(TransaktionOption option) {

        switch (option) {

            case GELD_ABHEBEN:
                abheben();
                break;
            case EINFACHE_UEBERWEISUNG:
                startEinfacheUberweisung();
                break;
            case MASSEN_UBERWEISUNG:
                startMassenUberweisung();
                break;
            case Zurueck:
                kontoAnsichtManager.start(user);
                break;

            case null:
                Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
                start(user);
        }


    }

    private void startEinfacheUberweisung() {

        try {
            System.out.println(
                    "Aktueller Kontostand:" + kontoService.kontostandErmitteln(user.getUserId()) + "\n");

        } catch (ServiceException e) {

            System.out.println(e.getMessage());
            System.out.println("Nochmal versuchen (y)?" +
                    "\nsonst anderes Zeichen wählen");
            if (scanner.next().equals("y")) {
                startEinfacheUberweisung();
            } else start(user);

        }

        try {

            System.out.println("An wen wolle sie geld schicken?");

            UserName name = new UserName(scanner.next());

            System.out.println("Wie viel wollen sie überweisen?");
            System.out.println("\nBetrag angeben!");

            double gewaehlterBetrag = Double.parseDouble(scanner.next());

            System.out.println("Beschreibung hinzufügen");

            scanner.next();
            String beschreibung = scanner.nextLine();

            UeberweisungsAnweisung anweisung = new UeberweisungsAnweisung(
                    kontoService.ermittelKontoIdByUserId(user.getUserId())
                    , kontoService.ermittelKontoIdByUserId(userService.ermittleUserByUserName(name).getUserId())
                    , beschreibung
                    , gewaehlterBetrag);


            transaktionsService.einzelUeberweisung(anweisung);
            System.out.println("Es wurden: " + anweisung.getBetrag() + "an " + name + " ueberwiesen.");
            start(user);

        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            Menufehlermeldungen.BETRAG_FORMAT_FALSCH.print();
            System.out.println("Nochmal versuchen (y)?" +
                    "\nsonst anderes Zeichen wählen");
            if (scanner.next().equals("y")) {
                startEinfacheUberweisung();
            } else start(user);


        } catch (ServiceException e) {
            System.out.println(e.getMessage());
            System.out.println("Nochmal versuchen (y)?" +
                    "\nsonst anderes Zeichen wählen");

            if (scanner.next().equals("y")) {
                startEinfacheUberweisung();
            } else start(user);

        }

        start(user);
    }


    private void startMassenUberweisung() {

    }


    public void abheben() {

        printHead();


        try {
            System.out.println(
                    "Aktueller Kontostand:" + kontoService.kontostandErmitteln(user.getUserId()) + "\n");

        } catch (ServiceException e) {
            System.out.println(e.getMessage());
            System.out.println("Nochmal versuchen (y)?" +
                    "\nsonst anderes Zeichen wählen");
            if (scanner.next().equals("y")) {
                abheben();
            } else start(user);

        }

        System.out.println("Wie viel wollen sie abheben?");
        System.out.println("\nBetrag angeben!");

        try {
            double gewaehlterBetrag = Double.parseDouble(scanner.next());


            //die anweisung erstellen sollte glaub auch ein service sein
            AbhebungsAnweisung anweisung = new AbhebungsAnweisung(
                    kontoService.ermittelKontoIdByUserId(
                            user.getUserId())
                    , gewaehlterBetrag);

            transaktionsService.abheben(anweisung);
            System.out.println("Es wurden: " + anweisung.getBetrag() + "€ von ihrem Kontoabgebucht");
            start(user);

        } catch (NumberFormatException e) {
            Menufehlermeldungen.BETRAG_FORMAT_FALSCH.print();
            System.out.println("Nochmal versuchen (y)?" +
                    "\nsonst anderes Zeichen wählen");
            if (scanner.next().equals("y")) {
                abheben();
            } else start(user);


        } catch (ServiceException e) {
            System.out.println(e.getMessage());
            System.out.println("Nochmal versuchen (y)?" +
                    "\nsonst anderes Zeichen wählen");

            if (scanner.next().equals("y")) {
                abheben();
            } else start(user);

        }


    }


}
