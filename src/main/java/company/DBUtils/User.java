package company.DBUtils;

import company.databaseFiles.SQLFunctions;

public class User {
    int userID;
    String password;
    String emailAddress;

    public User(int userID, String password, String emailAddress) {
        this.userID = userID;
        this.password = password;
        this.emailAddress = emailAddress;
    }


}
