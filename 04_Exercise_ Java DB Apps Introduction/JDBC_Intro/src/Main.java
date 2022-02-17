import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Properties;

public class Main {

    private static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "minions_db";
    public static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws SQLException, IOException {

        System.out.println("Enter user:");
        String user = reader.readLine();
        System.out.println("Enter password:");
        String password = reader.readLine();

        Properties properties = new Properties();
        properties.setProperty("user", user);
        properties.setProperty("password", password);

        Connection connection = DriverManager
                .getConnection(CONNECTION_STRING + DB_NAME, properties);


        PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT * FROM minions WHERE id < ?;");

        System.out.println("Enter max id");
        int maxId = Integer.parseInt(reader.readLine());

        preparedStatement.setInt(1, maxId);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.printf("%s %d %n", resultSet.getString("name"),
                    resultSet.getInt("age"));

        }

    }
}