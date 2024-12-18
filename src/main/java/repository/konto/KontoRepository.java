package repository.konto;

import data.anweisungen.UpdateAbhebenKontostand;
import data.anweisungen.UpdateSenderEmpfaengerKontostaende;
import data.identifier.KontoId;
import data.identifier.UserId;
import data.konto.Konto;
import repository.dbConnection.DataBaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class KontoRepository {
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

    public void abheben(UpdateAbhebenKontostand sardz) throws SQLException {
        Connection conn = DataBaseConnection.getInstance();

        String updateEmpfaengerKonto = """  
                UPDATE konto SET kontostand = ? WHERE konto_id = ?;
                """;

        PreparedStatement updateEmpfaengerKontoStatement = conn.prepareStatement(updateEmpfaengerKonto);

        updateEmpfaengerKontoStatement.setDouble(1, sardz.getNeuerKontostand());
        updateEmpfaengerKontoStatement.setString(2, sardz.getKontoId().toString());

        updateEmpfaengerKontoStatement.executeUpdate();
    }

    public void ueberweisen(UpdateSenderEmpfaengerKontostaende caculatedBalances) throws SQLException {
        Connection conn = DataBaseConnection.getInstance();

        String updateEmpfaengerKonto = """  
                UPDATE konto SET kontostand = ? WHERE konto_id = ?;
                """;

        PreparedStatement updateEmpfaengerKontoStatement = conn.prepareStatement(updateEmpfaengerKonto);

        updateEmpfaengerKontoStatement.setDouble(1, caculatedBalances.getNeuerEmpfeangerKontoStand());
        updateEmpfaengerKontoStatement.setString(2, caculatedBalances.getEmpfaengerId().toString());

        String updateSenderKonto = """        
                UPDATE konto SET kontostand = ? WHERE konto_id = ?;
                """;

        PreparedStatement updateSenderKontoStatement = conn.prepareStatement(updateSenderKonto);

        updateSenderKontoStatement.setDouble(1,caculatedBalances.getNeuerSenderKontoStand());
        updateSenderKontoStatement.setString(2, caculatedBalances.getSenderId().toString());
        updateSenderKontoStatement.executeUpdate();
        updateEmpfaengerKontoStatement.executeUpdate();
    }
}