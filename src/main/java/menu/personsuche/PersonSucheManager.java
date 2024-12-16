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


    public void start(User user) {
        printHead();

        System.out.println("Bitte den Namen der zu suchenden Person angeben");

        try {

            User gefundenerUser = userService.ermittleUserByUserName(new UserName(scanner.nextLine()));

            PersonSucheMenuOption.printAll(gefundenerUser.getUsername());

            printBitteWahlnummerWaehlenFooter();

            switch (PersonSucheMenuOption.ofWahlnummer(Integer.parseInt(scanner.nextLine()))){
                case PINNWAND : pinnwandManager.pinnwandVonUserAufrufen(user,gefundenerUser);
                break;
                case CHAT : messageManager.start(user);
                break;
                case ZURUECK : userLogedInManager.start(user);
                break;
                case null:
                    System.out.println("Erneut versuchen? (y) sonst anderes Zeichen wählen");
                    if(scanner.nextLine().equals("y")){
                        start(user);
                    }userLogedInManager.start(user);
            }

        } catch (ServiceException | NumberFormatException e) {
            if(e.getClass() == NumberFormatException.class){
                Menufehlermeldungen.WAHLNUMMER_NICHT_KORREKT.print();
            }else{System.out.println(e.getMessage());}


            System.out.println("nochmal versuchen?(y)");

            if(scanner.nextLine().equals("y")) {
                start(user);
            }else
                userLogedInManager.start(user);
        }








    }





}
