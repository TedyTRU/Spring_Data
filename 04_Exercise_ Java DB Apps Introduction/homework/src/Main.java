import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

public class Main {

    private static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "minions_db";
    public static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Connection connection;

    public static void main(String[] args) throws SQLException, IOException {
        connection = getConnection();

        System.out.println("Enter exercise number: ");
        int exNum = Integer.parseInt(reader.readLine());

        switch (exNum) {
            case 2: exerciseTwo();
            case 3: exerciseThree();
        }



    }

    private static void exerciseThree() throws IOException, SQLException {
        System.out.println("Enter villain id: ");
        int villainId = Integer.parseInt(reader.readLine());

//        String villainName = findVillainNameBId(villainId);
        String villainName = findEntityNameById("villains", villainId);

        System.out.printf("Villain: %s%n", villainName);

        Set<String> minions = getAllMinionsByVillainId(villainId);
        minions.forEach(System.out::println);

    }

    private static Set<String> getAllMinionsByVillainId(int id) throws SQLException {
        Set<String> result = new LinkedHashSet<>();
        PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT m.name, age FROM minions m " +
                        "JOIN minions_villains mv on m.id = mv.minion_id " +
                        "WHERE villain_id = ?;");
        preparedStatement.setInt(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();
//        int counter = 0;

        while (resultSet.next()) {
            result.add(String.format("%d. %s %d", resultSet.getRow(),
                    resultSet.getString("name"),
                    resultSet.getInt("age")));
        }
        return result;
    }

    private static String findEntityNameById(String tableName, int entityId) throws SQLException {
        String query = String.format("SELECT name FROM %s WHERE id = ?", tableName);
        PreparedStatement preparedStatement = connection
                .prepareStatement(query);
        preparedStatement.setInt(1, entityId);
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();

        return rs.getString(1);
    }

    private static String findVillainNameBId(int villainId) throws SQLException {
        PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT name FROM villains WHERE id = ?");
        preparedStatement.setInt(1, villainId);

        ResultSet rs = preparedStatement.executeQuery();

        if (rs.isBeforeFirst()) {
            rs.next();
            return rs.getString("name");
        }
        return null;
    }

    private static void exerciseTwo() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT v.name, COUNT(DISTINCT mv.minion_id) AS 'count' FROM villains AS v " +
                "JOIN minions_villains AS mv ON v.id = mv.villain_id " +
                "GROUP BY mv.villain_id " +
                "HAVING count > ? " +
                "ORDER BY count DESC;");

        preparedStatement.setInt(1, 15);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            String name = resultSet.getString(1);
            Integer count = resultSet.getInt(2);

            System.out.printf("%s %d %n", name, count);
        }
    }

    private static Connection getConnection() throws IOException, SQLException {
        System.out.println("Enter user: ");
        String user = reader.readLine().equals("") ? "root" : reader.readLine();
        System.out.println("Enter password: ");
        String password = reader.readLine().equals("") ? "123456" : reader.readLine();

        Properties properties = new Properties();
        properties.setProperty("user", user);
        properties.setProperty("password", password);

        return DriverManager
                .getConnection(CONNECTION_STRING + DB_NAME, properties);
    }
}

