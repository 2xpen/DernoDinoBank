package repository.directmessages;

import data.identifier.UserId;
import data.nachricht.Nachricht;
import repository.dbConnection.DataBaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DirectMessagesRepository {

    //todo @TOM

    public List<Nachricht> getNachrichtenByUserId(UserId userId) throws SQLException {
        Connection conn = DataBaseConnection.getInstance();
        String selectAlleNachrichtenByUserId = """
                        SELECT * FROM directmessages WHERE empfaenger = ?
                """;

        PreparedStatement preparedStatement = conn.prepareStatement(selectAlleNachrichtenByUserId);
        preparedStatement.setString(1, userId.toString());

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Nachricht> alleNachrichten = new ArrayList<>();

        while (resultSet.next()) {
            alleNachrichten.add(
                    new Nachricht(
                            resultSet.getTimestamp("date"),
                            new UserId(resultSet.getString("sender")),
                            new UserId(resultSet.getString("empfaenger")),
                            resultSet.getString("message")
                    )
            );
        }
        return alleNachrichten;
    }

    //todo wrapperklasse für createDirectMessage anweisung
    public void createDirectMessage(Timestamp date, UserId sender, UserId empfaenger, String message) throws SQLException {
        Connection conn = DataBaseConnection.getInstance();
        String createDirectMessage = """
                INSERT INTO directmessages(date, sender, empfaenger, message)
                VALUES(?, ?, ?, ?)
                """;
        PreparedStatement preparedStatement = conn.prepareStatement(createDirectMessage);
        preparedStatement.setTimestamp(1, date);
        preparedStatement.setString(2, sender.toString());
        preparedStatement.setString(3, empfaenger.toString());
        preparedStatement.setString(4, message);
        //todo auswertung affected rows
        preparedStatement.execute();
    }
}
