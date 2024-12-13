package menu.kontoauszug;

import data.user.User;
import menu.ManagerBase;
import menu.konto.KontoAnsichtManager;
import service.GevoService;
import service.KontoService;
import service.UserService;
import service.serviceexception.ServiceException;

public class KontoauszugManager extends ManagerBase {

    private final KontoService kontoService;
    private final GevoService gevoService;
    private final UserService userService;
    private final KontoAnsichtManager kontoAnsichtManager;
    private User user;

    public KontoauszugManager(KontoService kontoService, GevoService gevoService, UserService userService, KontoAnsichtManager kontoAnsichtManager) {
        this.kontoService = kontoService;
        this.gevoService = gevoService;
        this.userService = userService;
        this.kontoAnsichtManager = kontoAnsichtManager;
    }

    public void start(User user) {
        this.user = user;
        uebersichtTransaktionen();
    }

    private void uebersichtTransaktionen() {
        printHead();

        printAllTransaktions();

        kontoAnsichtManager.start(user);
    }


    public void printAllTransaktions() {

        try {
            System.out.println(gevoService.fetchTransaktionsHistorie(user.getUserId()).toString());
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }

    }


}