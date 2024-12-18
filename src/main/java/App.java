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
    public static void start() {
        UserRepository userRepository = new UserRepository();
        KontoRepository kontoRepository = new KontoRepository();
        PinnwandRepository pinnwandRepository = new PinnwandRepository();
        DirectMessagesRepository directMessagesRepository = new DirectMessagesRepository();
        GevoRepository gevoRepository = new GevoRepository();

        UserService userService = new UserService(userRepository);
        KontoService kontoService = new KontoService(kontoRepository);
        GevoService gevoService = new GevoService(gevoRepository, userService, kontoService);

        ImportExportService importExportService =  new ImportExportService(userService,kontoService);
        AnmeldeService anmeldeService = new AnmeldeService(userRepository);
        PinnwandService pinnwandService = new PinnwandService(pinnwandRepository, userService);
        RegistrierungService registrierungService = new RegistrierungService(userRepository, kontoRepository);
        TransaktionsValidatorService transaktionsValidatorService = new TransaktionsValidatorService(kontoRepository,userService,kontoService);
        TransaktionsService transaktionsService = new TransaktionsService(kontoRepository, gevoService,importExportService, transaktionsValidatorService,kontoService,userService);
        MessageService messageService = new MessageService(directMessagesRepository,userService);

        CSV_Handler csvHandler = new CSV_Handler();

        StartseiteManager startseiteManager = new StartseiteManager();
        AnmeldungsManager anmeldungsManager = new AnmeldungsManager(startseiteManager, anmeldeService);
        UserLogedInManager userLogedInManager = new UserLogedInManager(anmeldungsManager, kontoService);
        RegistrierungManger registrierungManger = new RegistrierungManger(startseiteManager, registrierungService);
        PinnwandManager pinnwandManager = new PinnwandManager(userLogedInManager, pinnwandService,importExportService);
        TransaktionsManager transaktionsManager = new TransaktionsManager(userLogedInManager, kontoService, transaktionsService,importExportService,userService);
        MessageManager messageManager = new MessageManager(userLogedInManager, messageService, userService,importExportService);
        KontoauszugManager kontoauszugManager = new KontoauszugManager(kontoService, gevoService, userService, userLogedInManager,importExportService);
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