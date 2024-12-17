package menu.konto.transaktion;

import data.anweisungen.AbhebungsAnweisung;
import data.anweisungen.UeberweisungsAnweisung;
import data.anweisungen.UeberweisungsAnweisungParam;
import data.user.User;
import data.user.UserName;
import helper.FileHelper;
import menu.ManagerBase;
import menu.Menufehlermeldungen;
import menu.helper.CurrencyFormatter;
import menu.konto.UserLogedInManager;
import service.ImportExportService;
import service.KontoService;
import service.UserService;
import service.serviceexception.DatenbankException;
import service.serviceexception.ImportExportServiceException;
import service.serviceexception.ServiceException;
import service.ueberweisung.TransaktionsService;

import java.io.FileNotFoundException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class TransaktionsManager extends ManagerBase {

    private final UserLogedInManager userLogedInManager;
    private final KontoService kontoService;
    private final TransaktionsService transaktionsService;
    private final ImportExportService  importExportService;
    private final UserService userService;
    private User user;

    public TransaktionsManager(UserLogedInManager userLogedInManager, KontoService kontoService, TransaktionsService transaktionsService, ImportExportService importExportService, UserService userService) {
        this.userLogedInManager = userLogedInManager;
        this.kontoService = kontoService;
        this.transaktionsService = transaktionsService;
        this.importExportService = importExportService;
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
            decider(TransaktionOption.ofWahlNummer(Integer.parseInt(scanner.nextLine())));
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
                userLogedInManager.start(user);
                break;

            case null:
                Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
                start(user);
        }

    }

    private void startEinfacheUberweisung() {

        try {
           Kontostandanzeigen();
        } catch (ServiceException e) {

            System.out.println(e.getMessage());
            System.out.println("Nochmal versuchen (y)?" +
                    "\nsonst anderes Zeichen wählen");
            if (scanner.nextLine().equals("y")) {
                startEinfacheUberweisung();
            } else start(user);

        }

        try {

            System.out.println("An wen wollen Sie Geld schicken?");

            UserName name = new UserName(scanner.nextLine());

            System.out.println("Wie viel wollen sie überweisen?");
            System.out.println("\nBetrag angeben!");

            double gewaehlterBetrag = Double.parseDouble(scanner.nextLine());

            System.out.println("Beschreibung hinzufügen");

            String beschreibung = scanner.nextLine();

            UeberweisungsAnweisung anweisung = new UeberweisungsAnweisung(
                    kontoService.ermittelKontoIdByUserId(user.getUserId())
                    , kontoService.ermittelKontoIdByUserId(userService.ermittleUserByUserName(name).getUserId())
                    , beschreibung
                    , gewaehlterBetrag);


            transaktionsService.einzelUeberweisung(anweisung);
            System.out.println("Es wurden: " + CurrencyFormatter.formatCurrency(anweisung.getBetrag()) + " an " + name + " überwiesen.");
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


        System.out.println("Bitte den Pfad angeben worunter die Anweisungen liegen sollen");



        Path quellpfad = null;

        try {
            quellpfad = Path.of(scanner.nextLine());
            FileHelper.isPathAccessible(quellpfad);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Menufehlermeldungen.ERNEUT_VERSUCHEN.print();
            if(scanner.nextLine().equals("y")) {
            startMassenUberweisung();
            } else start(user);
        }

        try {
            transaktionsService.massenUeberweisung(quellpfad,kontoService.ermittelKontoIdByUserId(user.getUserId()));

        } catch (ImportExportServiceException e) {
            System.out.println(e.getMessage());
            Menufehlermeldungen.ERNEUT_VERSUCHEN.print();
            if(scanner.nextLine().equals("y")) {
                startMassenUberweisung();
            } else start(user);
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
            Menufehlermeldungen.ERNEUT_VERSUCHEN.print();
            if(scanner.nextLine().equals("y")) {
                startMassenUberweisung();
            }start(user);

        }

        System.out.println("Massenüberweisung wurde durchgeführt!");

        start(user);

    }


    public void abheben() {

        printHead();


        try {
            Kontostandanzeigen();
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
            System.out.println("Nochmal versuchen (y)?" +
                    "\nsonst anderes Zeichen wählen");
            if (scanner.nextLine().equals("y")) {
                abheben();
            } else start(user);

        }

        System.out.println("Wie viel wollen sie abheben?");
        System.out.println("\nBetrag angeben!");

        try {
            double gewaehlterBetrag = Double.parseDouble(scanner.nextLine());


            //die anweisung erstellen sollte glaub auch ein service sein
            AbhebungsAnweisung anweisung = new AbhebungsAnweisung(
                    kontoService.ermittelKontoIdByUserId(
                            user.getUserId())
                    , gewaehlterBetrag);

            transaktionsService.abheben(anweisung);
            System.out.println("Es wurden: " + CurrencyFormatter.formatCurrency(anweisung.getBetrag()) + " von ihrem Konto abgebucht");
            start(user);

        } catch (NumberFormatException e) {
            Menufehlermeldungen.BETRAG_FORMAT_FALSCH.print();
            System.out.println("Nochmal versuchen (y)?" +
                    "\nsonst anderes Zeichen wählen");
            if (scanner.nextLine().equals("y")) {
                abheben();
            } else start(user);


        } catch (ServiceException e) {
            System.out.println(e.getMessage());
            System.out.println("Nochmal versuchen (y)?" +
                    "\nsonst anderes Zeichen wählen");

            if (scanner.nextLine().equals("y")) {
                abheben();
            } else start(user);

        }


    }

    private void Kontostandanzeigen() throws ServiceException {
        System.out.println(
                "Aktueller Kontostand: " + CurrencyFormatter.formatCurrency(kontoService.kontostandErmitteln(user.getUserId())));
    }


}
