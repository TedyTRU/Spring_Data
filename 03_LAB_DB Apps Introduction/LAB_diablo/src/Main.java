import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter username default (root): ");
        String user = sc.nextLine();
        user = user.equals("") ? "root" : user;
        System.out.println();

        System.out.print("Enter password default (empty):");
        String password = sc.nextLine().trim();
        System.out.println();

        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", password);

        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/diablo", props);

        PreparedStatement stmt =
                connection.prepareStatement("SELECT user_name, first_name, last_name, COUNT(users_games.id) FROM users " +
                        "JOIN users_games ON users_games.user_id = users.id " +
                        "WHERE user_name = ?" +
                        "GROUP BY users_games.user_id;");

        String userName = sc.nextLine();
        stmt.setString(1, userName);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()){
            String dbUserName = rs.getString("user_name");
            String dbFirstName = rs.getString("first_name");
            String dbLastName = rs.getString("last_name");
            Integer dbGamesCount = rs.getInt("COUNT(users_games.id)");

            System.out.printf("User: %s\n%s %s has played %d games\n", dbUserName, dbFirstName, dbLastName, dbGamesCount);
        } else {
            System.out.println("No such user exists");
        }
        connection.close();
    }
}
