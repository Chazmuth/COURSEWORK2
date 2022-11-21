package company;

import java.util.Scanner;

import company.DBUtils.SQLFunctions;

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
        accessToken = SQLFunctions.checkUser(password, username);
        if(accessToken){
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
            if(decision.equals("a")){
                this.user = "henryjobling@gmail.com";
                break;
            }
        }
    }

    public void addJob(Scanner input) {
        System.out.println("adding jobs");
    }

    public void viewJobs(Scanner input) {
        SQLFunctions.getJobs(this.user);
    }

    public void run(){
        Scanner input = new Scanner(System.in);
        System.out.println("Welcome to the Freight Routing System");
        loginLogic(input);
        Boolean logoutToken = false;
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
