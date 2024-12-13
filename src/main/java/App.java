import menu.anmeldung.AnmeldungsManager;
import menu.directMessages.MessageManager;
import menu.konto.KontoAnsichtManager;
import menu.konto.transaktion.TransaktionsManager;
import menu.kontoauszug.KontoauszugManager;
import menu.pinnwand.PinnwandManager;
import menu.registrierung.RegistrierungManger;
import menu.startseite.StartseiteManager;
import repository.GevoRepository;
import repository.UserRepository;
import repository.directmessages.DirectMessagesRepository;
import repository.konto.KontoRepository;
import repository.pinnwand.PinnwandRepository;
import service.*;
import service.GevoService;
import service.ueberweisung.TransaktionsService;


public class App {

    /// besser wäre es quasi hier mit interfaces matroschka mäßig alle sachen austauschbar in einander zu stecken
    ///  das hier wär auch anderes lösbar, ich kann nur besser schlafen wenn man das programm konfigurierbar machen KÖNNTE
    ///  wenn man es wollte, also das man plug and play mäßig die komponenten austauschen könnte,
    ///  quasi würde hier dann eine methode sein die Startparamter entgegen nimmt welches frontend man haben will oder
    ///  oder warum auch immer andere services / repo klassen einsetzen könnte

    public static void start() {


        /// hier jetzt schichten weise sich nach und nach das backend und dann die services und dann das frontend zusammen
        ///  basteln


        UserRepository userRepository = new UserRepository();
        KontoRepository kontoRepository = new KontoRepository();
        PinnwandRepository pinnwandRepository = new PinnwandRepository();
        DirectMessagesRepository directMessagesRepository = new DirectMessagesRepository();
        GevoRepository gevoRepository = new GevoRepository();


        UserService userService = new UserService(userRepository);
        KontoService kontoService = new KontoService(kontoRepository);
        GevoService gevoService = new GevoService(gevoRepository, userService, kontoService);

        AnmeldeService anmeldeService = new AnmeldeService(userRepository);
        PinnwandService pinnwandService = new PinnwandService(pinnwandRepository, userService);
        RegistrierungService registrierungService = new RegistrierungService(userRepository, kontoRepository);
        TransaktionsService transaktionsService = new TransaktionsService(kontoRepository, gevoService);
        MessageService messageService = new MessageService(directMessagesRepository);


        StartseiteManager startseiteManager = new StartseiteManager();
        AnmeldungsManager anmeldungsManager = new AnmeldungsManager(startseiteManager, anmeldeService);
        KontoAnsichtManager kontoAnsichtManager = new KontoAnsichtManager(anmeldungsManager, kontoService);
        RegistrierungManger registrierungManger = new RegistrierungManger(startseiteManager, registrierungService);
        PinnwandManager pinnwandManager = new PinnwandManager(kontoAnsichtManager, pinnwandService, userService);
        TransaktionsManager transaktionsManager = new TransaktionsManager(kontoAnsichtManager, kontoService, transaktionsService, userService);
        MessageManager messageManager = new MessageManager(kontoAnsichtManager, messageService, userService);
        KontoauszugManager kontoauszugManager = new KontoauszugManager(kontoService, gevoService, userService, kontoAnsichtManager);

        startseiteManager.setAnmeldungsManager(anmeldungsManager);
        startseiteManager.setRegistrierungManger(registrierungManger);

        anmeldungsManager.setKontoansichtsmanager(kontoAnsichtManager);

        kontoAnsichtManager.setPinnwandManager(pinnwandManager);
        kontoAnsichtManager.setUeberweisungManager(transaktionsManager);
        kontoAnsichtManager.setMessageManager(messageManager);
        kontoAnsichtManager.setKontoauszugManager(kontoauszugManager);

        startseiteManager.start();
    }

}
