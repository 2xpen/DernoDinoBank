package menu.directMessages;

import data.nachricht.Nachricht;
import data.user.User;
import data.user.UserName;
import menu.ManagerBase;
import menu.Menufehlermeldungen;
import menu.konto.KontoAnsichtManager;
import service.MessageService;
import service.UserService;
import service.serviceexception.ServiceException;
import service.serviceexception.validateexception.ValidateBeschreibungException;
import validator.Validator;

import java.text.SimpleDateFormat;

import java.sql.Timestamp;
import java.util.List;

public class MessageManager extends ManagerBase {
    private final KontoAnsichtManager kontoAnsichtManager;
    private final MessageService messageService;
    private final UserService userService;
    private User user;

    public MessageManager(KontoAnsichtManager kontoAnsichtManager, MessageService messageService, UserService userService) {
        this.kontoAnsichtManager = kontoAnsichtManager;
        this.messageService = messageService;
        this.userService = userService;
    }

    public void start(User user) {
        this.user = user;

        printHead();

        DIRECT_MESSAGES_MENU_OPTION.printAll();

        printFooter();

        try {
            int wahlNummer = Integer.parseInt(scanner.next());
            deciderDirectMessagesMenuOption(DIRECT_MESSAGES_MENU_OPTION.ofWahlNummer(wahlNummer));
        } catch (NumberFormatException e) {
            Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
            start(user);
        }
    }

    private void deciderDirectMessagesMenuOption(DIRECT_MESSAGES_MENU_OPTION option) {
        switch (option) {
            case DIRECT_MESSAGES_ANSEHEN:
                seeAllMessages();
                break;
            case DIRECT_MESSAGE_SENDEN:
                sendDirectMessage();
                break;
            case ZURÜCK:
                kontoAnsichtManager.start(user);
                break;
            case null:
                Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
                start(user);
                break;
        }
    }

    private void seeAllMessages() {

        try {
            List<Nachricht> allMessages = messageService.getNachrichten(user.getUserId());
            printAlleNachrichten(allMessages);
            printImportExportDecider();
        } catch (ServiceException serviceException) {
            System.out.println(serviceException.getMessage());
            start(user);
        }
    }

    private void sendDirectMessage() {
        try {
            System.out.println("""
                    Bitte gib den Namen von dem User ein,
                    zudem du eine Nachricht senden möchtest
                    """);
            UserName eingegebenerName = new UserName(scanner.next());
            scanner.nextLine();
            User empfaenger = userService.ermittleUserByUserName(eingegebenerName);

            System.out.println("""
                    Bitte gib deine Nachricht ein: 
                    """);
            String messageToSend = scanner.nextLine();
            if (messageService.sendMessage(new Timestamp(System.currentTimeMillis()), user.getUserId(), empfaenger.getUserId(), messageToSend)) {
                System.out.println("Deine Nachricht wurde erfolgreich versendet!");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
                start(user);
            } else {
                //maybe thorw iwas
                start(user);
            }
            /// hier das selbe wie bei pinnnwand, das aufnehmen des suer inputs und das absenden sollten zwie verschioenden prozesse sien, oder hatl die try anders setzenb so das der fortschritt vom user nicht einfach zurückgesetzt nur weil er iwie mal
        } catch (ValidateBeschreibungException e) {
            System.out.println(e.getMessage());
            sendDirectMessage();
        } catch (ServiceException serviceException) {
            System.out.println(serviceException.getMessage());
            start(user);
        }
    }

    public void printAlleNachrichten(List<Nachricht> allMessages) {
        try {
            StringBuilder sb = new StringBuilder();
            int counter = 1;
            sb.append("Posteingang von ").append(user.getUsername()).append("\n");
            for (Nachricht nachricht : allMessages) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd. MMMM yyyy, HH:mm 'Uhr'");
                String formattedTimestamp = formatter.format(nachricht.getDate());
                sb.append("------------------------------------------").append("\n")
                        .append("Nachricht ").append(counter).append(":\n")
                        .append("Von: ").append(userService.ermittleUserByUserId(nachricht.getSender()).getUsername()).append("\n")
                        .append("Am: ").append(formattedTimestamp).append("\n")
                        .append("Nachricht: ").append(nachricht.getMessage()).append("\n")
                        .append("------------------------------------------").append("\n");
                counter++;
            }
            System.out.println(sb);

        } catch (ServiceException serviceException) {
            System.out.println(serviceException.getMessage());
            start(user);
        }

    }

    public void printImportExportDecider() {

        // todo @TOM das thrown und catchen sieht hier so schlimm aus weil das input getten und der decider hier vermischt sind, einmal catcht man die numberformat exception, und einmal muss man catchen wenn aus der of methode ncihts rauskommt, könnte man theoretisch in einem wiederaufruf der methode fixxen, aber das sind semantisch zwei verschiedene sachen

        IMPORT_EXPORT_MENU_OPTION.printAll();

        printBitteWahlnummerWaehlenFooter();

        try {
            IMPORT_EXPORT_MENU_OPTION option = IMPORT_EXPORT_MENU_OPTION.ofWahlNummer(Integer.parseInt(scanner.next()));

            printFooter();
            switch (option) {
                case IMPORT:
                    break;
                case EXPORT:
                    break;
                case ZURUECK:
                    start(user);
                    break;
                case null:
                    Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
                    printImportExportDecider();
                    break;
            }
        } catch (NumberFormatException e) {
            Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
            printImportExportDecider();
        }

    }

}

