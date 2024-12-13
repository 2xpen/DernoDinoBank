package repository;

import data.geschaeftsvorfall.GevoArt;
import data.geschaeftsvorfall.GevoZeile;
import data.geschaeftsvorfall.AbhebungGevo;
import data.geschaeftsvorfall.UeberweisungGevo;
import data.identifier.KontoId;
import repository.dbConnection.DataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GevoRepository {


    private static PreparedStatement getPreparedStatement() throws SQLException {
        Connection conn = DataBaseConnection.getInstance();

        var insertgevo = """
                
                    INSERT INTO geschaeftsvorfaelle (
                      gevo_id
                    , gevo_art
                    , transaktionsdatum
                    , sender
                    , empfaenger
                    , beschreibung
                    , betrag)
                
                    VALUES(?,?,?,?,?,?,?)
                
                """;

        var stmt = conn.prepareStatement(insertgevo);
        return stmt;
    }

    /**
     * mit dieser Methode wird jeder Ueberweisungsgevo abgehandelt, gut wie last -schrift es ist ja nur die art verschieden
     */
    public void createUeberweisungsGevo(UeberweisungGevo gevo) throws SQLException {

        var stmt = getPreparedStatement();
        stmt.setString(1, gevo.getGevoId().toString());
        stmt.setString(2, gevo.getArt().toString());
        stmt.setTimestamp(3, gevo.getTimestamp());
        stmt.setString(4, gevo.getSenderId().toString());
        stmt.setString(5, gevo.getEmpfaengerId().toString());
        stmt.setString(6, gevo.getBeschreibung());
        stmt.setDouble(7, gevo.getBetrag());


        stmt.execute();
    }

    public void createAbhebungsGevo(AbhebungGevo gevo) throws SQLException {

        Connection conn = DataBaseConnection.getInstance();

        var insertgevo = """
                
                    INSERT INTO geschaeftsvorfaelle (
                      gevo_id
                    , gevo_art
                    , transaktionsdatum
                    , sender
                    , beschreibung
                    , betrag)
                
                    VALUES(?,?,?,?,?,?)
                
                """;

        var stmt = conn.prepareStatement(insertgevo);
        stmt.setString(1, gevo.getGevoId().toString());
        stmt.setString(2, gevo.getArt().toString());
        stmt.setTimestamp(3, gevo.getTimestamp());
        stmt.setString(4, gevo.getSenderId().toString());
        stmt.setString(5, gevo.getBeschreibung());
        stmt.setDouble(6, gevo.getBetrag());


        stmt.execute();
    }

    public List<GevoZeile> fetchGevosOfKonto(KontoId id) throws SQLException {

        Connection conn = DataBaseConnection.getInstance();

        String select = """
                SELECT * FROM geschaeftsvorfaelle
                WHERE sender = ? OR empfaenger = ? 
                ORDER BY transaktionsdatum DESC
                """;
        PreparedStatement stmt = conn.prepareStatement(select);
        stmt.setString(1, id.toString());
        stmt.setString(2, id.toString());

        ResultSet rs = stmt.executeQuery();

        List<GevoZeile> gevos = new ArrayList<>();

        while (rs.next()) {

            KontoId empfaenger = rs.getString("empfaenger") != null ? new KontoId(rs.getString("empfaenger")) : null;

            gevos.add(new GevoZeile(
                    rs.getTimestamp("transaktionsdatum"),
                    empfaenger,
                    new KontoId(rs.getString("sender")),
                    rs.getString("beschreibung"),
                    rs.getDouble("betrag"),
                    GevoArt.ermittleGevoAusString(rs.getString("gevo_art"))
            ));
        }
        return gevos;
    }


}