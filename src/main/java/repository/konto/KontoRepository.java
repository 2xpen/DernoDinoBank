package repository.konto;

import data.anweisungen.AbhebungsAnweisung;
import data.identifier.KontoId;
import data.identifier.UserId;
import data.konto.Konto;
import data.anweisungen.UeberweisungsAnweisung;
import repository.dbConnection.DataBaseConnection;
import service.serviceexception.DatenbankException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class KontoRepository {


    /// TODO TODO TODO @sezari was macht mehr Sinn,
    /// immer nur die Benötigten daten eines konto zu ermitteln oder das gesamte kontozurück zugeben
    /// ,aus konsistenzgründen wär doch das nachladen der einzelnen metdaten sinnvoller
    ///  ich wäre dafür, der service sollte nicht mit einem kompletten konto handhaben ig

    public boolean kontoExsist(KontoId id) throws SQLException {

        Connection conn = DataBaseConnection.getInstance();

        var insertKonto = """
                
                    SELECT * FROM konto WHERE konto_id = ?
                
                """;

        var stmt = conn.prepareStatement(insertKonto);
        stmt.setString(1, id.toString());

        ResultSet resultSet = stmt.executeQuery();

        return resultSet.next();

    }


    public void createKonto(Konto konto) throws SQLException {

        Connection conn = DataBaseConnection.getInstance();

        var insertKonto = """
                
                    INSERT INTO konto (user_id,konto_id,kontostand) VALUES(?, ?, ?)
                
                """;

        var stmt = conn.prepareStatement(insertKonto);
        stmt.setString(1, konto.getUserId().toString());
        stmt.setString(2, konto.getKontoID().toString());
        stmt.setDouble(3, konto.getKontostand());

        stmt.executeUpdate();

    }

    public KontoId kontoIdErmittelnByUserId(UserId id) throws SQLException {

        Connection conn = DataBaseConnection.getInstance();

        var selectKonto =
                """
                        SELECT * FROM konto WHERE user_id = ?
                        """;

        var stmt = conn.prepareStatement(selectKonto);

        stmt.setString(1, id.toString());

        var resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            return new KontoId(resultSet.getString("konto_id"));
        } else {
            throw new SQLException("Konto nicht gefunden");
        }
    }


    public UserId ermittelUserIdByKontoId(KontoId id) throws SQLException {

        Connection conn = DataBaseConnection.getInstance();

        var selectKonto =
                """
                        SELECT user_id FROM konto WHERE konto_id = ?
                        """;

        var stmt = conn.prepareStatement(selectKonto);

        stmt.setString(1, id.toString());

        var resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            return new UserId(resultSet.getString("user_id"));
        } else {
            throw new SQLException("User nicht gefunden");
        }
    }


    public double ladeKontoStandVonKonto(KontoId id) throws SQLException {

        Connection conn = DataBaseConnection.getInstance();

        String ladeKontoStand = """
                
                SELECT kontostand FROM konto WHERE konto_id = ?
                """;

        PreparedStatement stmt = conn.prepareStatement(ladeKontoStand);

        stmt.setString(1, id.toString());

        ResultSet resultSet = stmt.executeQuery();

        if (resultSet.next()) {
            return resultSet.getDouble("kontostand");
        } else throw new SQLException("kontostand nicht gefunden");


    }

/*
    private UserName getUserNameByKontoId(KontoId kontoId) throws SQLException {

        Connection conn = DataBaseConnection.getInstance();

        String ladeKontoStand = """
                SELECT username FROM benutzer WHERE user_id = ?
                """;

        PreparedStatement stmt = conn.prepareStatement(ladeKontoStand);

        stmt.setString(1, kontoId.toString());

        ResultSet resultSet = stmt.executeQuery();
        return new UserName(resultSet.getString("username"));
    }
*/

    public void abheben(AbhebungsAnweisung anweisung) throws SQLException {

        Connection conn = DataBaseConnection.getInstance();

        //todo calc auslagern
        double neuerKontoStand = ladeKontoStandVonKonto(anweisung.getKontoId()) - anweisung.getBetrag();

        /// Befüllen Empfängerkonto
        String updateEmpfaengerKonto = """  
                UPDATE konto SET kontostand = ? WHERE konto_id = ?;
                """;

        PreparedStatement updateEmpfaengerKontoStatement = conn.prepareStatement(updateEmpfaengerKonto);

        updateEmpfaengerKontoStatement.setDouble(1, neuerKontoStand);
        updateEmpfaengerKontoStatement.setString(2, anweisung.getKontoId().toString());

        updateEmpfaengerKontoStatement.executeUpdate();
    }


    public void ueberweisen(UeberweisungsAnweisung anweisung) throws SQLException {

        Connection conn = DataBaseConnection.getInstance();


/// todo Kalkulieren der neuen Kontostände BITTE AUSLAGERN!**************************************************************

        /// calc für Empfänger
        double neuerKontoStandEmpfaenger = ladeKontoStandVonKonto(anweisung.getEmpfaengerId()) + anweisung.getBetrag();

        /// calc für Sender
        double neuerKontoStandSender = ladeKontoStandVonKonto(anweisung.getSenderId()) - anweisung.getBetrag();


///*********************************************************************************************************************


/// Befüllen der SQL STATEMENTS*****************************************************************************************

        /// Befüllen Empfängerkonto
        String updateEmpfaengerKonto = """  
                UPDATE konto SET kontostand = ? WHERE konto_id = ?;
                """;

        PreparedStatement updateEmpfaengerKontoStatement = conn.prepareStatement(updateEmpfaengerKonto);

        updateEmpfaengerKontoStatement.setDouble(1, neuerKontoStandEmpfaenger);
        updateEmpfaengerKontoStatement.setString(2, anweisung.getEmpfaengerId().toString());

        /// Befüllen Senderkonto
        String updateSenderKonto = """        
                UPDATE konto SET kontostand = ? WHERE konto_id = ?;
                """;

        PreparedStatement updateSenderKontoStatement = conn.prepareStatement(updateSenderKonto);

        updateSenderKontoStatement.setDouble(1, neuerKontoStandSender);
        updateSenderKontoStatement.setString(2, anweisung.getSenderId().toString());

///*********************************************************************************************************************

/// Statements an DB chicken *******************************************************************************************
        //todo executeUpdate gibt zurück wieviele zeilen betroffen sind, sollten die 0 sein(wie auch immer das möglich sein sollte) dann muss ja eigentlich alles re
        updateSenderKontoStatement.executeUpdate();
        updateEmpfaengerKontoStatement.executeUpdate();

/*
                                    VERALTET
/// bauen der TransaktionSummary ***************************************************************************************
        TransaktionSummary summary = new TransaktionSummary(
                getUserNameOfByKontoId(anweisung.empfaengerId())
                , anweisung.betrag()
                , anweisung.beschreibung());
*/


    }


}



/*
public KontoId kontoIdErmittelnByUserId(UserId id) throws SQLException {

    Connection conn = DataBaseConnection.getInstance();

    var selectKonto =
            """
                    SELECT * FROM konto WHERE user_id = ?
                    """;

    var stmt = conn.prepareStatement(selectKonto);

    stmt.setString(1, id.toString());

    var resultSet = stmt.executeQuery();
    if (resultSet.next()) {
        KontoBuilder builder = new KontoBuilder();
        return new KontoId(
                builder
                        .setKontoID(new KontoId(resultSet.getString("konto_id")))
                        .setUserId(new UserId(resultSet.getString("user_id")))
                        .setKontostand(resultSet.getDouble("kontostand")));
    } else {
        return null;
    }
}*/
