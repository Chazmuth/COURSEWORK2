package company;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import company.DBUtils.JobUtils.EntryJob;
import company.DBUtils.JobUtils.Job;

import static company.DBUtils.JobUtils.Job.parseDate;

import company.DBUtils.SQLFunctions;
import company.objects.graph.Path;
import company.objects.neuralNetwork.trainingDataGeneration.DijkstraShortestPath;
import dnl.utils.text.table.TextTable;


public class Main {
    protected String user;

    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }

    public boolean login(Scanner input) {
        //add input validation
        boolean accessToken;
        System.out.println("Enter Email: ");
        String username = input.next();
        System.out.println("Enter Password: ");
        String password = input.next();
        System.out.println("Validating...");
        accessToken = SQLFunctions.checkUser(password, username);
        if (accessToken) {
            this.user = username;
        }
        return accessToken;
    }

    public void signup(Scanner input) {
        System.out.println("Enter Email: ");
        String email = input.next();
        System.out.println("Enter Password: ");
        String password = input.next();
        SQLFunctions.enterUser(email, password);
    }

    public void loginLogic(Scanner input) {
        while (true) {
            System.out.println("Would you like to log in (l), or sign up (s)");
            String decision = input.next();
            if (decision.equals("l")) {
                if (login(input)) {
                    System.out.println("Access Granted");
                    break;
                } else {
                    System.out.println("Please check you username or password");
                }
            }
            if (decision.equals("s")) {
                signup(input);
            }
            if (decision.equals("a")) {
                this.user = "henryjobling@gmail.com";
                break;
            }
        }
    }

    public void addJob(Scanner input) {
        System.out.println("Enter Start Node: ");
        int source = input.nextInt();
        System.out.println("Enter Destination Node: ");
        int destination = input.nextInt();
        System.out.println("Calculating...");
        Path route = DijkstraShortestPath.dijkstra(source, destination);
        System.out.println("Enter a date in the format [yyyy-MM-dd]: ");
        String startDate = input.next();

        System.out.println(startDate);
        int time = route.getCost();
        EntryJob newJob = new EntryJob(this.user, startDate, false,
                time, route);
        SQLFunctions.enterJob(newJob);
    }

    public void viewJobs(Scanner input) {
        System.out.println("fetching...");
        ArrayList<Job> jobs = SQLFunctions.getJobs(this.user);
        String[] columnNames = {
                "ID", "Start Date", "End Date", "Complete"
        };
        Object[][] tableData = new Object[jobs.size()][];
        for (int i = 0; i < jobs.size(); i++) {
            tableData[i] = jobs.get(i).toArray();
        }
        TextTable textTable = new TextTable(columnNames, tableData);
        textTable.setAddRowNumbering(true);
        textTable.printTable();
        System.out.println("\n");
    }

    public void run() {
        Scanner input = new Scanner(System.in);
        System.out.println("Welcome to the Freight Routing System");
        loginLogic(input);
        boolean logoutToken = false;
        while (true) {
            if (logoutToken) {
                break;
            } else {
                System.out.println("""
                        Please Choose a Function\s
                        Add Job [1]\s
                        View Jobs [2]\s
                        Log Out [3]""");
                int decision = input.nextInt();
                switch (decision) {
                    case 1 -> addJob(input);
                    case 2 -> viewJobs(input);
                    case 3 -> logoutToken = true;
                }
            }
        }
    }
}
