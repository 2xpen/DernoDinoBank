package menu.directMessages;

import data.nachricht.Nachricht;
import data.user.User;
import data.user.UserName;
import menu.ManagerBase;
import menu.Menufehlermeldungen;
import menu.konto.UserLogedInManager;
import menu.personsuche.PersonSucheManager;
import service.MessageService;
import service.UserService;
import service.serviceexception.ServiceException;
import service.serviceexception.validateexception.ValidateBeschreibungException;

import java.text.SimpleDateFormat;

import java.sql.Timestamp;
import java.util.List;

public class MessageManager extends ManagerBase {
    private final UserLogedInManager userLogedInManager;
    private final MessageService messageService;
    private final UserService userService;
    private PersonSucheManager personSucheManager;

    public MessageManager(UserLogedInManager userLogedInManager, MessageService messageService, UserService userService) {
        this.userLogedInManager = userLogedInManager;
        this.messageService = messageService;
        this.userService = userService;
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
            if(allMessages.isEmpty()) {
                System.out.println("Der Chatverlauf ist leer");
                start(user);
            }else {
                printAlleNachrichten(allMessages,user);
                printImportExportDecider(user);
            }
        } catch (ServiceException serviceException) {
            System.out.println(serviceException.getMessage());
            start(user);
        }
    }


    public void sendDirectMessage(User selector, User selectedUser) {

        try {
            System.out.println("""
                    Bitte gib deine Nachricht ein: 
                    """);
            String messageToSend = scanner.nextLine();
            if (messageService.sendMessage(new Timestamp(System.currentTimeMillis()), selector.getUserId(), selectedUser.getUserId(), messageToSend)) {
                System.out.println("Deine Nachricht wurde erfolgreich versendet!");
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
                personSucheManager.startWithSelectedUser(selector, selectedUser);
            } else {
                //maybe thorw iwas
                personSucheManager.startWithSelectedUser(selector, selectedUser);
            }
            /// hier das selbe wie bei pinnnwand, das aufnehmen des suer inputs und das absenden sollten zwie verschioenden prozesse sien, oder hatl die try anders setzenb so das der fortschritt vom user nicht einfach zurückgesetzt nur weil er iwie mal
        } catch (ServiceException serviceException) {
            System.out.println(serviceException.getMessage());
            System.out.println("Erneut versuchen? (y) sonst anderes Zeichen eingeben");
            if (scanner.nextLine().equals("y")) {
                sendDirectMessage(selectedUser, selector);
            }
        }
        personSucheManager.startWithSelectedUser(selector, selectedUser);

    }


    public void printAlleNachrichten(List<Nachricht> allMessages, User user) {
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

    public void printImportExportDecider(User user) {

        // todo @TOM das thrown und catchen sieht hier so schlimm aus weil das input getten und der decider hier vermischt sind, einmal catcht man die numberformat exception, und einmal muss man catchen wenn aus der of methode ncihts rauskommt, könnte man theoretisch in einem wiederaufruf der methode fixxen, aber das sind semantisch zwei verschiedene sachen

        IMPORT_EXPORT_MENU_OPTION.printAll();

        printBitteWahlnummerWaehlenFooter();

        try {
            IMPORT_EXPORT_MENU_OPTION option = IMPORT_EXPORT_MENU_OPTION.ofWahlNummer(Integer.parseInt(scanner.nextLine()));

            printFooter();
            switch (option) {
                case EXPORT:
                    break;
                case ZURUECK:
                    start(user);
                    break;
                case null:
                    Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
                    printImportExportDecider(user);
                    break;
            }
        } catch (NumberFormatException e) {
            Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
            printImportExportDecider(user);
        }

    }




    public void setPersonSucheManager(PersonSucheManager personSucheManager) {
        this.personSucheManager = personSucheManager;
    }

}



