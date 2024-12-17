package service;

import data.identifier.KontoId;
import data.identifier.UserId;
import repository.konto.KontoRepository;
import service.serviceexception.DatenbankException;
import service.serviceexception.ServiceException;

import java.sql.SQLException;

public class KontoService {

    private final KontoRepository repo;


    public KontoService(KontoRepository repo) {
        this.repo = repo;
    }

    public KontoId ermittelKontoIdByUserId(UserId userId) throws DatenbankException {
        try {
            return repo.kontoIdErmittelnByUserId(userId);
        } catch (SQLException e) {
            throw new DatenbankException(DatenbankException.Message.INTERNAL_SERVER_ERROR);
        }
    }


    public double kontostandErmitteln(UserId userId) throws ServiceException {
        try {
            return repo.ladeKontoStandVonKonto(
                    repo.kontoIdErmittelnByUserId(userId));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new DatenbankException(DatenbankException.Message.INTERNAL_SERVER_ERROR) {
            };
        }

    }


    public UserId ermittelUserIdByKontoId(KontoId id) throws DatenbankException {
        try {
            return repo.ermittelUserIdByKontoId(id);
        } catch (SQLException e) {
            throw new DatenbankException(DatenbankException.Message.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean kontoExsist(KontoId id) throws DatenbankException {
        try {
            return repo.kontoExsist(id);
        } catch (SQLException e) {
            throw new DatenbankException(DatenbankException.Message.INTERNAL_SERVER_ERROR);
        }
    }



}
