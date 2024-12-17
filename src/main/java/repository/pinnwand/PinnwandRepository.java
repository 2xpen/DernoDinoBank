package repository.pinnwand;

import data.identifier.UserId;
import data.pinnwand.Pinnwand;
import data.pinnwand.PinnwandEntry;
import repository.dbConnection.DataBaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PinnwandRepository {
    //todo @TOM
    public  List<PinnwandEntry> getPinnwandByUserId(data.identifier.UserId userId) throws SQLException {
        Connection conn = DataBaseConnection.getInstance();
        String selectPinnwandByUserId = """
                SELECT * FROM pinnwandentry WHERE besitzer_id = ?
                """;

        PreparedStatement preparedStatement = conn.prepareStatement(selectPinnwandByUserId);

        preparedStatement.setString(1, userId.toString());
        ResultSet resultSet = preparedStatement.executeQuery();

        List<PinnwandEntry> pinnwandEntryList = new ArrayList<>();

        while(resultSet.next()) {
            pinnwandEntryList.add(
                    new PinnwandEntry(
                            resultSet.getString("nachricht"),
                            new UserId(resultSet.getString("besitzer_id")),
                            new UserId(resultSet.getString("autor_id")),
                            resultSet.getTimestamp("date")
                    )
            );
        }
        return pinnwandEntryList;
    }

    public void createPinnwandentry(String message, data.identifier.UserId autor_id, data.identifier.UserId besitzer_id, Timestamp date) throws SQLException {

        Connection conn = DataBaseConnection.getInstance();
        String createPinnwandEntry = """
                INSERT INTO pinnwandentry(besitzer_id, autor_id, nachricht,date)
                VALUES(?, ?, ?,?)
                """;
        PreparedStatement preparedStatement = conn.prepareStatement(createPinnwandEntry);
        preparedStatement.setString(1, besitzer_id.toString());
        preparedStatement.setString(2, autor_id.toString());
        preparedStatement.setString(3, message);
        preparedStatement.setTimestamp(4,date);

        preparedStatement.execute();

        //todo return createte Pinnwandentry
    }
}
