package service;

import data.KontoauszugWrapper;
import data.anweisungen.AbhebungsAnweisung;
import data.anweisungen.AnweisungBase;
import data.anweisungen.UeberweisungsAnweisung;
import data.geschaeftsvorfall.AbhebungGevo;
import data.geschaeftsvorfall.GevoZeile;
import data.geschaeftsvorfall.KontoauszugZeile;
import data.geschaeftsvorfall.UeberweisungGevo;
import data.identifier.KontoId;
import data.identifier.UserId;
import data.user.UserName;
import repository.GevoRepository;
import service.serviceexception.DatenbankException;
import service.serviceexception.ServiceException;
import service.serviceexception.validateexception.ValidateGevoException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GevoService {
    private final GevoRepository gevoRepository;
    private final UserService userService;
    private final KontoService kontoService;

    public GevoService(GevoRepository gevoRepository, UserService userService, KontoService kontoService) {
        this.gevoRepository = gevoRepository;
        this.userService = userService;
        this.kontoService = kontoService;
    }

    public KontoauszugWrapper fetchTransaktionsHistorie(UserId id) throws ServiceException {
        try {
            return demaskGevoZeile(gevoRepository.fetchGevosOfKonto(kontoService.ermittelKontoIdByUserId(id)));
        } catch (SQLException e) {
            throw new DatenbankException(DatenbankException.Message.INTERNAL_SERVER_ERROR);
        }
    }


    public void doc(AnweisungBase anweisung) throws ServiceException {
        if (anweisung instanceof AbhebungsAnweisung) {
            docAbhebungGevo(
                    new AbhebungGevo((AbhebungsAnweisung) anweisung));

        } else if (anweisung instanceof UeberweisungsAnweisung) {
            docUeberweisungGevo(
                    new UeberweisungGevo((UeberweisungsAnweisung) anweisung));

        } else {
            throw new ValidateGevoException(ValidateGevoException.Message.GEVOERSTELLEN_FEHLSCHLAG);
        }
    }


    //nur fürs testen public
    public void docUeberweisungGevo(UeberweisungGevo gevo) throws ServiceException {
        try {
            gevoRepository.createUeberweisungsGevo(gevo);
        } catch (SQLException e) {
            throw new DatenbankException(DatenbankException.Message.INTERNAL_SERVER_ERROR);
        }
    }

    //nur fürs testen public
    public void docAbhebungGevo(AbhebungGevo gevo) throws ServiceException {
        try {
            gevoRepository.createAbhebungsGevo(gevo);
        } catch (SQLException e) {
            throw new DatenbankException(DatenbankException.Message.INTERNAL_SERVER_ERROR);
        }
    }

    public UserName ermittleUserNameByKontoId(KontoId kontoId) throws ServiceException {
        return userService.ermittleUserByUserId(
                        kontoService.ermittelUserIdByKontoId(kontoId))
                .getUsername();
    }

    public KontoauszugWrapper demaskGevoZeile(List<GevoZeile> gevos) throws ServiceException {
        List<KontoauszugZeile> kontoauszugZeilen = new ArrayList<>();

        for (GevoZeile gevo : gevos) {
            UserName empfaenger = gevo.getEmpfaenger().isPresent() ? ermittleUserNameByKontoId(gevo.getEmpfaenger().get()) : null;
            UserName sender = ermittleUserNameByKontoId(gevo.getSender());
            kontoauszugZeilen.add(
                    new KontoauszugZeile(
                            gevo.getDatum()
                            , Optional.ofNullable(empfaenger)
                            , sender
                            , gevo.getBeschreibung()
                            , gevo.getBetrag()
                            , gevo.getArt()
                    )
            );
        }
        return new KontoauszugWrapper(kontoauszugZeilen);
    }
}