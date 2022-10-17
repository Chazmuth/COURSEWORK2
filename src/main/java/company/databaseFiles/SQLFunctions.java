package company.databaseFiles;


import company.DBUtils.ConnectionStatementPair;
import company.objects.graph.Edge;
import company.objects.graph.Graph;

import java.security.MessageDigest;
import java.sql.*;
import java.util.regex.Pattern;

public class SQLFunctions {

    protected static String databaseLocation = (System.getProperty("user.dir") + "\\freightRoutingSystemDatabase.accdb");

    public static ConnectionStatementPair init() {
        //a method to return the connection and statement for each method to make it need less code
        ConnectionStatementPair output;
        try {
            Connection connection = DriverManager.getConnection("jdbc:ucanaccess://" + databaseLocation, "", "");
            // sets up a connection with the database

            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            //forms a statement which is used to format the results from your SQL

            output = new ConnectionStatementPair(connection, statement);
        } catch (Exception e) {
            System.out.println("Error in the SQL class: " + e);
            output = new ConnectionStatementPair(null, null);
        }
        return output;
    }

    public static void getTable(String tableName) {
        try {
            ConnectionStatementPair connectionStatementPair = init();

            String sql = "SELECT * FROM " + tableName; //this is just the sql command
            ResultSet resultSet = connectionStatementPair.getStatement().executeQuery(sql);
            //executes the command

            //loops through the result set printing the result
            while (resultSet.next()) {
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = resultSet.getString(i);
                    System.out.print(resultSet.getMetaData().getColumnName(i) + " " + columnValue);
                }
                System.out.println("\n");
            }

            //closing connections so there are no deadlocks
            resultSet.close();
            connectionStatementPair.closeConnection();

        } catch (Exception e) {
            System.out.println("Error in the SQL class: " + e);
        }
    }

    public static Graph readGraph() {
        Graph graph;
        try {
            ConnectionStatementPair connectionStatementPair = init();

            String getTableSQL = "SELECT * FROM Edges";
            ResultSet resultSet = connectionStatementPair.getStatement().executeQuery(getTableSQL);

            String countSQL = "SELECT COUNT(*) FROM Edges AS count";
            ResultSet countResultSet = connectionStatementPair.getStatement().executeQuery(countSQL);

            countResultSet.next();
            graph = new Graph(countResultSet.getInt(1));

            while (resultSet.next()) {
                int source = Integer.parseInt(resultSet.getString("StartNode")) - 1;
                int destination = Integer.parseInt(resultSet.getString("EndNode")) - 1;
                StringBuilder stringCost = new StringBuilder(resultSet.getString("Cost"));
                stringCost.delete(stringCost.length() - 5, stringCost.length());
                int cost = Integer.parseInt(stringCost.toString());
                int[] edgeData = {source, destination, cost};
                graph.addEdge(new Edge(edgeData));
            }

        } catch (Exception e) {
            System.out.println("Error in the SQL class: " + e);
            graph = new Graph(0);
        }
        return graph;
    }

    public static boolean checkUser(String password, String emailAddress) {
        Boolean valid = false;

        password = hashPassword(password);

        try {
            ConnectionStatementPair connectionStatementPair = init();

            PreparedStatement statement = connectionStatementPair.getConnection().prepareStatement("SELECT password FROM Users WHERE userID=?");

            statement.setString(1, emailAddress);

            ResultSet resultSet = statement.executeQuery();
            //executes the command

            //checks that the password is the same as the one in the database
            while (resultSet.next()) {
                if (resultSet.getString("password").equals(password)) {
                    valid = true;
                }
            }

            //closing connections so there are no deadlocks
            resultSet.close();
            connectionStatementPair.closeConnection();

        } catch (Exception e) {
            System.out.println("Error in the SQL class: ");
            e.printStackTrace();
        }
        return valid;
    }

    public static void enterUser(String emailAddress, String password) {
        String hashedPassword = hashPassword(password);
        try {
            ConnectionStatementPair connectionStatementPair = init();

            PreparedStatement statement = connectionStatementPair.getConnection().prepareStatement("INSERT INTO Users(userID, password) VALUES(?,?)");

            statement.setString(1, emailAddress);
            statement.setString(2, hashedPassword);

            statement.executeUpdate();
            //executes the command
            System.out.println("User entered successfully");
        }catch (Exception e){
            System.out.println("Error in the SQL class: ");
            e.printStackTrace();
        }
    }

    public static boolean validateEmail(String emailAddress){
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        //allows for a regular email address only without dots before the @
        return Pattern.compile(regexPattern).matcher(emailAddress).matches();
    }

    public static String hashPassword(String password){
        String hashedPassword = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());
            hashedPassword = new String(messageDigest.digest());
        }catch(Exception e){
            e.printStackTrace();
        }
        return hashedPassword;
    }

    public static void main(String[] args) {
        System.out.println(checkUser("password", "hfuiah@gmail.com"));
    }
}