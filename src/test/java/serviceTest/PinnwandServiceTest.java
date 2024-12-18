package serviceTest;

import data.identifier.UserId;
import data.pinnwand.Pinnwand;
import data.pinnwand.PinnwandEntry;
import data.pinnwand.PinnwandEntryView;
import data.user.User;
import data.user.UserName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.directmessages.DirectMessagesRepository;
import repository.pinnwand.PinnwandRepository;
import service.MessageService;
import service.PinnwandService;
import service.UserService;
import service.serviceexception.ServiceException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class PinnwandServiceTest {
    private PinnwandRepository pinnwandRepository;
    private UserService userService;
    private PinnwandService pinnwandService;

    @BeforeEach
    void setUp() {
        pinnwandRepository = mock(PinnwandRepository.class);
        userService = mock(UserService.class);
        pinnwandService = new PinnwandService(pinnwandRepository, userService);
    }

    @Test
    void testAnforderung11positiv() {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        UserId author = new UserId("123");
        UserId besitzer = new UserId("456");
        User user1 = new User(author, new UserName("Ruffy"));
        User user2 = new User(besitzer, new UserName("PinkePank"));
        User user3 = new User(new UserId(), new UserName("Zoro"));
        User user4 = new User(new UserId(), new UserName("Robin"));
        User user5 = new User(new UserId(), new UserName("Sam"));
        Pinnwand pinnwand = new Pinnwand();

        PinnwandEntryView pinnwandEntryView1 = new PinnwandEntryView(new PinnwandEntry("HUHU", besitzer, author, timestamp), user1.getUsername(), user2.getUsername());
        PinnwandEntryView pinnwandEntryView2 = new PinnwandEntryView(new PinnwandEntry("+rep", besitzer, author, timestamp), user1.getUsername(), user2.getUsername());
        PinnwandEntryView pinnwandEntryView3 = new PinnwandEntryView(new PinnwandEntry("1", besitzer, user3.getUserId(), timestamp), user1.getUsername(), user3.getUsername());
        PinnwandEntryView pinnwandEntryView4 = new PinnwandEntryView(new PinnwandEntry("JOOOHOOOHO", besitzer, user4.getUserId(), timestamp), user1.getUsername(), user4.getUsername());
        PinnwandEntryView pinnwandEntryView5 = new PinnwandEntryView(new PinnwandEntry("-rep", besitzer, user5.getUserId(), timestamp), user1.getUsername(), user5.getUsername());
        PinnwandEntryView pinnwandEntryView6 = new PinnwandEntryView(new PinnwandEntry("hacker", besitzer, author, timestamp), user1.getUsername(), user2.getUsername());
        PinnwandEntryView pinnwandEntryView7 = new PinnwandEntryView(new PinnwandEntry("looser", besitzer, author, timestamp), user1.getUsername(), user2.getUsername());

        pinnwand.add(pinnwandEntryView1);
        pinnwand.add(pinnwandEntryView2);
        pinnwand.add(pinnwandEntryView3);
        pinnwand.add(pinnwandEntryView4);
        pinnwand.add(pinnwandEntryView5);
        pinnwand.add(pinnwandEntryView6);
        pinnwand.add(pinnwandEntryView7);


        List<PinnwandEntry> correctEntries = new ArrayList<>();

        correctEntries.add(pinnwandEntryView1);
        correctEntries.add(pinnwandEntryView2);
        correctEntries.add(pinnwandEntryView6);
        correctEntries.add(pinnwandEntryView7);

        List<PinnwandEntry> filterdEntries = pinnwandService.filterafterAnforderung11(pinnwand, user2.getUserId(), user1.getUserId());

        assertEquals(correctEntries, filterdEntries);
    }

    @Test
    void testAnforderung11negativ() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        UserId author = new UserId("123");
        UserId besitzer = new UserId("456");
        User user1 = new User(author, new UserName("Ruffy"));
        User user2 = new User(besitzer, new UserName("PinkePank"));
        User user3 = new User(new UserId(), new UserName("Zoro"));
        User user4 = new User(new UserId(), new UserName("Robin"));
        User user5 = new User(new UserId(), new UserName("Sam"));
        User user6 = new User(new UserId(), new UserName("Blackbeard"));
        Pinnwand pinnwand = new Pinnwand();

        PinnwandEntryView pinnwandEntryView1 = new PinnwandEntryView(new PinnwandEntry("HUHU", besitzer, author, timestamp), user1.getUsername(), user2.getUsername());
        PinnwandEntryView pinnwandEntryView2 = new PinnwandEntryView(new PinnwandEntry("+rep", besitzer, author, timestamp), user1.getUsername(), user2.getUsername());
        PinnwandEntryView pinnwandEntryView3 = new PinnwandEntryView(new PinnwandEntry("1", besitzer, user3.getUserId(), timestamp), user1.getUsername(), user3.getUsername());
        PinnwandEntryView pinnwandEntryView4 = new PinnwandEntryView(new PinnwandEntry("JOOOHOOOHO", besitzer, user4.getUserId(), timestamp), user1.getUsername(), user4.getUsername());
        PinnwandEntryView pinnwandEntryView5 = new PinnwandEntryView(new PinnwandEntry("-rep", besitzer, user5.getUserId(), timestamp), user1.getUsername(), user5.getUsername());
        PinnwandEntryView pinnwandEntryView6 = new PinnwandEntryView(new PinnwandEntry("hacker", besitzer, author, timestamp), user1.getUsername(), user2.getUsername());
        PinnwandEntryView pinnwandEntryView7 = new PinnwandEntryView(new PinnwandEntry("looser", besitzer, author, timestamp), user1.getUsername(), user2.getUsername());

        pinnwand.add(pinnwandEntryView1);
        pinnwand.add(pinnwandEntryView2);
        pinnwand.add(pinnwandEntryView3);
        pinnwand.add(pinnwandEntryView4);
        pinnwand.add(pinnwandEntryView5);
        pinnwand.add(pinnwandEntryView6);
        pinnwand.add(pinnwandEntryView7);


        List<PinnwandEntry> correctEntries = new ArrayList<>();

        List<PinnwandEntry> filterdEntries = pinnwandService.filterafterAnforderung11(pinnwand, user6.getUserId(), user1.getUserId());

        assertEquals(correctEntries, filterdEntries);
    }

    @Test
    void testConvertPinnwandEntries() throws ServiceException {

        User user1 = new User(new UserId(), new UserName("Ruffy"));
        User user2 = new User(new UserId(), new UserName("PinkePank"));
        User user3 = new User(new UserId(), new UserName("Zoro"));
        User user4 = new User(new UserId(), new UserName("Robin"));
        User user5 = new User(new UserId(), new UserName("Sam"));

        List<PinnwandEntry> pinnwandEntries = new ArrayList<>();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        PinnwandEntry pinnwandEntry1 = new PinnwandEntry("HUHU", user1.getUserId(), user2.getUserId(), timestamp);
        PinnwandEntry pinnwandEntry2 = new PinnwandEntry("+rep", user1.getUserId(), user4.getUserId(), timestamp);
        PinnwandEntry pinnwandEntry3 = new PinnwandEntry("1", user1.getUserId(), user3.getUserId(), timestamp);
        PinnwandEntry pinnwandEntry4 = new PinnwandEntry("JOOOHOOOHO", user1.getUserId(), user5.getUserId(), timestamp);
        PinnwandEntry pinnwandEntry5 = new PinnwandEntry("-rep", user1.getUserId(), user5.getUserId(), timestamp);
        PinnwandEntry pinnwandEntry6 = new PinnwandEntry("hacker", user1.getUserId(), user2.getUserId(), timestamp);
        PinnwandEntry pinnwandEntry7 = new PinnwandEntry("looser", user1.getUserId(), user5.getUserId(), timestamp);

        pinnwandEntries.add(pinnwandEntry1);
        pinnwandEntries.add(pinnwandEntry2);
        pinnwandEntries.add(pinnwandEntry3);
        pinnwandEntries.add(pinnwandEntry4);
        pinnwandEntries.add(pinnwandEntry5);
        pinnwandEntries.add(pinnwandEntry6);
        pinnwandEntries.add(pinnwandEntry7);

        PinnwandEntryView pinnwandEntryView1 = new PinnwandEntryView(pinnwandEntry1, user1.getUsername(), user2.getUsername());
        PinnwandEntryView pinnwandEntryView2 = new PinnwandEntryView(pinnwandEntry2, user1.getUsername(), user4.getUsername());
        PinnwandEntryView pinnwandEntryView3 = new PinnwandEntryView(pinnwandEntry3, user1.getUsername(), user3.getUsername());
        PinnwandEntryView pinnwandEntryView4 = new PinnwandEntryView(pinnwandEntry4, user1.getUsername(), user5.getUsername());
        PinnwandEntryView pinnwandEntryView5 = new PinnwandEntryView(pinnwandEntry5, user1.getUsername(), user5.getUsername());
        PinnwandEntryView pinnwandEntryView6 = new PinnwandEntryView(pinnwandEntry6, user1.getUsername(), user2.getUsername());
        PinnwandEntryView pinnwandEntryView7 = new PinnwandEntryView(pinnwandEntry7, user1.getUsername(), user5.getUsername());

        Pinnwand expectedPinnwand = new Pinnwand();
        expectedPinnwand.add(pinnwandEntryView1);
        expectedPinnwand.add(pinnwandEntryView2);
        expectedPinnwand.add(pinnwandEntryView3);
        expectedPinnwand.add(pinnwandEntryView4);
        expectedPinnwand.add(pinnwandEntryView5);
        expectedPinnwand.add(pinnwandEntryView6);
        expectedPinnwand.add(pinnwandEntryView7);

        assertEquals(expectedPinnwand, pinnwandService.convertPinnwandEntriesToPinnwand(pinnwandEntries));
    }
}

