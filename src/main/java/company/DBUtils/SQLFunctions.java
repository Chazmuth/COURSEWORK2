package company.DBUtils;

import company.DBUtils.JobUtils.EntryJob;
import company.DBUtils.JobUtils.Job;
import company.objects.graph.Edge;
import company.objects.graph.Graph;
import company.objects.graph.Path;
import company.objects.graph.Vertex;

import java.io.File;
import java.security.MessageDigest;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import static company.objects.neuralNetwork.trainingDataGeneration.DijkstraShortestPath.dijkstra;

public class SQLFunctions {

    //   protected static String databaseLocation = (System.getProperty("user.dir") + "\\freightRoutingSystemDatabase.accdb");
    protected static String databaseLocation = ("freightRoutingSystemDatabase.accdb");


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
                StringBuilder stringCost = new StringBuilder(resultSet.getString("distance"));
                int cost = Integer.parseInt(stringCost.toString());
                int[] edgeData = {source, destination, cost};
                graph.addEdge(new Edge(edgeData));
            }

            connectionStatementPair.closeConnection();

        } catch (Exception e) {
            System.out.println("Error in the SQL class: " + e);
            graph = new Graph(0);
        }
        return graph;
    }

    public static boolean checkUser(String password, String emailAddress) {
        boolean valid = false;

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
        if (validateEmail(emailAddress)) {
            try {
                ConnectionStatementPair connectionStatementPair = init();

                PreparedStatement statement = connectionStatementPair.getConnection().
                        prepareStatement("INSERT INTO Users(userID, password) VALUES(?,?)");

                statement.setString(1, emailAddress);
                statement.setString(2, hashedPassword);

                statement.executeUpdate();
                //executes the command
                statement.close();
                connectionStatementPair.getConnection().close();

                System.out.println("User entered successfully");
            } catch (Exception e) {
                System.out.println("Error in the SQL class: ");
                e.printStackTrace();
            }
        } else {
            System.out.println("Please enter a valid email address");
        }
    }

    public static void enterJob(EntryJob entryJob) {
        try {
            ConnectionStatementPair connectionStatementPair = init();

            PreparedStatement statement = connectionStatementPair.getConnection().
                    prepareStatement("INSERT INTO Jobs(userID, endDate, startDate, complete) VALUES(?,?,?,?)");

            statement.setString(1, entryJob.getUserID());
            statement.setDate(2, Date.valueOf(entryJob.getEndDate()));
            statement.setDate(3, Date.valueOf(entryJob.getStartDate()));
            statement.setBoolean(4, false);

            statement.executeUpdate();
            //executes the command

            //gets the generated jobID and inputs the associated job nodes
            ResultSet resultSet = statement.getGeneratedKeys();
            int generatedJobID = -1;

            if (resultSet.next()) {
                generatedJobID = resultSet.getInt(1);
            }

            System.out.println(generatedJobID);

            //iterate through the node array in jobs to add each jobnode


            for (int i = 0; i < entryJob.getPath().size(); i++) {
                enterJobNode(generatedJobID, entryJob.getPath().get(i));
            }

            resultSet.close();
            connectionStatementPair.getConnection().close();
            statement.close();
            System.out.println("Job entered successfully");

        } catch (Exception e) {
            System.out.println("Error in the SQL class: ");
            e.printStackTrace();
        }
    }

    public static ArrayList<Job> getJobs(String username) {
        ArrayList<Job> jobs = new ArrayList<>();
        try {
            ConnectionStatementPair connectionStatementPair = init();

            PreparedStatement statement = connectionStatementPair.getConnection().prepareStatement("SELECT userID,startDate,endDate,complete FROM Jobs WHERE userID = ?");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            //executes the command

            //loops through the result set printing the result
            if (!resultSet.next()) {
                System.out.println("You Have No Jobs\nPlease Add a Job to View Your Jobs");
            }

            while (resultSet.next()) {
                ArrayList<String> jobArray = new ArrayList<>();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    jobArray.add(resultSet.getString(i));
                }
                jobs.add(new Job(jobArray));
            }

            //closing connections so there are no deadlocks
            resultSet.close();
            connectionStatementPair.closeConnection();

        } catch (Exception e) {
            System.out.println("Error in the SQL class: ");
            e.printStackTrace();
        }
        return jobs;
    }

    public static void enterJobNode(int jobID, int nodeID) {
        try {
            ConnectionStatementPair connectionStatementPair = init();
            PreparedStatement statement = connectionStatementPair.getConnection().
                    prepareStatement("INSERT INTO JobNodes(jobID, nodeID) VALUES(?,?)");

            statement.setInt(1, jobID);
            statement.setInt(2, nodeID);

            statement.executeUpdate();

            connectionStatementPair.getConnection().close();
            statement.close();

        } catch (Exception e) {
            System.out.println("Error in the SQL class: ");
            e.printStackTrace();
        }
    }

    public static boolean validateEmail(String emailAddress) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        //allows for a regular email address only without dots before the @
        return Pattern.compile(regexPattern).matcher(emailAddress).matches();
    }

    public static String hashPassword(String password) {
        String hashedPassword = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());
            hashedPassword = new String(messageDigest.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hashedPassword;
    }

    public static void enterNode(int nodeID, String name) {
        try {
            ConnectionStatementPair connectionStatementPair = init();
            PreparedStatement statement = connectionStatementPair.getConnection().
                    prepareStatement("INSERT INTO Nodes(nodeID ,name, areaID) VALUES(?,?,?)");
            //

            statement.setInt(1, nodeID);
            statement.setString(2, name);
            statement.setInt(3, 1);

            statement.executeUpdate();

            connectionStatementPair.getConnection().close();
            statement.close();

        } catch (Exception e) {
            System.out.println("Error in the SQL class: ");
            e.printStackTrace();
        }
    }

    public static void enterEdge(int edgeID, float cost, float time, float CO2, int startNode, int endNode, float distance) {
        try {
            ConnectionStatementPair connectionStatementPair = init();
            PreparedStatement statement = connectionStatementPair.getConnection().
                    prepareStatement("INSERT INTO Edges(edgeID ,cost, Time, CO2, StartNode, EndNode, Distance) VALUES(?,?,?,?,?,?,?)");
            //

            statement.setInt(1, edgeID);
            statement.setFloat(2, cost);
            statement.setFloat(3, time);
            statement.setFloat(4, CO2);
            statement.setInt(5, startNode);
            statement.setInt(6, endNode);
            statement.setFloat(7, distance);


            statement.executeUpdate();

            connectionStatementPair.getConnection().close();
            statement.close();

        } catch (Exception e) {
            System.out.println("Error in the SQL class: ");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Scanner fileReader = new Scanner(new File("src/main/java/company/databaseFiles/fullGraph/edges"));
            while (fileReader.hasNext()) {
                String[] data = fileReader.next().split(",");
                for (int i = 0; i < data.length; i++) {
                    System.out.println(i + ":" + data[i]);
                }
                enterEdge(Integer.parseInt(data[0]), Float.parseFloat(data[4]), Float.parseFloat(data[6]),
                        Float.parseFloat(data[5]), Integer.parseInt(data[1]), Integer.parseInt(data[2]), Float.parseFloat(data[3]));
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}


//add more information to the job table such as the total cost, total time, total distance