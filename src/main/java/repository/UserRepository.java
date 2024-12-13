package repository;

import data.identifier.UserId;
import data.user.UserName;
import data.user.Passwort;
import data.user.User;
import repository.dbConnection.DataBaseConnection;

import java.sql.*;


public class UserRepository {


    //todo @DBCONNECTION hier werden zwei technische prozesse gebraucht,
    public User anmelden(UserName userName, Passwort password) throws SQLException {

        Connection conn = DataBaseConnection.getInstance();

        var selectUser = """
                
                        SELECT * FROM benutzer WHERE username = ? AND password = ?
                """;

        var stmt = conn.prepareStatement(selectUser);

        stmt.setString(1, userName.toString());
        stmt.setString(2, password.toString());

        var resultSet = stmt.executeQuery();

        if (resultSet.next()) {
            return new User(
                    new UserId(resultSet.getString("user_Id")),
                    new UserName(resultSet.getString("username")));

        }
        return null;
    }

    /**
     * todo @DBCONNECTION bei registrieren muss noch ein zusammenhängendes Konto in "konto" erstellt werden,
     * sinnvoll wär es das es zwei methoden gibt für user erstellen und für konto erstellen,
     * registrieren würde dann nur die beiden methoden zusammenführen
     */


    public void createUser(User user, Passwort passwort) throws SQLException {

        Connection conn = DataBaseConnection.getInstance();

        var insertPerson = """
                
                    INSERT INTO benutzer (user_id, username, password) VALUES(?, ?, ?)
                
                """;

        var stmt = conn.prepareStatement(insertPerson);
        stmt.setString(1, user.getUserId().toString());
        stmt.setString(2, user.getUsername().toString());
        stmt.setString(3, passwort.toString());

        stmt.executeUpdate();
    }

    public boolean userNameExists(UserName userId) throws SQLException {

        Connection conn = DataBaseConnection.getInstance();


        var selectUser = """
                
                        SELECT * FROM benutzer WHERE username = ?
                """;


        PreparedStatement preparedStatement = conn.prepareStatement(selectUser);

        preparedStatement.setString(1, userId.toString());

        ResultSet resultSet = preparedStatement.executeQuery();

        return resultSet.next();
    }

    public User findUserByName(UserName userId) throws SQLException {

        Connection conn = DataBaseConnection.getInstance();

        var selectUser = """
                
                        SELECT * FROM benutzer WHERE username = ?
                """;

        PreparedStatement preparedStatement = conn.prepareStatement(selectUser);

        preparedStatement.setString(1, userId.toString());

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return new User(
                    new UserId(resultSet.getString("user_id")),
                    new UserName(resultSet.getString("username")));
        } else throw new SQLException("User nicht gefunden");
    }

    public User getUserByUserId(UserId userId) throws SQLException {
        Connection conn = DataBaseConnection.getInstance();

        String selectUserByUserId = """
                SELECT * FROM benutzer 
                WHERE user_id = ?
                """;
        PreparedStatement preparedStatement = conn.prepareStatement(selectUserByUserId);

        preparedStatement.setString(1, userId.toString());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return new User(
                    new data.identifier.UserId(
                            resultSet.getString("user_id")
                    )
                    , new UserName(
                    resultSet.getString("username")
            )
            );
        } else throw new SQLException("User nicht gefunden");
    }
/*
    public Konto getKontoById(KontoId id) throws SQLException {

        Connection conn = DataBaseConnection.getInstance();

        var select = """
                
                    SELECT * FROM konto where konto_id = ?
                
                """;

        var stmt = conn.prepareStatement(select);
        stmt.setString(1, id.toString());

        ResultSet resultSet = stmt.executeQuery();

        if (resultSet.next()) {

            KontoBuilder builder = new KontoBuilder();

            return new Konto(
                    builder
                            .setKontoID(new KontoId(resultSet.getString("konto_id")))
                            .setUserId(new UserId(resultSet.getString("user_id")))
                            .setKontostand(resultSet.getDouble(resultSet.getString("kontostand")))
            );

        } else
            return null;

    }
*/

}