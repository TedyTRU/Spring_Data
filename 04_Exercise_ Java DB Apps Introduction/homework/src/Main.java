import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;

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
            case 2 -> exerciseTwo();
            case 3 -> exerciseThree();
            case 4 -> exerciseFour();
            case 5 -> exerciseFive();
            case 6 -> exerciseSix();
            case 7 -> exerciseSeven();
            case 8 -> exerciseEight();
            case 9 -> exerciseNine();
        }

    }

    private static void exerciseNine() throws IOException, SQLException {
        System.out.println("Enter minion id: ");
        int minion_id = Integer.parseInt(reader.readLine());

        CallableStatement callableStatement = connection
                .prepareCall("CALL usp_get_older (?)");
        callableStatement.setInt(1, minion_id);

        int affected = callableStatement.executeUpdate();

        PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT name, age FROM minions " +
                        "WHERE id = ?");
        preparedStatement.setInt(1, minion_id);
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            System.out.printf("%s %d %n", rs.getString("name"), rs.getInt("age"));
        }
    }

    private static void exerciseEight() throws IOException, SQLException {
        System.out.println("Enter minion IDs: ");
        int [] minionIDs = Arrays.stream(reader.readLine().split("\\s+"))
                .mapToInt(Integer::parseInt).toArray();

        for (int minionID : minionIDs) {
            PreparedStatement incrementMinionAge = connection
                    .prepareStatement("UPDATE minions " +
                            "SET age = age + 1, name = LOWER(name) " +
                            "WHERE id = ?");
            incrementMinionAge.setInt(1, minionID);
            incrementMinionAge.executeUpdate();
        }

        PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT name, age FROM minions");
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            System.out.println(rs.getString("name") + " "
                    + rs.getInt("age"));
        }

    }

    private static void exerciseSeven() throws SQLException {
        PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT name FROM minions");

        ResultSet rs = preparedStatement.executeQuery();

        List<String> allMinionsNames = new ArrayList<>();
//        ArrayDeque<String> allMinionsNames = new ArrayDeque<>();

        while (rs.next()) {
            allMinionsNames.add(rs.getString("name"));
        }

        int start = 0;
        int end = allMinionsNames.size() - 1;

        for (int i = 0; i < allMinionsNames.size(); i++) {
            System.out.println( i % 2 == 0
                                ? allMinionsNames.get(start++)
                                : allMinionsNames.get(end--));
        }
    }

    private static void exerciseSix() throws IOException, SQLException {
        System.out.println("Enter villain id: ");
        int villainId = Integer.parseInt(reader.readLine());

        String villainName = findEntityNameById("villains", villainId);

        if (villainName == null) {
            System.out.println("No such villain was found.");
            return;
        }

        int affectedEntities = deleteMinionsByVillainId(villainId);
        deleteVillainById(villainId);

        System.out.printf("%s was deleted%n%d minions released%n", villainName, affectedEntities);
    }

    private static void deleteVillainById(int villainId) throws SQLException {
        PreparedStatement preparedStatement = connection
                .prepareStatement("DELETE FROM villains WHERE id = ?");

        preparedStatement.setInt(1, villainId);
        preparedStatement.executeUpdate();
    }

    private static int deleteMinionsByVillainId(int villainId) throws SQLException {
        PreparedStatement preparedStatement = connection
                .prepareStatement("DELETE FROM minions_villains WHERE villain_id = ?");

        preparedStatement.setInt(1, villainId);

        return preparedStatement.executeUpdate();
    }

    private static void exerciseFive() throws IOException, SQLException {
        System.out.println("Enter country name: ");
        String countryName = reader.readLine();

        PreparedStatement preparedStatement = connection
                .prepareStatement("UPDATE towns " +
                        "SET name = UPPER(name) " +
                        "WHERE country = ?");
        preparedStatement.setString(1, countryName);

        int affectedRows = preparedStatement.executeUpdate();

        if (affectedRows == 0) {
            System.out.println("No town names were affected.");
            return;
        }

        System.out.printf("%d town names were affected.%n", affectedRows);

        PreparedStatement preparedStatementTowns = connection
                .prepareStatement("SELECT name FROM towns " +
                        "WHERE country = ?");
        preparedStatementTowns.setString(1, countryName);
        ResultSet towns = preparedStatementTowns.executeQuery();

        List<String> names = new ArrayList<>();
        while (towns.next()) {
            names.add(towns.getString("name"));
            //System.out.println(towns.getString("name"));
        }
        System.out.println(names);
    }

    private static void exerciseFour() throws IOException, SQLException {
        System.out.println("Enter input: ");
        String minionInput = reader.readLine().split(": ")[1];
        String minionName = minionInput.split(" ")[0];
        int minionAge = Integer.parseInt(minionInput.split(" ")[1]);
        String townName = minionInput.split(" ")[2];
        String villainName = reader.readLine().split(": ")[1];

        int townId = getOrInsertTown(townName);
        int villainId = getOrInsertVillain(villainName);

        PreparedStatement insertMinion = connection
                .prepareStatement("INSERT INTO minions (name, age, town_id) " +
                        "VALUES (?, ?, ?)");
        insertMinion.setString(1, minionName);
        insertMinion.setInt(2, minionAge);
        insertMinion.setInt(3, townId);
        insertMinion.executeUpdate();

        int minionId = findEntityIdByName("minions", minionName);

        PreparedStatement insertMinionsVillains = connection
                .prepareStatement("INSERT INTO minions_villains VALUES (?, ?)");
        insertMinionsVillains.setInt(1, minionId);
        insertMinionsVillains.setInt(2, villainId);
        insertMinionsVillains.executeUpdate();

        System.out.printf("Successfully added %s to be minion of %s.", minionName, villainName);
    }

    private static int getOrInsertTown(String townName) throws SQLException {
        int townId = findEntityIdByName("towns", townName);

        if (townId == 0) {

            PreparedStatement insertTown = connection
                    .prepareStatement("INSERT INTO towns (name) VALUES (?);");
            insertTown.setString(1, townName);
            insertTown.executeUpdate();

            townId = findEntityIdByName("towns", townName);

            System.out.printf("Town %s was added to the database.%n", townName);
        }

        return townId;
    }

    private static int getOrInsertVillain(String villainName) throws SQLException {
        int villainId = findEntityIdByName("villains", villainName);

        if (villainId == 0) {

            PreparedStatement insertVillain = connection
                    .prepareStatement("INSERT INTO villains (name, evilness_factor)" +
                            "VALUES (?, ?)");
            insertVillain.setString(1, villainName);
            insertVillain.setString(2, "evil");
            insertVillain.executeUpdate();

            villainId = findEntityIdByName("villains", villainName);

            System.out.printf("Villain %s was added to the database.%n", villainName);
        }

        return villainId;
    }

    private static int findEntityIdByName (String tableName, String entityName) throws SQLException {
        String query = String.format("SELECT id FROM %s WHERE name = ?", tableName);
        PreparedStatement preparedStatement = connection
                .prepareStatement(query);
        preparedStatement.setString(1, entityName);
        ResultSet rs = preparedStatement.executeQuery();

        if (rs.next()) {
            return rs.getInt(1);
        }

        return 0;
    }

    private static void exerciseThree() throws IOException, SQLException {
        System.out.println("Enter villain id: ");
        int villainId = Integer.parseInt(reader.readLine());

//        String villainName = findVillainNameBId(villainId);
        String villainName = findEntityNameById("villains", villainId);

        if (villainName == null) {
            System.out.printf("No villain with ID %d exists in the database.", villainId);
            return;
        }

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

        if (rs.next()) {
            return rs.getString(1);
        }

        return null;
    }

    private static String findVillainNameById(int villainId) throws SQLException {
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
        PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT v.name, COUNT(DISTINCT mv.minion_id) AS 'count' FROM villains AS v " +
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