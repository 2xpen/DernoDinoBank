package serviceTest;

import data.anweisungen.AbhebungsAnweisung;
import data.anweisungen.AnweisungBase;
import data.anweisungen.UeberweisungsAnweisung;
import data.geschaeftsvorfall.GevoArt;
import data.geschaeftsvorfall.GevoZeile;
import data.geschaeftsvorfall.KontoauszugZeile;
import data.identifier.KontoId;
import data.identifier.UserId;
import data.user.User;
import data.user.UserName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import repository.GevoRepository;
import service.GevoService;
import service.KontoService;
import service.UserService;
import service.serviceexception.ServiceException;
import service.serviceexception.validateexception.ValidateGevoException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class GevoServiceTest {

    GevoService gevoService;
    private GevoRepository gevoRepository;
    private UserService userService;
    private KontoService kontoService;

    @BeforeEach
    void setUp() {
        gevoRepository = mock(GevoRepository.class);
        userService = mock(UserService.class);
        kontoService = mock(KontoService.class);

        gevoService = Mockito.spy(new GevoService(gevoRepository, userService, kontoService));
    }

    @Test
    void testDemaskGevoZeile() throws ServiceException {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());


        UserId userId1 = new UserId("1");
        UserId userId2 = new UserId("2");
        UserId userId3 = new UserId("3");


        UserName userName1 = new UserName("user1");
        UserName userName2 = new UserName("user2");
        UserName userName3 = new UserName("user3");


        User user1 = new User(userId1, userName1);
        User user2 = new User(userId2, userName2);
        User user3 = new User(userId3, userName3);

        KontoId kontoId1 = new KontoId("id1");
        KontoId kontoId2 = new KontoId("id2");
        KontoId kontoId3 = new KontoId("id3");


        when(kontoService.ermittelUserIdByKontoId(kontoId1)).thenReturn(user1.getUserId());
        when(kontoService.ermittelUserIdByKontoId(kontoId2)).thenReturn(user2.getUserId());
        when(kontoService.ermittelUserIdByKontoId(kontoId3)).thenReturn(user3.getUserId());

        when(userService.ermittleUserByUserId(userId1)).thenReturn(new User(userId1, userName1));
        when(userService.ermittleUserByUserId(userId2)).thenReturn(new User(userId2, userName2));
        when(userService.ermittleUserByUserId(userId3)).thenReturn(new User(userId3, userName3));


        List<GevoZeile> gevoZeilen = new ArrayList<>(List.of(
                new GevoZeile(
                        timestamp
                        , kontoId2
                        , kontoId1
                        , "von 1 zu 2"
                        , 23d
                        , GevoArt.UEBERWEISUNG)

                , new GevoZeile(
                        timestamp
                        , null
                        , kontoId1
                        , "das war ne abhebung in Bielefeld"
                        , 23d
                        , GevoArt.ABHEBUNG)

                , new GevoZeile(
                        timestamp
                        , kontoId3
                        , kontoId1
                        , "von 1 zu 3"
                        , 23d
                        , GevoArt.UEBERWEISUNG)

                , new GevoZeile(
                        timestamp
                        , kontoId1
                        , kontoId3
                        , "von 3 zu 1"
                        , 76d
                        , GevoArt.UEBERWEISUNG)


        ));


        List<KontoauszugZeile> kontoauszugZeilenExcpeted = new ArrayList<>(List.of(
                new KontoauszugZeile(
                        timestamp
                        , Optional.of(userName2)
                        , userName1, "von 1 zu 2"
                        , 23d
                        , GevoArt.UEBERWEISUNG)

                , new KontoauszugZeile(
                        timestamp
                        , Optional.ofNullable(null)
                        , userName1
                        , "das war ne abhebung in Bielefeld"
                        , 23d
                        , GevoArt.ABHEBUNG)

                , new KontoauszugZeile(
                        timestamp
                        , Optional.of(userName3)
                        , userName1
                        , "von 1 zu 3"
                        , 23d
                        , GevoArt.UEBERWEISUNG)

                , new KontoauszugZeile(
                        timestamp
                        , Optional.of(userName1)
                        , userName3
                        , "von 3 zu 1"
                        , 76d
                        , GevoArt.UEBERWEISUNG)
        ));

        assertEquals(

                kontoauszugZeilenExcpeted.toString(),

                gevoService
                        .demaskGevoZeile(gevoZeilen)
                        .getKontauszugZeile().toString());
    }


    @Test
    void testDocWithAbhebungsAnweisung() throws Exception {
        AbhebungsAnweisung anweisung = mock(AbhebungsAnweisung.class);

        gevoService.doc(anweisung);

        verify(gevoService, times(1)).docAbhebungGevo(any()); // Spy allows private method verification
        verify(gevoService, never()).docUeberweisungGevo(any());
    }

    @Test
    void testDocWithUeberweisungsAnweisung() throws Exception {
        UeberweisungsAnweisung anweisung = mock(UeberweisungsAnweisung.class);

        gevoService.doc(anweisung);

        verify(gevoService, times(1)).docUeberweisungGevo(any());
        verify(gevoService, never()).docAbhebungGevo(any());
    }

    @Test
    void testDocWithInvalidAnweisung() {
        AnweisungBase invalidAnweisung = mock(AnweisungBase.class);

        assertThrows(ValidateGevoException.class, () -> gevoService.doc(invalidAnweisung));
    }


}
