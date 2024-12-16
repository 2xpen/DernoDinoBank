import csv.CSV_Handler;
import menu.anmeldung.AnmeldungsManager;
import menu.directMessages.MessageManager;
import menu.konto.UserLogedInManager;
import menu.konto.transaktion.TransaktionsManager;
import menu.kontoauszug.KontoauszugManager;
import menu.personsuche.PersonSucheManager;
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
import service.ueberweisung.TransaktionsValidatorService;


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

        ImportExportService importExportService =  new ImportExportService(userService);
        AnmeldeService anmeldeService = new AnmeldeService(userRepository);
        PinnwandService pinnwandService = new PinnwandService(pinnwandRepository, userService);
        RegistrierungService registrierungService = new RegistrierungService(userRepository, kontoRepository);
        TransaktionsValidatorService transaktionsValidatorService = new TransaktionsValidatorService(kontoRepository);
        TransaktionsService transaktionsService = new TransaktionsService(kontoRepository, gevoService,importExportService, transaktionsValidatorService);
        MessageService messageService = new MessageService(directMessagesRepository);


        CSV_Handler csvHandler = new CSV_Handler();


        StartseiteManager startseiteManager = new StartseiteManager();
        AnmeldungsManager anmeldungsManager = new AnmeldungsManager(startseiteManager, anmeldeService);
        UserLogedInManager userLogedInManager = new UserLogedInManager(anmeldungsManager, kontoService);
        RegistrierungManger registrierungManger = new RegistrierungManger(startseiteManager, registrierungService);
        PinnwandManager pinnwandManager = new PinnwandManager(userLogedInManager, pinnwandService);
        TransaktionsManager transaktionsManager = new TransaktionsManager(userLogedInManager, kontoService, transaktionsService, userService);
        MessageManager messageManager = new MessageManager(userLogedInManager, messageService, userService, csvHandler);
        KontoauszugManager kontoauszugManager = new KontoauszugManager(kontoService, gevoService, userService, userLogedInManager);
        PersonSucheManager personSucheManager = new PersonSucheManager(userLogedInManager,pinnwandManager,messageManager,userService);


        startseiteManager.setAnmeldungsManager(anmeldungsManager);
        startseiteManager.setRegistrierungManger(registrierungManger);

        anmeldungsManager.setKontoansichtsmanager(userLogedInManager);

        userLogedInManager.setPinnwandManager(pinnwandManager);
        userLogedInManager.setUeberweisungManager(transaktionsManager);
        userLogedInManager.setMessageManager(messageManager);
        userLogedInManager.setKontoauszugManager(kontoauszugManager);
        userLogedInManager.setPersonSucheManager(personSucheManager);

        messageManager.setPersonSucheManager(personSucheManager);
        pinnwandManager.setPersonSucheManager(personSucheManager);


        startseiteManager.start();
    }

}
