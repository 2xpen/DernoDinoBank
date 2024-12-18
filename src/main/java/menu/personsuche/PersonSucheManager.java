package menu.personsuche;

import data.user.User;
import data.user.UserName;
import menu.ManagerBase;
import menu.Menufehlermeldungen;
import menu.directMessages.MessageManager;
import menu.konto.UserLogedInManager;
import menu.pinnwand.PinnwandManager;
import service.UserService;
import service.serviceexception.ServiceException;

public class PersonSucheManager extends ManagerBase {
    private final UserLogedInManager userLogedInManager;
    private final PinnwandManager pinnwandManager;
    private final MessageManager messageManager;
    private final UserService userService;

    public PersonSucheManager(UserLogedInManager userLogedInManager, PinnwandManager pinnwandManager, MessageManager messageManager, UserService userService) {
        this.userLogedInManager = userLogedInManager;
        this.pinnwandManager = pinnwandManager;
        this.messageManager = messageManager;
        this.userService = userService;
    }

    public void selectUser(User user) {
        printHead();

        System.out.println("Bitte den Namen der zu suchenden Person angeben.");

        try {
            User gefundenerUser = userService.ermittleUserByUserName(new UserName(scanner.nextLine()));

            startWithSelectedUser(user, gefundenerUser);

        } catch (ServiceException e) {
            System.out.println(e.getMessage());
            System.out.println("Erneut versuchen? (y/n)");

            if(scanner.nextLine().equals("y")) {
                selectUser(user);
            } userLogedInManager.start(user);
        }
    }

    public void startWithSelectedUser(User selector,User selectedUser) {
        PersonSucheMenuOption.printAll(selectedUser.getUsername());

        printBitteWahlnummerWaehlenFooter();
        try {
            switch (PersonSucheMenuOption.ofWahlnummer(Integer.parseInt(scanner.nextLine()))) {
                case PINNWAND:
                    pinnwandManager.pinnwandVonUserAufrufen(selector, selectedUser);
                    break;
                case CHAT:
                    messageManager.einstiegMitPersonsuche(selector, selectedUser);
                    break;
                case ZURUECK:
                    userLogedInManager.start(selector);
                    break;
                case null:
                    System.out.println("Erneut versuchen? (y/n)");
                    if (scanner.nextLine().equals("y")) {
                        startWithSelectedUser(selector, selectedUser);
                    }
                    userLogedInManager.start(selector);
                    break;
            }
        }catch (NumberFormatException e) {
            Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
            startWithSelectedUser(selector, selectedUser);
        }
    }
}