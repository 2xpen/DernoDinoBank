package menu.directMessages;

import data.nachricht.Nachricht;
import data.user.User;
import helper.FileHelper;
import menu.ManagerBase;
import menu.Menufehlermeldungen;
import menu.konto.UserLogedInManager;
import menu.personsuche.PersonSucheManager;
import service.ImportExportService;
import service.MessageService;
import service.UserService;
import service.serviceexception.ServiceException;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageManager extends ManagerBase {
    private final UserLogedInManager userLogedInManager;
    private final UserService userService;
    private final MessageService messageService;
    private final ImportExportService importExportService;
    private PersonSucheManager personSucheManager;

    public MessageManager(UserLogedInManager userLogedInManager, MessageService messageService, UserService userService, ImportExportService importExportService) {
        this.userLogedInManager = userLogedInManager;
        this.messageService = messageService;
        this.userService = userService;
        this.importExportService = importExportService;
    }

    public void start(User user) {
        printHead();

        DIRECT_MESSAGES_MENU_OPTION.printAll();

        printFooter();

        try {
            int wahlNummer = Integer.parseInt(scanner.nextLine());
            deciderDirectMessagesMenuOption(DIRECT_MESSAGES_MENU_OPTION.ofWahlNummer(wahlNummer), user);
        } catch (NumberFormatException e) {
            Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
            start(user);
        }
    }

    private void deciderDirectMessagesMenuOption(DIRECT_MESSAGES_MENU_OPTION option, User user) {
        switch (option) {
            case DIRECT_MESSAGES_ANSEHEN:
                seeAllMessages(user);
                break;
            case ZURÜCK:
                userLogedInManager.start(user);
                break;
            case null:
                Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
                start(user);
                break;
        }
    }


    private void seeAllMessages(User user) {
        try {
            List<Nachricht> allMessages = messageService.getNachrichten(user.getUserId());
            if (allMessages.isEmpty()) {
                System.out.println("Der Chatverlauf ist leer");
                start(user);
            } else {
                printAlleNachrichten(allMessages, user);
                printImportExportDecider(user, allMessages);
            }
        } catch (ServiceException serviceException) {
            System.out.println(serviceException.getMessage());
            start(user);
        }
    }

    public void einstiegMitPersonsuche(User selector, User selectedUser) {
        System.out.println("1: Konversation ansehen");
        System.out.println("2: Nachricht senden");
        System.out.println("0: Zurück");

        try {

            int wahlnummer = Integer.parseInt(scanner.nextLine());
            switch (wahlnummer) {
                case 1:
                    printConvo(selector, selectedUser);
                    einstiegMitPersonsuche(selector, selectedUser);
                    break;
                case 2:
                    sendMessage(selector, selectedUser);
                case 0:
                    personSucheManager.startWithSelectedUser(selector, selectedUser);
            }

        } catch (NumberFormatException e) {
            Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
            einstiegMitPersonsuche(selector, selectedUser);
        }

    }

    public void sendMessage(User selector, User selectedUser) {

        try {
            System.out.println("""
                    Bitte Nachricht eingeben: 
                    """);
            String messageToSend = scanner.nextLine();
            if (messageService.sendMessage(new Timestamp(System.currentTimeMillis()), selector.getUserId(), selectedUser.getUserId(), messageToSend)) {
                System.out.println("Deine Nachricht wurde erfolgreich versendet");
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
                personSucheManager.startWithSelectedUser(selector, selectedUser);
            } else {
                personSucheManager.startWithSelectedUser(selector, selectedUser);
            }
        } catch (ServiceException serviceException) {
            System.out.println(serviceException.getMessage());
            System.out.println("Erneut versuchen? (y/n)");
            if (scanner.nextLine().equals("y")) {
                sendMessage(selector, selectedUser);
            }
        }
        personSucheManager.startWithSelectedUser(selector, selectedUser);
    }

    private void printConvo(User selector, User selectedUser) {
        try {
            List<Nachricht> convo = messageService.getConvo(selector, selectedUser);
            try {
                StringBuilder sb = new StringBuilder();
                int counter = 1;
                sb.append("Konversation mit ").append(selectedUser.getUsername()).append("\n");

                printNachrichtenListe(convo, sb, counter);

                IMPORT_EXPORT_DIRECTMESSAGE_MENU_OPTION.printAll();

                try {
                    switch (IMPORT_EXPORT_DIRECTMESSAGE_MENU_OPTION.ofWahlNummer(Integer.parseInt(scanner.nextLine()))) {
                        case EXPORT:
                            exportPersonsucheAnsicht(convo, selector, selectedUser);
                            printNachrichtenListe(convo, sb, counter);
                            break;
                        case ZURUECK:
                            einstiegMitPersonsuche(selector, selectedUser);
                        case null, default:
                            throw new NumberFormatException();
                    }

                } catch (NumberFormatException e) {
                    Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
                    printConvo(selector, selectedUser);
                }

            } catch (ServiceException serviceException) {
                System.out.println(serviceException.getMessage());
                sendMessage(selector, selectedUser);
            }

        } catch (ServiceException serviceException) {
            System.out.println(serviceException.getMessage());
            System.out.println("Erneut versuchen? (y/n)");
            if (scanner.nextLine().equals("y")) {
                printConvo(selector, selectedUser);
            }
            sendMessage(selector, selectedUser);
        }
    }

    private void printNachrichtenListe(List<Nachricht> convo, StringBuilder sb, int counter) throws ServiceException {
        for (Nachricht nachricht : convo) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd. MMMM yyyy, HH:mm 'Uhr'");
            String formattedTimestamp = formatter.format(nachricht.getDate());
            sb.append("------------------------------------------").append("\n")
                    .append("Nachricht ").append(counter).append(":\n")
                    .append("An: ").append(userService.ermittleUserByUserId(nachricht.getEmpfaenger()).getUsername()).append("\n")
                    .append("Von: ").append(userService.ermittleUserByUserId(nachricht.getSender()).getUsername()).append("\n")
                    .append("Am: ").append(formattedTimestamp).append("\n")
                    .append("Nachricht: ").append(nachricht.getMessage()).append("\n")
                    .append("------------------------------------------").append("\n");
            counter++;
        }
        System.out.println(sb);
    }

    public void printAlleNachrichten(List<Nachricht> allMessages, User user) {
        try {
            StringBuilder sb = new StringBuilder();
            int counter = 1;
            sb.append("Posteingang von ").append(user.getUsername()).append("\n");
            printNachrichtenListe(allMessages, sb, counter);

        } catch (ServiceException serviceException) {
            System.out.println(serviceException.getMessage());
            start(user);
        }

    }

    public void printImportExportDecider(User user, List<Nachricht> nachrichten) {
        IMPORT_EXPORT_DIRECTMESSAGE_MENU_OPTION.printAll();

        printBitteWahlnummerWaehlenFooter();

        try {
            IMPORT_EXPORT_DIRECTMESSAGE_MENU_OPTION option = IMPORT_EXPORT_DIRECTMESSAGE_MENU_OPTION.ofWahlNummer(Integer.parseInt(scanner.nextLine()));

            printFooter();
            switch (option) {
                case EXPORT:
                    exportKontoAnsicht(nachrichten, user);
                    printImportExportDecider(user, nachrichten);
                    break;
                case ZURUECK:
                    start(user);
                    break;
                case null:
                    Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
                    printImportExportDecider(user, nachrichten);
                    break;
            }
        } catch (NumberFormatException e) {
            Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
            printImportExportDecider(user, nachrichten);
        }
    }

    private void exportKontoAnsicht(List<Nachricht> nachrichten, User user) {
        System.out.println("1: Alle nachrichten exportieren");
        System.out.println("2: Nachrichten auswählen zum exportieren");
        System.out.println("0: Zurück");

        int wahlSymbol = 0;

        try {
            wahlSymbol = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
            exportKontoAnsicht(nachrichten, user);
        }

        if (wahlSymbol == 0) {
            printImportExportDecider(user, nachrichten);
        }

        System.out.println("Bitte den Zielpfad angeben");
        Path zielPfad = Path.of(scanner.nextLine());

        try {
            FileHelper.isPathAccessible(zielPfad);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            exportKontoAnsicht(nachrichten, user);
        }

        switch (wahlSymbol) {
            case 1:
                try {
                    importExportService.exportDirectMessages(nachrichten, zielPfad);
                    System.out.println("Es wurde ein Protokoll unter: " + zielPfad + " abgelegt");
                    exportKontoAnsicht(nachrichten, user);
                } catch (ServiceException e) {
                    System.out.println(e.getMessage());
                    exportKontoAnsicht(nachrichten, user);
                }
                break;
            case 2:
                try {
                    importExportService.exportDirectMessages(selectMessages(nachrichten),
                            zielPfad);
                    System.out.println("Es wurde ein Protokoll unter: " + zielPfad + " abgelegt");
                    exportKontoAnsicht(nachrichten, user);
                } catch (ServiceException e) {
                    System.out.println(e.getMessage());
                    exportKontoAnsicht(nachrichten, user);
                }
                break;
            default:
                Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();

                System.out.println("Erneut Versuchen? (y/n)");

                if (scanner.nextLine().equals("y")) {
                    exportKontoAnsicht(nachrichten, user);
                } else {
                    printImportExportDecider(user, nachrichten);
                }
        }
    }

    private void exportPersonsucheAnsicht(List<Nachricht> nachrichten, User selector, User selectedUser) throws ServiceException {
        System.out.println("1: Alle nachrichten exportieren");
        System.out.println("2: Nachrichten auswählen zum exportieren");
        System.out.println("0: Zurück");

        int wahlSymbol = 0;

        try {
            wahlSymbol = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
            exportPersonsucheAnsicht(nachrichten, selector, selectedUser);
        }

        if (wahlSymbol == 0) {
            printConvo(selector, selectedUser);
        }

        System.out.println("Bitte den Zielpfad angeben");
        Path zielPfad = Path.of(scanner.nextLine());

        try {
            FileHelper.isPathAccessible(zielPfad);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            exportPersonsucheAnsicht(nachrichten, selector, selectedUser);
        }

        switch (wahlSymbol) {
            case 1:
                try {
                    importExportService.exportDirectMessages(nachrichten, zielPfad);
                    System.out.println("Es wurde ein Protokoll unter: " + zielPfad + " abgelegt");
                    exportPersonsucheAnsicht(nachrichten, selector, selectedUser);
                } catch (ServiceException e) {
                    System.out.println(e.getMessage());
                    exportPersonsucheAnsicht(nachrichten, selector, selectedUser);
                }
                break;
            case 2:
                try {
                    importExportService.exportDirectMessages(selectMessages(nachrichten),
                            zielPfad);
                    System.out.println("Es wurde ein Protokoll unter: " + zielPfad + " abgelegt");
                    exportPersonsucheAnsicht(nachrichten, selector, selectedUser);
                } catch (ServiceException e) {
                    System.out.println(e.getMessage());
                    exportPersonsucheAnsicht(nachrichten, selector, selectedUser);
                }
                break;
            default:
                Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();

                System.out.println("Erneut Versuchen? (y/n)");

                if (scanner.nextLine().equals("y")) {
                    exportPersonsucheAnsicht(nachrichten, selector, selectedUser);
                } else {
                    einstiegMitPersonsuche(selector, selectedUser);
                }
        }
    }

    private List<Nachricht> selectMessages(List<Nachricht> quellList) {
        System.out.println("Bitte die Nummern aller gewünschten Nachrichten in folgendem Format \"1,2,3\" angeben.");
        String gewaehlteNummern = scanner.nextLine();
        List<Integer> selectedIndex = new ArrayList<>();
        try {
            selectedIndex = Arrays.stream(gewaehlteNummern.split(",")).toList().stream().map(Integer::parseInt).toList();
        } catch (NumberFormatException e) {
            System.out.println("Format nicht korrekt");
            System.out.println("Erneut versuchen? (y/n)");
            if (scanner.nextLine().equals("y")) {
                selectMessages(quellList);
            }
        }
        List<Nachricht> selectedMessages = new ArrayList<>();
        for (int index : selectedIndex) {
            try {
                selectedMessages.add(quellList.get(index - 1));
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Die Nachricht mit der Nummer: " + index + " gibt es nicht");
                System.out.println("Erneut versuchen? (y/n)");
                if (scanner.nextLine().equals("y")) {
                    selectMessages(quellList);
                }
            }
        }
        return selectedMessages;
    }

    public void setPersonSucheManager(PersonSucheManager personSucheManager) {
        this.personSucheManager = personSucheManager;
    }
}